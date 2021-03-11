package com.atlantbh.auctionapp.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface FullProductResponse {

    Long getId();

    Long getPersonId();

    String getName();

    String getDescription();

    BigDecimal getStartPrice();

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    Boolean getWished();

    Long getImageId();

    String getImageUrl();

    Boolean getImageFeatured();
}
