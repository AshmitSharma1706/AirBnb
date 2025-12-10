package com.project.airBnb.service;

import com.project.airBnb.entity.Booking;

public interface CheckoutService {
    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);
}
