package com.patach.patachoux.Model;

public class Product {
    String productId,productName,productPrice,productPic,productDescription;


    public Product(String productId, String productName, String productPrice, String productPic, String productDescription) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productPic = productPic;
        this.productDescription = productDescription;
    }

    public Product(String productId, String productName, String productPic, String productDescription) {
        this.productId = productId;
        this.productName = productName;
        this.productPic = productPic;
        this.productDescription = productDescription;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductPic() {
        return productPic;
    }

    public String getProductDescription() {
        return productDescription;
    }
}
