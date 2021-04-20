package com.atlantbh.auctionapp.projection;

import java.math.BigDecimal;

public interface SimpleSubcategoryProjection {

    Long getId();

    String getName();

    String getCategoryName();

    String getUrl();

    BigDecimal getStartPrice();
}
