package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.exception.BadGatewayException;
import com.atlantbh.auctionapp.exception.UnprocessableException;
import com.atlantbh.auctionapp.model.Notification;
import com.atlantbh.auctionapp.model.Person;
import com.atlantbh.auctionapp.model.Product;
import com.atlantbh.auctionapp.projection.WinnerProjection;
import com.atlantbh.auctionapp.repository.ProductRepository;
import com.atlantbh.auctionapp.utilities.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;

import static com.atlantbh.auctionapp.utilities.ResourceUtil.getResourceFileAsString;

@Service
@EnableScheduling
public class ScheduleService {

    private final ProductRepository productRepository;
    private final PushService pushService;
    private final EmailService emailService;

    @Autowired
    public ScheduleService(ProductRepository productRepository, PushService pushService, EmailService emailService) {
        this.productRepository = productRepository;
        this.pushService = pushService;
        this.emailService = emailService;
    }

    private String hostUrl;

    @Value("${app.hostUrl}")
    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    @Scheduled(fixedRateString = "${app.scheduleRate}")
    public void notifyHighestBidders() {
        List<WinnerProjection> winners = productRepository.getNotNotifiedWinners();
        for (WinnerProjection winner : winners) {
            if (winner.getPushNotify()) {
                Notification notification = new Notification(
                        "success",
                        "Congratulations! You outbid the competition, your bid of $" + winner.getMaxBid() +
                                " is the highest bid.",
                        new Product(winner.getProductId(), winner.getProductName()),
                        new Person(winner.getId())
                );
                pushService.broadcastNotification(notification, winner.getId().toString());
            }
            if (winner.getEmailNotify()) {
                String body = formEmailBody(hostUrl, winner);
                try {
                    emailService.sendMail(winner.getEmail(), "Bid winner", body);
                } catch (MessagingException e) {
                    throw new BadGatewayException("Error sending an email");
                }
            }
            Product product = productRepository.findById(winner.getProductId())
                    .orElseThrow(() -> new UnprocessableException("Wrong product id"));
            product.setNotified(true);
            productRepository.save(product);
        }
    }

    private String formEmailBody(String hostUrl, WinnerProjection winner) {
        String body = getResourceFileAsString("static/auction_won.html");
        return body.replace("maxBid", winner.getMaxBid().toPlainString())
                .replace("productName", winner.getProductName())
                .replace("productId", winner.getProductId().toString())
                .replace("paymentUrl", StringUtil.getPaymentPageUrl(winner.getProductId().toString(), hostUrl))
                .replace("settingsUrl", hostUrl + "/my-account/settings");
    }
}
