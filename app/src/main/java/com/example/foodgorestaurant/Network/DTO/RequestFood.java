package com.example.foodgorestaurant.Network.DTO;

import java.math.BigDecimal;

public class RequestFood {
    private String dishName = "";
    private String description;
    private BigDecimal price;
    // Use String for image path/URL or Uri/File as needed
    private String imageUrl;
    private boolean isAvailable = true;

    // Getters and setters
    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}
