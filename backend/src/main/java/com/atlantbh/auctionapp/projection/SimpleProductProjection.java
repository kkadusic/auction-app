package com.atlantbh.auctionapp.projection;

import java.math.BigDecimal;

public interface SimpleProductProjection {

    Long getId();

    String getName();

    BigDecimal getStartPrice();

    String getDescription();

    String getImageUrl();

    String getCategoryName();

    String getSubcategoryName();
}
