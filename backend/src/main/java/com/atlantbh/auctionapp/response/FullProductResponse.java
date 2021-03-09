package com.atlantbh.auctionapp.response;

import java.time.LocalDateTime;

public interface FullProductResponse {

    Long getId();

    String getName();

    String getDescription();

    Integer getStartPrice();

    LocalDateTime getEndDate();

    Boolean getWished();

    Long getImageId();

    String getImageUrl();

    Boolean getImageFeatured();
}
