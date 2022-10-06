package com.maveric.userservice.service;

import com.maveric.userservice.dto.UserDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface UserService {

    public ResponseEntity saveUserDetails(UserDTO user);

    public ResponseEntity getUserDetailsById(Integer userId);

    public ResponseEntity updateUser(UserDTO userDTO);

    public ResponseEntity deleteUser(Integer userId);

    public ResponseEntity getUserByEmail(String email);

    public ResponseEntity getUsers(Pageable pageable);

    public ResponseEntity getUserByEmailAndPassword(String email,String password);
}
