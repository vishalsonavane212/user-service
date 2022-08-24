package com.maveric.userservice.controller;

import com.maveric.userservice.dto.UserDTO;
import com.maveric.userservice.entity.UserEntity;
import com.maveric.userservice.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1")
public class userController {

    @Autowired
    private IUserService iUserService;

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDTO useDto){
        iUserService.saveUserDetails(useDto);
        if(useDto.getId() > 0){
      return   ResponseEntity.status(HttpStatus.OK).body("success");
        }else {
            return   ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data not saved");
        }

    }
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDTO>  getUser(@PathVariable String userId){
        UserDTO userDTO = iUserService.getUserDetailsById(Integer.valueOf(userId));
        return  new ResponseEntity<UserDTO>(userDTO,HttpStatus.OK);
    }

    @PutMapping("/users/{userId}")
    public  ResponseEntity updateUser(@PathVariable String userId,@RequestBody UserDTO userDTO){
        userDTO.setId(Integer.valueOf(userId));
        UserDTO userDTOResponse =iUserService.updateUser(userDTO);
        return  new ResponseEntity<UserDTO>(userDTOResponse,HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public  ResponseEntity deleteUser(@PathVariable String userId){
        UserDTO userDTOResponse = iUserService.deleteUser(Integer.valueOf(userId));
        return  new ResponseEntity<String>("User successfully deleted",HttpStatus.OK);
    }

    @GetMapping("/users/getUsersByEmail/{email}")
    public  ResponseEntity getUserByEmail(@PathVariable String email){
       List<UserDTO> usersDTO = iUserService.getUserByEmail(email);
       return  new ResponseEntity(usersDTO,HttpStatus.OK);
    }
}
