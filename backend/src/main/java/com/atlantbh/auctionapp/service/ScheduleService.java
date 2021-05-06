package com.atlantbh.auctionapp.service;

import com.atlantbh.auctionapp.exception.BadGatewayException;
import com.atlantbh.auctionapp.exception.UnprocessableException;
import com.atlantbh.auctionapp.model.Product;
import com.atlantbh.auctionapp.projection.WinnerProjection;
import com.atlantbh.auctionapp.repository.ProductRepository;
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
    private final EmailService emailService;

    @Autowired
    public ScheduleService(ProductRepository productRepository, EmailService emailService) {
        this.productRepository = productRepository;
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
            try {
                if (winner.getEmailNotify()) {
                    String body = formEmailBody(hostUrl, winner);
                    emailService.sendMail(winner.getEmail(), "Bid winner", body);
                }
                Product product = productRepository.findById(winner.getProductId())
                        .orElseThrow(() -> new UnprocessableException("Wrong product id"));
                product.setNotified(true);
                productRepository.save(product);
            } catch (MessagingException e) {
                throw new BadGatewayException("Error sending an email");
            }
        }
    }

    private String formEmailBody(String hostUrl, WinnerProjection winner) {
        String body = getResourceFileAsString("static/auction_won.html");
        return body.replace("maxBid", winner.getMaxBid().toPlainString())
                .replace("productName", winner.getProductName())
                .replace("productId", winner.getProductId().toString())
                .replace("paymentUrl", hostUrl + "/my-account/bids")
                .replace("settingsUrl", hostUrl + "/my-account/settings");
    }
}
