package com.maveric.userservice.controller;

import com.maveric.userservice.dto.UserDTO;
import com.maveric.userservice.entity.UserEntity;
import com.maveric.userservice.service.IUserService;
import com.maveric.userservice.utils.UserServiceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1")
public class UserController {


    @Autowired
    private IUserService iUserService;

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDTO useDto){
        iUserService.saveUserDetails(useDto);
        if(useDto.getId() > 0){
      return   ResponseEntity.status(HttpStatus.CREATED).body(UserServiceConstant.user_created_successfully);
        }else {
            return   ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserServiceConstant.user_not_created);
        }

    }
    @GetMapping("/users/{userId}")
    public ResponseEntity  getUser(@PathVariable String userId){
        UserDTO userDTO = iUserService.getUserDetailsById(Integer.valueOf(userId));
        if(userDTO.getId() >0) {
            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
        }else {
            return new ResponseEntity( HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/users/{userId}")
    public  ResponseEntity updateUser(@PathVariable String userId,@RequestBody @Valid UserDTO userDTO){
        userDTO.setId(Integer.valueOf(userId));
        UserDTO userDTOResponse =iUserService.updateUser(userDTO);
        if(userDTOResponse.getId() >0) {
            return new ResponseEntity<UserDTO>(userDTOResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/users/{userId}")
    public  ResponseEntity deleteUser(@PathVariable String userId){
        UserDTO userDTOResponse = iUserService.deleteUser(Integer.valueOf(userId));
        return  new ResponseEntity<String>(UserServiceConstant.user_successfully_deleted,HttpStatus.OK);
    }

    @GetMapping("/users/getUsersByEmail/{email}")
    public  ResponseEntity getUserByEmail(@PathVariable String email){
       List<UserDTO> usersDTO = iUserService.getUserByEmail(email);
       return  new ResponseEntity(usersDTO,HttpStatus.OK);
    }
    @GetMapping("/users")
    public  ResponseEntity getUsers(Pageable pageable){
     Page<UserEntity> response= iUserService.getUsers(pageable);
     return  new ResponseEntity(response,HttpStatus.OK);
    }
}
