package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.exception.BadRequestException;
import com.atlantbh.auctionapp.exception.UnauthorizedException;
import com.atlantbh.auctionapp.exception.UnprocessableException;
import com.atlantbh.auctionapp.model.Bid;
import com.atlantbh.auctionapp.model.Notification;
import com.atlantbh.auctionapp.model.Person;
import com.atlantbh.auctionapp.model.Product;
import com.atlantbh.auctionapp.projection.SimpleBidProjection;
import com.atlantbh.auctionapp.repository.BidRepository;
import com.atlantbh.auctionapp.repository.PersonRepository;
import com.atlantbh.auctionapp.repository.ProductRepository;
import com.atlantbh.auctionapp.request.BidDeleteRequest;
import com.atlantbh.auctionapp.request.BidRequest;
import com.atlantbh.auctionapp.security.JwtTokenUtil;
import com.atlantbh.auctionapp.utilities.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.atlantbh.auctionapp.utilities.ResourceUtil.getResourceFileAsString;

@Service
public class BidService {

    private final BidRepository bidRepository;
    private final PersonRepository personRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;
    private final PushService pushService;

    private String hostUrl;

    @Value("${app.hostUrl}")
    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    @Autowired
    public BidService(BidRepository bidRepository, PersonRepository personRepository,
                      ProductRepository productRepository, EmailService emailService, PushService pushService) {
        this.bidRepository = bidRepository;
        this.personRepository = personRepository;
        this.productRepository = productRepository;
        this.emailService = emailService;
        this.pushService = pushService;
    }

    public List<SimpleBidProjection> getBidsForProduct(Long id) {
        if (!productRepository.existsByIdAndIsActive(id))
            throw new UnprocessableException("Wrong product id");
        return bidRepository.getBidsForProduct(id);
    }

    public void add(BidRequest bidRequest) {
        Product product = productRepository.findByIdAndIsActive(bidRequest.getProductId())
                .orElseThrow(() -> new UnprocessableException("Wrong product id"));
        if (product.getStartPrice().compareTo(bidRequest.getAmount()) > 0) {
            throw new BadRequestException("Price can't be lower than the product start price");
        }
        if (product.getStartDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Auction is yet to start for this product");
        }
        if (product.getEndDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Auction ended for this product");
        }
        Long personId = JwtTokenUtil.getRequestPersonId();
        if (personId == null) {
            throw new UnprocessableException("Invalid JWT signature");
        }
        Person person = personRepository.findById(personId).
                orElseThrow(() -> new UnauthorizedException("Wrong person id"));
        if (product.getPerson().getId().equals(person.getId())) {
            throw new BadRequestException("You can't bid on your own product");
        }
        BigDecimal maxBid = bidRepository.getMaxBidFromPersonForProduct(person.getId(), product.getId());
        if (maxBid != null && (maxBid.compareTo(bidRequest.getAmount()) >= 0)) {
            throw new BadRequestException("Price can't be lower than your previous bid of $" + maxBid);
        }
        notifyHighestBidder(product, person.getEmail(), bidRequest.getAmount());
        bidRepository.save(new Bid(bidRequest.getAmount(), person, product));
    }

    public void remove(BidDeleteRequest bidDeleteRequest) {
        Long personId = JwtTokenUtil.getRequestPersonId();
        if (bidDeleteRequest.getProductId() == null)
            throw new BadRequestException("A product id has to be supplied");
        List<Bid> bids = bidRepository.findAllByProductId(personId, bidDeleteRequest.getProductId());
        if (bids.size() == 0)
            throw new BadRequestException("No bids found for this product or the auction has finished");
        bidRepository.deleteAll(bids);
    }

    private void notifyHighestBidder(Product product, String email, BigDecimal price) {
        bidRepository.getHighestBidder(product.getId())
                .ifPresent(bidder -> new Thread(() -> {
                    if (email.equals(bidder.getEmail()) || bidder.getMaxBid().compareTo(price) >= 0)
                        return;
                    if (bidder.getPushNotify()) {
                        Notification notification = new Notification(
                                "warning",
                                "You lost your highest bid place! Your bid of $" + bidder.getMaxBid() +
                                        " isn't the highest bid anymore.",
                                product,
                                new Person(bidder.getId())
                        );
                        pushService.broadcastNotification(notification, bidder.getId().toString());
                    }
                    if (bidder.getEmailNotify()) {
                        String body = formEmailBody(hostUrl, product, bidder.getMaxBid().toPlainString());
                        try {
                            emailService.sendMail(bidder.getEmail(), "Lost the highest bid place", body);
                        } catch (MessagingException ignore) {
                        }
                    }
                }).start());
    }

    private String formEmailBody(String hostUrl, Product product, String maxBid) {
        String body = getResourceFileAsString("static/higher_bid.html");
        return body.replace("maxBid", maxBid)
                .replace("productName", product.getName())
                .replace("productId", product.getId().toString())
                .replace("productPageUrl", StringUtil.getProductPageUrl(product, hostUrl))
                .replace("settingsUrl", hostUrl + "/my-account/settings");
    }
}
