package com.maveric.userservice.controller;

import com.maveric.userservice.dto.UserDTO;
import com.maveric.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/v1/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService iUserService;


    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDTO userDto){
       ResponseEntity  response = iUserService.saveUserDetails(userDto);
       return  new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getStatusCode().value()));
    }
    @GetMapping("/{userId}")
    public ResponseEntity  getUser(@PathVariable(required = true) String userId){
        ResponseEntity response = iUserService.getUserDetailsById(Integer.valueOf(userId));
        return  new ResponseEntity(response.getBody(),response.getStatusCode());
    }

    @PutMapping("/{userId}")
    public  ResponseEntity updateUser(@PathVariable(required = true) String userId,@RequestBody @Valid UserDTO userDTO){
        userDTO.setId(Integer.valueOf(userId));
        ResponseEntity userDTOResponse =iUserService.updateUser(userDTO);
        return  new ResponseEntity(userDTOResponse.getBody(),userDTOResponse.getStatusCode());
    }

    @DeleteMapping("/{userId}")
    public  ResponseEntity deleteUser(@PathVariable String userId){
        ResponseEntity userDTOResponse = iUserService.deleteUser(Integer.valueOf(userId));
        return  new ResponseEntity(userDTOResponse.getBody(),userDTOResponse.getStatusCode());
    }

    @GetMapping("/getUsersByEmail/{email}")
    public  ResponseEntity getUserByEmail(@PathVariable String email){
        return iUserService.getUserByEmail(email);
    }
    @GetMapping()
    public  ResponseEntity getUsers(Pageable pageable){
     ResponseEntity response= iUserService.getUsers(pageable);
     return  new ResponseEntity(response.getBody(),response.getStatusCode());
    }

    @GetMapping(path = "/authenticate/{email}/{password}",produces = "application/json")
    public ResponseEntity getAuthenticate(@PathVariable(required = true) String email,@PathVariable(required = true) String password){
         return   iUserService.getUserByEmailAndPassword(email,password);
    }
}
