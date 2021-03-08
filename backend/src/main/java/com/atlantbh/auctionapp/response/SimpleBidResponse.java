package com.atlantbh.auctionapp.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface SimpleBidResponse {

    Long getId();

    String getFirstName();

    String getLastName();

    String getImageUrl();

    LocalDateTime getDate();

    BigDecimal getAmount();
}
