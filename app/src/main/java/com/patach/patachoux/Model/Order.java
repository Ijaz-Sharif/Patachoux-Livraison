package com.patach.patachoux.Model;

public class Order {
    String orderId,date,status,suplierName,userName,userAddress,userNumber,userId;

    public Order(String orderId, String date,String userid) {

        this.orderId = orderId;
        this.date = date;
        this.userId=userid;
    }
    public Order(String orderId, String date,String status,String suplierName,String userName,String userAddress,String userNumber,String userId) {

        this.orderId = orderId;
        this.date = date;
        this.status=status;
        this.suplierName=suplierName;
        this.userAddress=userAddress;
        this.userNumber=userNumber;
        this.userName=userName;
        this.userId=userId;
    }



    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getSuplierName() {
        return suplierName;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getOrderId() {
        return orderId;
    }
}
