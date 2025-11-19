package com.project.airBnb.pricing_Strategies.impl;

import com.project.airBnb.entity.Inventory;
import com.project.airBnb.pricing_Strategies.PricingStrategy;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy {

    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price=wrapped.calculatePrice(inventory);
        boolean isHoliday=true; //TODO:call an API or check local date
        if (isHoliday){
            price=price.multiply(BigDecimal.valueOf(1.25));
        }
        return price;
    }
}
