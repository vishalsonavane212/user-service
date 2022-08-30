package com.maveric.userservice.service;


import com.maveric.userservice.dto.UserDTO;
import com.maveric.userservice.entity.UserEntity;
import com.maveric.userservice.exception.EmailIdIsAlreadyPresentException;
import com.maveric.userservice.exception.ErrorDetails;
import com.maveric.userservice.repository.IUserRepository;
import com.maveric.userservice.utils.UserServiceConstant;
import com.maveric.userservice.utils.Utills;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private IUserRepository iUserRepository;

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
               ErrorDetails errorDetails=new ErrorDetails();
               errorDetails.setMessage(UserServiceConstant.email_is_already_present);
               errorDetails.setStatus(HttpStatus.BAD_REQUEST);
               return new ResponseEntity(errorDetails,HttpStatus.BAD_REQUEST);
           }
        }catch (Exception e){
        throw new RuntimeException(e);
        }
    }

    @Override
    public UserDTO getUserDetailsById(Integer userId) {
        UserDTO userDTO = new UserDTO();
        try {
            Optional<UserEntity> userEntity = iUserRepository.findById(userId);
            if (userEntity.isPresent()) {
                BeanUtils.copyProperties(userEntity.get(), userDTO);
            }
        }catch (Exception e){

        }
        return userDTO;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        try {
            UserEntity resultById = null;
            Optional<UserEntity> userEntity = iUserRepository.findById(userDTO.getId());
            if (userEntity.isPresent()) {
                resultById = userEntity.get();
                // userDTO.setCreatedAt(Utills.convertSqlToUtilDate(resultById.getCreatedAt()));
                resultById = mapUserDtoToUserEntity(userDTO, resultById);
                resultById.setId(userDTO.getId());
                UserEntity result = iUserRepository.save(resultById);

                return mapUserEntityToUserDTO(result);
            }
        }catch (Exception e){
         throw new RuntimeException();
        }
        return  null;
    }

    @Override
    public UserDTO deleteUser(Integer userId) {
        try {
            Optional<UserEntity> userEntity = iUserRepository.findById(userId);
            if (userEntity.isPresent()) {
                iUserRepository.deleteById(userId);
                userEntity.get();
                return mapUserEntityToUserDTO(userEntity.get());
            }
        }catch (Exception e){
     throw  new RuntimeException();
        }
        return null;
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
                  response = new ResponseEntity(UserServiceConstant.email_not_present,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
         throw  new RuntimeException();
        }
        return  response;
    }

    @Override
    public Page<UserEntity> getUsers(Pageable pageable) {
        Page<UserEntity> results= null;
        try {
             results =  iUserRepository.findAll(pageable);
        }catch (Exception e){
        throw  new RuntimeException();
        }

        return results;
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
