package com.project.airBnb.pricing_Strategies.impl;

import com.project.airBnb.entity.Inventory;
import com.project.airBnb.pricing_Strategies.PricingStrategy;

import java.math.BigDecimal;


public class BasePricingStrategy implements PricingStrategy {
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }
}
