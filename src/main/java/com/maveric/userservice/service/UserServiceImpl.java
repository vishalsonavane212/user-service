package com.maveric.userservice.service;


import com.maveric.userservice.dto.UserDTO;
import com.maveric.userservice.entity.UserEntity;
import com.maveric.userservice.exception.CreateUserException;
import com.maveric.userservice.exception.GetUserByIdException;
import com.maveric.userservice.exception.UserException;
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
    public ResponseEntity saveUserDetails(UserDTO user)  {
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
                   return  new ResponseEntity(UserServiceConstant.USER_CREATED_SUCCESSFULLY,HttpStatus.CREATED);
               } else {
                 return  new ResponseEntity(UserServiceConstant.USER_NOT_CREATED,HttpStatus.BAD_REQUEST);
               }
           }else {
               Map<String ,String> error=new HashMap<>();
               error.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
               error.put(UserServiceConstant.error_message,UserServiceConstant.EMAIL_IS_ALREADY_PRESENT);
               return new ResponseEntity(error,HttpStatus.BAD_REQUEST);
           }
        }catch (Exception e){
        throw new CreateUserException(e,UserServiceConstant.USER_NOT_CREATED);
        }
    }

    @Override
    public ResponseEntity getUserDetailsById(Integer userId) {
        UserDTO userDTO = new UserDTO();
        try {
            Optional<UserEntity> userEntity = iUserRepository.findById(userId);
            if (userEntity.isPresent()) {
                BeanUtils.copyProperties(userEntity.get(), userDTO);
                return new ResponseEntity(userDTO,HttpStatus.OK);
            }else {
                Map<String,String> error=new HashMap<>();
                error.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
                error.put(UserServiceConstant.error_message,UserServiceConstant.USER_IS_NOT_PRESENT_FOR_GIVEN_ID);
                return new ResponseEntity(error,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
         throw  new GetUserByIdException(e,e.getMessage());
        }
    }

    @Override
    public ResponseEntity updateUser(UserDTO userDTO) {
        try {
            UserEntity resultById = null;
            Optional<UserEntity> userEntity = iUserRepository.findById(userDTO.getId());
            if (userEntity.isPresent()) {
                Optional<UserEntity> emailIsPresent=iUserRepository.findByEmail(userDTO.getEmail());
                if(emailIsPresent.isPresent()){
                    Map<String,String> error=new HashMap<>();
                    error.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
                    error.put(UserServiceConstant.error_message,UserServiceConstant.EMAIL_IS_ALREADY_PRESENT);
                  return   new ResponseEntity(error,HttpStatus.BAD_REQUEST);
                }
                    resultById = userEntity.get();
                    resultById = mapUserDtoToUserEntity(userDTO, resultById);
                    resultById.setId(userDTO.getId());
                    UserEntity result = iUserRepository.save(resultById);

                    return new ResponseEntity(mapUserEntityToUserDTO(result), HttpStatus.OK);

            }else {
                Map<String,String> error=new HashMap<>();
                error.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
                error.put(UserServiceConstant.error_message,UserServiceConstant.USER_IS_NOT_PRESENT_FOR_GIVEN_ID);
                return new ResponseEntity(error,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
         throw new UserException(e,UserServiceConstant.USER_NOT_UPDATED);
        }
    }

    @Override
    public ResponseEntity deleteUser(Integer userId) {
        try {
            Optional<UserEntity> userEntity = iUserRepository.findById(userId);
            if (userEntity.isPresent()) {
                iUserRepository.deleteById(userId);
               return new ResponseEntity(UserServiceConstant.USER_DELETED_SUCCESSFULLY,HttpStatus.OK);
            }else {
                Map<String,String> error=new HashMap<>();
                error.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
                error.put(UserServiceConstant.error_message,UserServiceConstant.USER_IS_NOT_PRESENT_FOR_GIVEN_ID);
                return new  ResponseEntity(error,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
     throw  new UserException(e,UserServiceConstant.USER_NOT_DELETED);
        }
    }

    @Override
    public ResponseEntity getUserByEmail(String email) {
        try {
            Optional<UserEntity> userEntity = iUserRepository.findByEmail(email);
            if (userEntity.isPresent()) {
                UserDTO user = mapUserEntityToUserDTO(userEntity.get());
                //user.setPassword(userEntity.get().getPassword());
                  return new ResponseEntity(user,HttpStatus.OK);
            }else {
                Map<String,String> result=new HashMap<>();
                result.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
                result.put(UserServiceConstant.error_message,UserServiceConstant.EMAIL_NOT_PRESENT);
                  return new ResponseEntity(result,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
         throw  new UserException(e,e.getMessage());
        }
    }

    @Override
    public ResponseEntity getUsers(Pageable pageable) {
        Page<UserEntity> results= null;
        try {
             results =  iUserRepository.findAll(pageable);
          List<UserDTO> userDtos =  results.stream().map(entity -> this.mapUserEntityToUserDTO(entity)).collect(Collectors.toList());
            return new ResponseEntity(userDtos,HttpStatus.OK);
        }catch (Exception e){
        throw  new UserException(e,e.getMessage());
        }
    }

    @Override
    public ResponseEntity getUserByEmailAndPassword(String email, String password) {
        try {
            Optional<UserEntity> userEntity = iUserRepository.findByEmailAndPassword(email,password);
            if (userEntity.isPresent()) {
                UserDTO user = mapUserEntityToUserDTO(userEntity.get());
                user.setPassword(userEntity.get().getPassword());
                return new ResponseEntity(user,HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new UserException(e,e.getMessage());
        }
        return null;
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
