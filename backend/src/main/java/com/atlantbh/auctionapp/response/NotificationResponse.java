package com.atlantbh.auctionapp.response;

public class NotificationResponse {

    private Long id;
    private String type;
    private String description;
    private Long productId;
    private String name;
    private String url;
    private Boolean checked;

    public NotificationResponse() {
    }

    public NotificationResponse(Long id, String type, String description, Long productId, String name, String url, Boolean checked) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.productId = productId;
        this.name = name;
        this.url = url;
        this.checked = checked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
