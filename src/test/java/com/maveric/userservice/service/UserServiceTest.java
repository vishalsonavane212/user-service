package com.maveric.userservice.service;

import com.maveric.userservice.dto.UserDTO;
import com.maveric.userservice.entity.UserEntity;
import com.maveric.userservice.exception.CreateUserException;
import com.maveric.userservice.exception.GetUserByIdException;
import com.maveric.userservice.exception.UserException;
import com.maveric.userservice.repository.UserRepository;
import com.maveric.userservice.utils.UserServiceConstant;
import com.maveric.userservice.utils.Utills;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepository iUserRepository;
    @InjectMocks
    private UserService iUserService = new UserServiceImpl();
    private  UserDTO useDto;
    private UserEntity userEntity;
    @BeforeEach
    public void setup(){
        userEntity=new UserEntity();
        userEntity.setId(1);
        userEntity.setFirstName("ABCD");
        userEntity.setMiddleName("abcd");
        userEntity.setLastName("XYZo");
        userEntity.setAddress("pune");
        userEntity.setEmail("vikas@gmail.com");
        userEntity.setPhoneNumber("9404074081");
        userEntity.setPassword("1234");
        userEntity.setRole("admin");
    }

    public UserDTO prepaireUserDto()  {
         useDto=new UserDTO();
        useDto.setId(1);
        useDto.setFirstName("ABCD");
        useDto.setMiddleName("abcd");
        useDto.setLastName("XYZo");
        useDto.setAddress("pune");
        useDto.setEmail("vikas@gmail.com");
        useDto.setPhoneNumber("9404074081");
        useDto.setPassword("1234");
        useDto.setRole("admin");
        java.util.Date utilDate = new java.util.Date(Utills.getCurrentDate().getTime());
        String testBithDate="2022-08-23";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // converting the util date into string format
        final String stringDate = dateFormat.format(utilDate);
        Date date = null;
        try {
            date = dateFormat.parse(testBithDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        useDto.setDateOfBirth(date);
        useDto.setCreatedAt(Utills.getCurrentDate());
        useDto.setUpdatedAt(Utills.getCurrentDate());
        return  useDto;
    }

    public UserDTO prepaireUserDtoForThrowException()  {
        useDto=new UserDTO();
        useDto.setId(1);
        useDto.setFirstName("ABCD");
        useDto.setMiddleName("abcd");
        useDto.setAddress("pune");
        useDto.setEmail("vikas@gmail.com");
        useDto.setPhoneNumber("9404074081");
        useDto.setPassword("1234");
        useDto.setRole("admin");
        java.util.Date utilDate = new java.util.Date(Utills.getCurrentDate().getTime());
        String testBithDate="2022-08-23";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // converting the util date into string format
        final String stringDate = dateFormat.format(utilDate);
        Date date = null;
        try {
            date = dateFormat.parse(testBithDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        useDto.setDateOfBirth(date);
        useDto.setCreatedAt(Utills.getCurrentDate());
        useDto.setUpdatedAt(Utills.getCurrentDate());
        return  useDto;
    }
    @Test
    public  void  giveUserObjectWhenSaveUserObjectThenReturnSuccessMessage(){
        given(iUserRepository.findByEmail(prepaireUserDto().getEmail()))
                .willReturn(Optional.empty());
        given(iUserRepository.save(userEntity)).willReturn(userEntity);
        ResponseEntity response = iUserService.saveUserDetails(prepaireUserDto());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED,response.getStatusCode());
        Assertions.assertEquals(UserServiceConstant.USER_CREATED_SUCCESSFULLY,response.getBody());
    }
    @Test
    public  void  giveExistingEmailWhenSaveUserThenReturnEmailPresentMsg(){
        given(iUserRepository.findByEmail(prepaireUserDto().getEmail()))
                .willReturn(Optional.of(userEntity));
        ResponseEntity response =  iUserService.saveUserDetails(prepaireUserDto());
        Map<String,String> result=new HashMap();
        result.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
        result.put(UserServiceConstant.error_message,UserServiceConstant.EMAIL_IS_ALREADY_PRESENT);
        Assertions.assertEquals(result,response.getBody());
        verify(iUserRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("JUnit test for saveUserDetails method will Throw Exception")
    public  void  giveUserObjectWhenSaveUserObjectThenThrowException(){
        given(iUserRepository.findByEmail(prepaireUserDto().getEmail()))
                .willReturn(Optional.empty());
        when(iUserService.saveUserDetails(prepaireUserDto())).thenThrow(new CreateUserException(new Exception(),UserServiceConstant.USER_NOT_CREATED));
        Assertions.assertThrows(CreateUserException.class,()->iUserService.saveUserDetails(prepaireUserDtoForThrowException()));
    }

    @DisplayName("JUnit test for getUserDetailsById method")
    @Test
    public  void  givenUserIdWhenGetUserDetailsByIdThenReturnUserObject(){
        given(iUserRepository.findById(1)).willReturn(Optional.of(userEntity));
        ResponseEntity response = iUserService.getUserDetailsById(1);
        Assertions.assertNotNull(response.getBody());
    }
    @DisplayName("JUnit test for getUserDetailsById method For Non Existing userId")
    @Test
    public  void giveNonExistingUserIdWhenGetUserDetailsByIdThenReturnErrorMsg(){

        given(iUserRepository.findById(1)).willReturn(Optional.empty());
        ResponseEntity response =iUserService.getUserDetailsById(1);
        Map<String,String> result=new HashMap();
        result.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
        result.put(UserServiceConstant.error_message,UserServiceConstant.USER_IS_NOT_PRESENT_FOR_GIVEN_ID);
        Assertions.assertEquals(result,response.getBody());
    }

    @Test
    @DisplayName("JUnit test for getUserDetailsById method Throw Exception")
    public  void  giveUserIdWhenGetUserDetailsByIdThenThrowException(){
        given(iUserRepository.findById(1)).willReturn(Optional.of(userEntity));
        when(iUserService.getUserDetailsById(prepaireUserDto().getId())).thenThrow(new GetUserByIdException(new Exception(),new Exception().getMessage()));
        Assertions.assertThrows(GetUserByIdException.class,()->iUserService.getUserDetailsById(prepaireUserDto().getId()));
    }

    @DisplayName("JUnit test for deleteUser method")
    @Test
    public void giveUserIdWhenDeleteUserThenReturnSuccessMessage(){
        Integer userId = 1;
        given(iUserRepository.findById(userId)).willReturn(Optional.of(userEntity));
        willDoNothing().given(iUserRepository).deleteById(userId);
        // when -  action or the behaviour that we are going test
        ResponseEntity response = iUserService.deleteUser(userId);
        verify(iUserRepository, times(1)).deleteById(userId);
        Assertions.assertEquals(UserServiceConstant.USER_DELETED_SUCCESSFULLY,response.getBody());
    }

    @DisplayName("JUnit test for deleteUser method when userId is not present")
    @Test
    public void giveUserIdWhenDeleteUserThenReturnUserIdIsNotPresent(){
        Integer userId = 1;
        given(iUserRepository.findById(userId)).willReturn(Optional.empty());
        ResponseEntity response = iUserService.deleteUser(userId);
        Map<String,String> error=new HashMap();
        error.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
        error.put(UserServiceConstant.error_message,UserServiceConstant.USER_IS_NOT_PRESENT_FOR_GIVEN_ID);
        Assertions.assertEquals(error,response.getBody());
    }

    @DisplayName("JUnit test for deleteUser method when throw exception")
    @Test
    public void givenUserIdWhenDeleteUserThenThrowException(){
        Integer userId = 1;
        given(iUserRepository.findById(userId)).willReturn(Optional.of(userEntity));
        willThrow(new UserException(new Exception(),UserServiceConstant.USER_NOT_DELETED)).willDoNothing().given(iUserRepository).deleteById(userId);
        Assertions.assertThrows(UserException.class,() -> iUserService.deleteUser(userId));
    }

    @DisplayName("JUnit test for getUserByEmail method")
    @Test
    public  void  giveEmailIdWhenGetUserByEmailThenReturnUserObject(){

        given(iUserRepository.findByEmail(prepaireUserDto().getEmail())).willReturn(Optional.of(userEntity));
        ResponseEntity response = iUserService.getUserByEmail(prepaireUserDto().getEmail());
        Assertions.assertNotNull(response.getBody());
    }
    @DisplayName("JUnit test for getUserByEmail method For Non Existing EmailID")
    @Test
    public  void giveNonExistingEmailIdWhenGetUserByEmailThenReturnErrorMsg(){

        given(iUserRepository.findByEmail(prepaireUserDto().getEmail())).willReturn(Optional.empty());
        ResponseEntity response =iUserService.getUserByEmail(prepaireUserDto().getEmail());
        Map<String,String> result=new HashMap();
        result.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
        result.put(UserServiceConstant.error_message,UserServiceConstant.EMAIL_NOT_PRESENT);
        Assertions.assertEquals(result,response.getBody());
    }

    @DisplayName("JUnit test for getUserByEmail method when Throw exception")
    @Test
    public  void giveEmailIdWhenGetUserByEmailThenThrowException(){
        given(iUserRepository.findByEmail(prepaireUserDto().getEmail())).willReturn(Optional.of(userEntity));
        when(iUserService.getUserByEmail(prepaireUserDto().getEmail())).thenThrow(new UserException(new Exception(),new Exception().getMessage()));
        Assertions.assertThrows(UserException.class,() ->iUserService.getUserByEmail(prepaireUserDto().getEmail()));
    }
    @DisplayName("JUnit test for updateUser method")
    @Test
    public void givenUserIdWhenUpdateUserThenReturnObject(){
        Integer userId = 1;
        given(iUserRepository.findById(userId)).willReturn(Optional.of(userEntity));
         userEntity.setPhoneNumber("9420887276");
        given(iUserRepository.save(userEntity)).willReturn(userEntity);
         ResponseEntity response = iUserService.updateUser(prepaireUserDto());
         System.out.println(response.getBody());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    @DisplayName("JUnit test for updateUser method Throw Exception")
    public  void  giveUserWhenUpdateUserThenThrowException(){
        given(iUserRepository.findById(1)).willReturn(Optional.of(userEntity));
        userEntity.setPhoneNumber("9420887276");
        when(iUserRepository.save(userEntity)).thenThrow(new UserException(new Exception(),new  Exception().getMessage()));
        Assertions.assertThrows(UserException.class,()->iUserService.updateUser(prepaireUserDto()));
    }

    @Test
    @DisplayName("JUnit test for updateUser method when for given userId User is not present")
    public  void  givenUserIdWhenUpdateUserThenReturnMsgUserNotPresent(){
        given(iUserRepository.findById(1)).willReturn(Optional.empty());
        Map<String,String> error=new HashMap();
        error.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
        error.put(UserServiceConstant.error_message,UserServiceConstant.USER_IS_NOT_PRESENT_FOR_GIVEN_ID);
        ResponseEntity response = iUserService.updateUser(prepaireUserDto());
        Assertions.assertEquals(error,response.getBody());
    }

    @Test
    @DisplayName("JUnit test for getUsers method pagination")
    public  void  whenGetUsersThenReturnListUser(){
        Pageable paging = PageRequest.of(0, 2, Sort.by("inventory"));
        Page<UserEntity> userEntityPage= Mockito.mock(Page.class);
        given(iUserRepository.findAll(paging)).willReturn(userEntityPage);
        ResponseEntity response = iUserService.getUsers(paging);
        Assertions.assertEquals(new ArrayList<UserEntity>(),response.getBody());
    }

    @Test
    @DisplayName("JUnit test for getUsers method pagination return Exception")
    public  void  whenGetUsersThenThrowException(){
        Pageable paging = PageRequest.of(0, 2, Sort.by("inventory"));
        Page<UserEntity> userEntityPage= Mockito.mock(Page.class);
        given(iUserRepository.findAll(paging)).willReturn(userEntityPage);
        when(iUserService.getUsers(paging)).thenThrow(new UserException(new Exception(),new Exception().getMessage()));
        Assertions.assertThrows(UserException.class,() ->iUserService.getUsers(paging));
    }
}
