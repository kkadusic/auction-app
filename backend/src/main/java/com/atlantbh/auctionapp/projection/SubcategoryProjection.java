package com.atlantbh.auctionapp.projection;

import java.math.BigDecimal;

public interface SubcategoryProjection {

    Long getId();

    String getName();

    String getCategoryName();

    String getUrl();

    BigDecimal getStartPrice();
}
