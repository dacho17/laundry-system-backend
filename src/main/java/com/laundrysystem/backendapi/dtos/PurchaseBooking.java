package com.laundrysystem.backendapi.dtos;

import com.laundrysystem.backendapi.entities.Booking;

public class PurchaseBooking {
    private Booking booking;
    private boolean isAlreadyCreated;

    public PurchaseBooking() {};

    public PurchaseBooking(Booking booking, boolean isAlreadyCreated) {
        super();
        this.booking = booking;
        this.isAlreadyCreated = isAlreadyCreated;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public boolean getIsAlreadyCreated() {
        return isAlreadyCreated;
    }

    public void setIsAlreadyCreated(boolean isAlreadyCreated) {
        this.isAlreadyCreated = isAlreadyCreated;
    }

    @Override
    public String toString() {
        return "PurchaseBooking [booking=" + booking + ", isAlreadyCreated=" + isAlreadyCreated + "]"; 
    }
}