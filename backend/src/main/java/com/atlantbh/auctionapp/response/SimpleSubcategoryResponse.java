package com.atlantbh.auctionapp.response;

import java.math.BigDecimal;

public interface SimpleSubcategoryResponse {
    Long getId();

    String getName();

    String getCategoryName();

    String getUrl();

    BigDecimal getStartPrice();
}
