package com.project.airBnb.pricing_Strategies.impl;

import com.project.airBnb.entity.Inventory;
import com.project.airBnb.pricing_Strategies.PricingStrategy;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class OccupancyPricingStrategy implements PricingStrategy {

    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price=wrapped.calculatePrice(inventory);
        double occupancyRate=(double) inventory.getBookedCount()/inventory.getTotalCount();
        if (occupancyRate > 0.8){
            price=price.multiply(BigDecimal.valueOf(1.2));
        }
        return price;
    }
}
