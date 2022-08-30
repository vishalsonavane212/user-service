package com.maveric.userservice.controller;

import com.maveric.userservice.dto.UserDTO;
import com.maveric.userservice.entity.UserEntity;
import com.maveric.userservice.service.UserService;
import com.maveric.userservice.service.UserServiceImpl;
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
@RequestMapping("api/v1/users")
public class UserController {


    @Autowired
    private UserService iUserService;

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDTO userDto){
       ResponseEntity  response = iUserService.saveUserDetails(userDto);
       return  new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getStatusCode().value()));
    }
    @GetMapping("/{userId}")
    public ResponseEntity  getUser(@PathVariable String userId){
        UserDTO userDTO = iUserService.getUserDetailsById(Integer.valueOf(userId));
        if(userDTO.getId() >0) {
            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
        }else {
            return new ResponseEntity( HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{userId}")
    public  ResponseEntity updateUser(@PathVariable String userId,@RequestBody @Valid UserDTO userDTO){
        userDTO.setId(Integer.valueOf(userId));
        UserDTO userDTOResponse =iUserService.updateUser(userDTO);
        if(userDTOResponse.getId() >0) {
            return new ResponseEntity<UserDTO>(userDTOResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{userId}")
    public  ResponseEntity deleteUser(@PathVariable String userId){
        UserDTO userDTOResponse = iUserService.deleteUser(Integer.valueOf(userId));
        return  new ResponseEntity<String>(UserServiceConstant.user_successfully_deleted,HttpStatus.OK);
    }

    @GetMapping("/getUsersByEmail/{email}")
    public  ResponseEntity getUserByEmail(@PathVariable String email){
        return iUserService.getUserByEmail(email);
    }
    @GetMapping()
    public  ResponseEntity getUsers(Pageable pageable){
     Page<UserEntity> response= iUserService.getUsers(pageable);
     return  new ResponseEntity(response,HttpStatus.OK);
    }
}
