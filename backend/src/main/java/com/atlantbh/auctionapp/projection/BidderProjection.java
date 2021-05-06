package com.atlantbh.auctionapp.projection;

import java.math.BigDecimal;

public interface BidderProjection {

    String getEmail();

    Boolean getEmailNotify();

    Boolean getPushNotify();

    BigDecimal getMaxBid();
}
