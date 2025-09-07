package com.project.airBnb.dto;

import com.project.airBnb.entity.ContactInfo;
import lombok.Data;

@Data
public class HotelDto {
    private Long id;

    private String name;

    private String city;

    private ContactInfo contactInfo;

    private String[] photos;

    private String[] amenities;

    private Boolean active;
}
