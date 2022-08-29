package com.maveric.userservice.service;


import com.maveric.userservice.dto.UserDTO;
import com.maveric.userservice.entity.UserEntity;
import com.maveric.userservice.repository.IUserRepository;
import com.maveric.userservice.utils.Utills;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class UserService implements IUserService{

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    @Transactional
    public UserDTO saveUserDetails(UserDTO user) {
        try {
        UserEntity userEntity=new UserEntity();
            userEntity.setDateOfBirth(Utills.convertDateUtilToSql(user.getDateOfBirth()));
        BeanUtils.copyProperties(user,userEntity);
            userEntity.setCreatedAt(Utills.getCurrentDate());
        iUserRepository.save(userEntity);
        user.setId(userEntity.getId());
        return  user;
        }catch (Exception e){

        }
        return user;
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

        }
        return null;
    }

    @Override
    public List<UserDTO> getUserByEmail(String email) {
        try {
            Optional<List<UserEntity>> userEntity = iUserRepository.findByemail(email);
            if (userEntity.isPresent()) {
                List<UserDTO> users = userEntity.get().stream().map(entity -> mapUserEntityToUserDTO(entity)).collect(Collectors.toList());
                return users;
            }
        }catch (Exception e){

        }
        return null;
    }

    @Override
    public Page<UserEntity> getUsers(Pageable pageable) {
        Page<UserEntity> results= null;
        try {
             results =  iUserRepository.findAll(pageable);
        }catch (Exception e){

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
        userEntity.setRole(userDTO.getRole());
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
        userDTO.setRole(userEntity.getRole());
        return  userDTO;
    }
}
