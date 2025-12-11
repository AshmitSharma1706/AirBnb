package com.project.airBnb.service;

import com.project.airBnb.dto.ProfileUpdateRequestDto;
import com.project.airBnb.dto.UserDto;
import com.project.airBnb.entity.User;

public interface UserService {
    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();

}
