package com.atlantbh.auctionapp.projection;

import java.math.BigDecimal;

public interface BidderProjection {

    Long getId();

    String getEmail();

    Boolean getEmailNotify();

    Boolean getPushNotify();

    BigDecimal getMaxBid();
}
