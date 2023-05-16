package com.laundrysystem.backendapi.helpers;

import com.laundrysystem.backendapi.exceptions.ApiRuntimeException;

public class LoyaltyProgramHelper {
    

    public static int convertCashPriceToLoyaltyPointsPrice(double price, String currency) {
        switch(currency) {
            case "SGD":
                return (int)Math.round(price * 3);
            case "USD":
                return (int)Math.round(price * 5);
            case "MYR":
                return (int)Math.round(price);
            default:
                throw new ApiRuntimeException("Uknown currency encountered while converting to loyalty points");
        }
    }
}
