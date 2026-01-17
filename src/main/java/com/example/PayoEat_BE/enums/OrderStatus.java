package com.example.PayoEat_BE.enums;

public enum OrderStatus {
    /** Order received, customer has just placed an order */
    RECEIVED,

    /** Order cancelled by customer or restaurant */
    CANCELLED,

    /** Awaiting payment from customer */
    PAYMENT,

    /** Customer has paid and restaurant has confirmed */
    CONFIRMED,

    /** Customer has arrived and scanned order barcode (dining) */
    ACTIVE,

    /** Food is ready to be served or picked up */
    READY,

    /** Order completed */
    FINISHED
}