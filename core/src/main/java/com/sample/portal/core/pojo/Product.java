package com.sample.portal.core.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private int id;
    private String title;
    private String description;
    private String thumbnail;
    private List<String> images;
    private String sku;
    private float price;
    private float discountPercentage;
    private String brand;
    private int minimumOrderQuantity;
    private String warrantyInformation;
    private String shippingInformation;
    private String availabilityStatus;
    private String returnPolicy;



    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscountedPrice() {
        float discount = (price * discountPercentage) / 100;
        float discountedPrice = price - discount;
        String formattedDiscountedPrice = String.format("%.2f", discountedPrice);
        return formattedDiscountedPrice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getMinimumOrderQuantity() {
        return minimumOrderQuantity;
    }

    public void setMinimumOrderQuantity(int minimumOrderQuantity) {
        this.minimumOrderQuantity = minimumOrderQuantity;
    }

    public String getWarrantyInformation() {
        return warrantyInformation;
    }

    public void setWarrantyInformation(String warrantyInformation) {
        this.warrantyInformation = warrantyInformation;
    }

    public String getShippingInformation() {
        return shippingInformation;
    }

    public void setShippingInformation(String shippingInformation) {
        this.shippingInformation = shippingInformation;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getReturnPolicy() {
        return returnPolicy;
    }

    public void setReturnPolicy(String returnPolicy) {
        this.returnPolicy = returnPolicy;
    }

}
