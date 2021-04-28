package com.atlantbh.auctionapp.projection;

import com.atlantbh.auctionapp.enumeration.Color;
import com.atlantbh.auctionapp.enumeration.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ReceiptProjection {
    String getId();

    LocalDateTime getDate();

    BigDecimal getAmount();

    String getStreet();

    String getCountry();

    String getCity();

    String getZip();

    String getPhone();

    String getSellerName();

    String getSellerStreet();

    String getSellerCountry();

    String getSellerCity();

    String getSellerZip();

    String getSellerPhone();

    String getName();

    String getDescription();

    Boolean getShipping();

    Color getColor();

    Size getSize();
}
