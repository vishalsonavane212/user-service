package com.maveric.userservice.service;


import com.maveric.userservice.dto.UserDTO;
import com.maveric.userservice.entity.UserEntity;
import com.maveric.userservice.exception.ErrorDetails;
import com.maveric.userservice.repository.UserRepository;
import com.maveric.userservice.utils.UserServiceConstant;
import com.maveric.userservice.utils.Utills;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository iUserRepository;

    @Override
    @Transactional
    public ResponseEntity saveUserDetails(UserDTO user)  {
        ResponseEntity response = new ResponseEntity( UserServiceConstant.email_is_already_present,HttpStatus.BAD_REQUEST);
        try {
           Optional<UserEntity> entity = iUserRepository.findByEmail(user.getEmail());
           if(!entity.isPresent()) {
               user.setPassword(Utills.cryptWithMD5(user.getPassword()));
               UserEntity userEntity = new UserEntity();
               userEntity.setDateOfBirth(Utills.convertDateUtilToSql(user.getDateOfBirth()));
               BeanUtils.copyProperties(user, userEntity);
               userEntity.setCreatedAt(Utills.getCurrentDate());
               iUserRepository.save(userEntity);
               user.setId(userEntity.getId());
               if (userEntity.getId() != null) {
                   return  response =new ResponseEntity(UserServiceConstant.user_created_successfully,HttpStatus.CREATED);
               } else {
                 return response = new ResponseEntity(UserServiceConstant.user_not_created,HttpStatus.BAD_REQUEST);
               }
           }else {
               Map<String ,String> error=new HashMap<String ,String>();
               error.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
               error.put(UserServiceConstant.error_message,UserServiceConstant.email_is_already_present);
               return new ResponseEntity(error,HttpStatus.BAD_REQUEST);
           }
        }catch (Exception e){
        throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity getUserDetailsById(Integer userId) {
        ResponseEntity response= new ResponseEntity(HttpStatus.OK);
        UserDTO userDTO = new UserDTO();
        try {
            Optional<UserEntity> userEntity = iUserRepository.findById(userId);
            if (userEntity.isPresent()) {
                BeanUtils.copyProperties(userEntity.get(), userDTO);
                response= new ResponseEntity(userDTO,HttpStatus.OK);
            }else {
                Map<String,String> error=new HashMap<String,String>();
                error.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
                error.put(UserServiceConstant.error_message,UserServiceConstant.user_is_not_present_given_id);
                response = new ResponseEntity(error,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
         throw  new RuntimeException(e);
        }
        return response;
    }

    @Override
    public ResponseEntity updateUser(UserDTO userDTO) {
        ResponseEntity response=new ResponseEntity(HttpStatus.OK);
        try {
            UserEntity resultById = null;
            Optional<UserEntity> userEntity = iUserRepository.findById(userDTO.getId());
            if (userEntity.isPresent()) {
                Optional<UserEntity> emailIsPresent=iUserRepository.findByEmail(userDTO.getEmail());
                if(emailIsPresent.isPresent()){
                    Map<String,String> error=new HashMap<String,String>();
                    error.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
                    error.put(UserServiceConstant.error_message,UserServiceConstant.email_is_already_present);
                  return   response =new ResponseEntity(error,HttpStatus.BAD_REQUEST);
                }
                    resultById = userEntity.get();
                    resultById = mapUserDtoToUserEntity(userDTO, resultById);
                    resultById.setId(userDTO.getId());
                    UserEntity result = iUserRepository.save(resultById);

                    response = new ResponseEntity(mapUserEntityToUserDTO(result), HttpStatus.OK);

            }else {
                Map<String,String> error=new HashMap<String,String>();
                error.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
                error.put(UserServiceConstant.error_message,UserServiceConstant.user_is_not_present_given_id);
                response =new ResponseEntity(error,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
         throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    public ResponseEntity deleteUser(Integer userId) {
        ResponseEntity response=new ResponseEntity(HttpStatus.OK);
        try {
            Optional<UserEntity> userEntity = iUserRepository.findById(userId);
            if (userEntity.isPresent()) {
                iUserRepository.deleteById(userId);
                response =new ResponseEntity(UserServiceConstant.user_successfully_deleted,HttpStatus.OK);
            }else {
                Map<String,String> error=new HashMap<String,String>();
                error.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
                error.put(UserServiceConstant.error_message,UserServiceConstant.user_is_not_present_given_id);
                response =new ResponseEntity(error,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
     throw  new RuntimeException(e);
        }
        return response;
    }

    @Override
    public ResponseEntity getUserByEmail(String email) {
        ResponseEntity response = new ResponseEntity(HttpStatus.BAD_REQUEST);
        try {
            Optional<UserEntity> userEntity = iUserRepository.findByEmail(email);
            if (userEntity.isPresent()) {
                UserDTO user = mapUserEntityToUserDTO(userEntity.get());
                  response = new ResponseEntity(user,HttpStatus.OK);
            }else {
                Map<String,String> result=new HashMap<String,String>();
                result.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
                result.put(UserServiceConstant.error_message,UserServiceConstant.email_not_present);
                  response = new ResponseEntity(result,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
         throw  new RuntimeException();
        }
        return  response;
    }

    @Override
    public ResponseEntity getUsers(Pageable pageable) {
        ResponseEntity response=new ResponseEntity(HttpStatus.OK);
        Page<UserEntity> results= null;
        try {
             results =  iUserRepository.findAll(pageable);
          List<UserDTO> userDtos =  results.stream().map(entity ->mapUserEntityToUserDTO(entity)).collect(Collectors.toList());
           response =new ResponseEntity(userDtos,HttpStatus.OK);
        }catch (Exception e){
        throw  new RuntimeException(e);
        }

        return response;
    }

    private UserEntity mapUserDtoToUserEntity(UserDTO userDTO,UserEntity userEntity){

        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setMiddleName(userDTO.getMiddleName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setAddress(userDTO.getAddress());
        userEntity.setPhoneNumber(userDTO.getPhoneNumber());
        userEntity.setUpdatedAt(Utills.getCurrentDate());
        userEntity.setDateOfBirth(Utills.convertDateUtilToSql(userDTO.getDateOfBirth()));
        userEntity.setGender(userDTO.getGender());
        return  userEntity;
      }
    private  UserDTO  mapUserEntityToUserDTO(UserEntity userEntity){
        UserDTO userDTO=new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setMiddleName(userEntity.getMiddleName());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setAddress(userEntity.getAddress());
        userDTO.setPhoneNumber(userEntity.getPhoneNumber());
        userDTO.setUpdatedAt(userEntity.getUpdatedAt());
        userDTO.setCreatedAt(userEntity.getCreatedAt());
        userDTO.setDateOfBirth(userEntity.getDateOfBirth());
        userDTO.setGender(userEntity.getGender());
        return  userDTO;
    }
}
