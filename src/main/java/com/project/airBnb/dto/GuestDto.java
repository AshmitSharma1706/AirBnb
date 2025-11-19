package com.project.airBnb.dto;

import com.project.airBnb.entity.User;
import com.project.airBnb.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestDto {

    private Long id;

    private User user;

    private String name;

    private Gender gender;

    private Integer age;
}
