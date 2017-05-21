package com.codecool.shop.model;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class OrderList {
    private static final Logger logger = LoggerFactory.getLogger(OrderList.class);
    public enum Status {newOrder, inCart, inCheckOut, payed}
    private Integer nextId = 1;
    private Integer inCartId;

    private Status status = Status.newOrder;
    private HashMap <Integer, Product> inCart = new HashMap();

    public void OrdeList(){
        this.status = Status.newOrder;
    }

    public void Orderlist(HashMap inCart){
        this.status = Status.newOrder;
        this.inCart = inCart;

    }

    public void orderProcess(int productId){
        switch (getStatus()){
            case newOrder:
                addProductToCart(productId);
                logger.info("Added a new item to shopping cart.");
                break;
            case inCart:
                //doublecheck products, change quantity, delete, etc...
                break;
            case inCheckOut:
                //payment method
                break;
            case payed:
                //sending email about purchased products
                break;

            default:
                break;
        }
    }

    private void addProductToCart (int productId) {
        inCartId = nextId;
        nextId++;
        if (productId != 0){
            ProductDao productDataStore = ProductDaoMem.getInstance();
            inCart.put(inCartId, productDataStore.find(productId));
            setStatus(Status.newOrder);
        }
    }

    public HashMap<Integer, Product> getInCart() {
        return inCart;
    }
    public Status getStatus() {
        return status;
    }
    private void setStatus(Status status) {
        this.status = status;
    }
}

