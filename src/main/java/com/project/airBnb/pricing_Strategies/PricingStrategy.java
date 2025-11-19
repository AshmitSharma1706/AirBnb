package com.project.airBnb.pricing_Strategies;

import com.project.airBnb.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}
