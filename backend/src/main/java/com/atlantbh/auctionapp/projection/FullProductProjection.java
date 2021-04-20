package com.atlantbh.auctionapp.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface FullProductProjection {

    Long getId();

    Long getPersonId();

    String getName();

    String getDescription();

    BigDecimal getStartPrice();

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    Boolean getWished();
}
