package com.project.airBnb.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ContactInfo {
    private String address;
    private String phoneNumber;
    private String email;
    private String location;
}
