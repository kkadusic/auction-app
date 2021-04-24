package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.exception.BadGatewayException;
import com.atlantbh.auctionapp.exception.BadRequestException;
import com.atlantbh.auctionapp.exception.ConflictException;
import com.atlantbh.auctionapp.exception.UnauthorizedException;
import com.atlantbh.auctionapp.model.Card;
import com.atlantbh.auctionapp.model.Person;

import com.atlantbh.auctionapp.model.Token;
import com.atlantbh.auctionapp.repository.CardRepository;
import com.atlantbh.auctionapp.repository.PersonRepository;

import com.atlantbh.auctionapp.repository.TokenRepository;
import com.atlantbh.auctionapp.request.CardRequest;
import com.atlantbh.auctionapp.request.ForgotPasswordRequest;
import com.atlantbh.auctionapp.request.LoginRequest;
import com.atlantbh.auctionapp.request.RegisterRequest;

import com.atlantbh.auctionapp.request.ResetPasswordRequest;
import com.atlantbh.auctionapp.request.TokenRequest;
import com.atlantbh.auctionapp.request.UpdateProfileRequest;
import com.atlantbh.auctionapp.security.JwtTokenUtil;
import com.atlantbh.auctionapp.utilities.UpdateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.atlantbh.auctionapp.utilities.ResourceUtil.getResourceFileAsString;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final UpdateMapper updateMapper;

    private String hostUrl;

    @Value("${app.hostUrl}")
    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    @Autowired
    public PersonService(PersonRepository personRepository, CardRepository cardRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository, EmailService emailService, UpdateMapper updateMapper) {
        this.personRepository = personRepository;
        this.cardRepository = cardRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.updateMapper = updateMapper;
    }

    public Person register(RegisterRequest registerRequest) {
        if (personRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException("Email already in use");
        }
        Person person = personRepository.save(new Person(
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()))
        );
        person.setPassword(null);
        return person;
    }

    public Person login(LoginRequest loginRequest) {
        Person person = personRepository.findByEmail(loginRequest.getEmail());
        if (person == null || !passwordEncoder.matches(loginRequest.getPassword(), person.getPassword())) {
            throw new UnauthorizedException("Wrong email or password");
        }
        if (!person.getActive()) {
            throw new UnauthorizedException("User account disabled");
        }
        person.setPassword(null);
        return person;
    }

    public String forgotPassword(ForgotPasswordRequest forgotPassRequest) {
        String message = "We sent you an email with a link to reset your password. " +
                "The link will expire after 24 hours.";
        Optional<Person> personOptional = personRepository.findByEmailAndActiveIsTrue(forgotPassRequest.getEmail());
        if (personOptional.isEmpty())
            return message;
        Person person = personOptional.get();
        if (tokenRepository.existsByPerson(person.getId()))
            return "We have already sent you an email with a link to reset your password " +
                    "in the last 24 hours. Please check your inbox.";
        UUID uuid = UUID.randomUUID();
        String body = formEmailBody(hostUrl, uuid);
        try {
            emailService.sendMail(person.getEmail(), "Password reset", body);
        } catch (MessagingException e) {
            throw new BadGatewayException("We have issues sending you an email");
        }
        Token token = new Token(uuid, person);
        tokenRepository.save(token);
        return message;
    }

    private String formEmailBody(String hostUrl, UUID uuid) {
        String body = getResourceFileAsString("static/mail.html");
        return body.replace("hostUrl", hostUrl + "/reset_password?token=" + uuid);
    }

    public String resetPassword(ResetPasswordRequest resetPassRequest) {
        Token token = tokenRepository.getToken(resetPassRequest.getToken().toString())
                .orElseThrow(() -> new BadRequestException("Invalid token"));
        Person person = personRepository.findById(token.getPerson().getId())
                .orElseThrow(() -> new BadRequestException("Invalid token"));

        person.setPassword(passwordEncoder.encode(resetPassRequest.getPassword()));
        personRepository.save(person);

        token.setUsed(true);
        tokenRepository.save(token);

        return "You have changed your password";
    }

    public Boolean validToken(TokenRequest tokenRequest) {
        Token token = tokenRepository.getToken(tokenRequest.getToken()).orElse(new Token());
        return token.getId() != null && personRepository.existsById(token.getPerson().getId());
    }

    public Person update(UpdateProfileRequest updateProfileRequest) {
        if (updateProfileRequest.getBirthDate().isAfter(LocalDateTime.now()))
            throw new BadRequestException("Date of birth can't be after current date");
        Long personId = JwtTokenUtil.getRequestPersonId();
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new UnauthorizedException("Wrong person id"));

        if (!person.getEmail().equals(updateProfileRequest.getEmail())
                && personRepository.existsByEmail(updateProfileRequest.getEmail()))
            throw new ConflictException("Email already in use");
        updateCard(updateProfileRequest.getCard(), person);
        updateMapper.updatePerson(updateProfileRequest, person);
        setBlankPropsToNull(person);
        person.setBirthDate(updateProfileRequest.getBirthDate());
        person.setPhoneNumber(updateProfileRequest.getPhoneNumber());
        System.out.println(updateProfileRequest.getBirthDate());
        if (updateProfileRequest.getImageUrl() != "http://www.gnd.center/bpm/resources/img/avatar-placeholder.gif"){
            person.setImageUrl(updateProfileRequest.getImageUrl());
        }
        Person savedPerson = personRepository.save(person);
        savedPerson.setPassword(null);
        return savedPerson;
    }

    private void updateCard(CardRequest updatedCard, Person person) {
        if (updatedCard != null) {
            Card card = cardRepository.findByPersonId(person.getId()).orElse(new Card(person));
            String maskedCardNumber = card.getMaskedCardNumber();
            if (maskedCardNumber != null && maskedCardNumber.equals(updatedCard.getCardNumber()))
                updatedCard.setCardNumber(card.getCardNumber());
            else if (!updatedCard.getCardNumber().matches("^(\\d*)$"))
                throw new BadRequestException("Card number can only contain digits");
            updateMapper.updateCard(updatedCard, card);
            cardRepository.save(card);
        } else {
            List<Card> cards = cardRepository.findAllByPersonId(person.getId());
            for (Card card : cards) {
                card.setPerson(null);
                cardRepository.save(card);
            }
        }
    }


    private void setBlankPropsToNull(Person person) {
        if (person.getStreet().equals(""))
            person.setStreet(null);
        if (person.getCountry().equals(""))
            person.setCountry(null);
        if (person.getCity().equals(""))
            person.setCity(null);
        if (person.getState().equals(""))
            person.setState(null);
        if (person.getZip().equals(""))
            person.setZip(null);
    }
}
