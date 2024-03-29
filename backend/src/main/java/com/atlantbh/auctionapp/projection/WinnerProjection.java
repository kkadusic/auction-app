package com.atlantbh.auctionapp.projection;

import java.math.BigDecimal;

public interface WinnerProjection {

    Long getId();

    Long getProductId();

    String getProductName();

    BigDecimal getMaxBid();

    String getEmail();

    Boolean getEmailNotify();

    Boolean getPushNotify();
}
