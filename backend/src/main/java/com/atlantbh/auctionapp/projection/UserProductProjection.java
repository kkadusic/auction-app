package com.atlantbh.auctionapp.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface UserProductProjection {

    Long getId();

    String getName();

    String getUrl();

    BigDecimal getPrice();

    String getCategoryName();

    String getSubcategoryName();

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    Integer getBidCount();

    BigDecimal getMaxBid();

    Long getPersonId();
}
