package com.patach.patachoux.Model;

public class Cart {
    String productName,ProductPrice,ProductQuantity,ProductImage,prodcutId;

    public Cart(String productName, String productPrice, String ProductQuant, String productImage,String prodcutId) {
        this.productName = productName;
        ProductPrice = productPrice;
        this.ProductQuantity = ProductQuant;
        ProductImage = productImage;
        this.prodcutId=prodcutId;
    }

    public String getProdcutId() {
        return prodcutId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }

    public String getProductImage() {
        return ProductImage;
    }
}
