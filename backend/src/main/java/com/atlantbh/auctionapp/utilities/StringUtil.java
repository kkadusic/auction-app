package com.atlantbh.auctionapp.utilities;

import com.atlantbh.auctionapp.model.Product;

public class StringUtil {
    public static String getProductPageUrl(Product product, String urlPrefix) {
        return urlPrefix + "/shop/" +
                removeSpaces(product.getSubcategory().getCategory().getName()) + "/" +
                removeSpaces(product.getSubcategory().getName())
                + "/" + product.getId();
    }

    public static String removeSpaces(String name) {
        return name.replaceAll(" ", "_").toLowerCase();
    }

    public static String getPaymentPageUrl(String id, String urlPrefix) {
        return urlPrefix + "/my-account/bids?id=" + id;
    }

    public static String getNotificationUrl(String type, Product product) {
        switch (type) {
            case "success":
                return StringUtil.getPaymentPageUrl(product.getId().toString(), "");
            case "warning":
                return StringUtil.getProductPageUrl(product, "");
            default:
                return "";
        }
    }
}
