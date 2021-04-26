package com.atlantbh.auctionapp.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface SimpleBidProjection {

    Long getId();

    String getFirstName();

    String getLastName();

    String getImageUrl();

    LocalDateTime getDate();

    BigDecimal getAmount();

    Long getPersonId();
}
