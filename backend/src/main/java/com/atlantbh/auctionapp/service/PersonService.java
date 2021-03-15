package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.exception.BadGatewayException;
import com.atlantbh.auctionapp.exception.BadRequestException;
import com.atlantbh.auctionapp.exception.ConflictException;
import com.atlantbh.auctionapp.exception.UnauthorizedException;
import com.atlantbh.auctionapp.model.Person;

import com.atlantbh.auctionapp.model.Token;
import com.atlantbh.auctionapp.repository.PersonRepository;

import com.atlantbh.auctionapp.repository.TokenRepository;
import com.atlantbh.auctionapp.request.ForgotPasswordRequest;
import com.atlantbh.auctionapp.request.LoginRequest;
import com.atlantbh.auctionapp.request.RegisterRequest;

import com.atlantbh.auctionapp.request.ResetPasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Optional;
import java.util.UUID;

import static com.atlantbh.auctionapp.utilities.ResourceUtil.getResourceFileAsString;


@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    private String hostUrl;

    @Value("${app.hostUrl}")
    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository, EmailService emailService) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
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
        System.out.println("tu sam");
        try {
            System.out.println("tu sam2");
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
        System.out.println("OK, reset");
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
}
