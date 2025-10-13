package com.example.PayoEat_BE.enums;

public enum OrderStatus {
    RECEIVED, CANCELLED, PAYMENT, CONFIRMED, DINING, FINISHED

    // Received: Order received, Customer has just placed an order
    // Cancelled: Order cancelled by customer or Restaurant cancel the order
    // Payment: Order is in payment process, customer needs to do payment
    // Confirmed: Customer has paid the order & restaurant confirm it.
    // Active/Dining: Customer has already come to the restaurant and scan the order barcode
    // Finished: Customer has finished eating & order is finished
}
    