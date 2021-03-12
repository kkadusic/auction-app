package com.atlantbh.auctionapp.response;

import java.math.BigDecimal;

public interface SimpleProductResponse {

    Long getId();

    String getName();

    BigDecimal getStartPrice();

    String getDescription();

    String getUrl();

    String getCategoryName();

    String getSubcategoryName();
}
