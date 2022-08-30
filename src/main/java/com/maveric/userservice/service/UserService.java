package com.maveric.userservice.service;

import com.maveric.userservice.dto.UserDTO;
import com.maveric.userservice.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public ResponseEntity saveUserDetails(UserDTO user);

    public UserDTO getUserDetailsById(Integer userId);

    public UserDTO updateUser(UserDTO userDTO);

    public UserDTO deleteUser(Integer userId);

    public ResponseEntity getUserByEmail(String email);

    public Page<UserEntity> getUsers(Pageable pageable);
}
