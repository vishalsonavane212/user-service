package com.maveric.userservice.service;

import com.maveric.userservice.dto.UserDTO;
import com.maveric.userservice.entity.UserEntity;
import com.maveric.userservice.repository.UserRepository;
import com.maveric.userservice.utils.UserServiceConstant;
import com.maveric.userservice.utils.Utills;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository iUserRepository;

    @InjectMocks // auto inject helloRepository
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
/*        userEntity.setDateOfBirth(Utills.getCurrentDate());
        userEntity.setCreatedAt(Utills.getCurrentDate());
        userEntity.setUpdatedAt(Utills.getCurrentDate());*/
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
    @Test
    public  void  givenUserObject_whenSaveUserObject_ThenReturnSuccessMessage(){
        given(iUserRepository.findByEmail(prepaireUserDto().getEmail()))
                .willReturn(Optional.empty());

        given(iUserRepository.save(userEntity)).willReturn(userEntity);

        ResponseEntity response = iUserService.saveUserDetails(prepaireUserDto());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED,response.getStatusCode());
        Assertions.assertEquals(UserServiceConstant.user_created_successfully,response.getBody());
    }
    @Test
    public  void  givenExistingEmail_whenSaveUser_thenReturnEmailPresentMsg(){
        given(iUserRepository.findByEmail(prepaireUserDto().getEmail()))
                .willReturn(Optional.of(userEntity));

        ResponseEntity response =  iUserService.saveUserDetails(prepaireUserDto());
        Map<String,String> result=new HashMap<String,String>();
        result.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
        result.put(UserServiceConstant.error_message,UserServiceConstant.email_is_already_present);
        Assertions.assertEquals(result,response.getBody());

        verify(iUserRepository, never()).save(any(UserEntity.class));
    }

    @DisplayName("JUnit test for getUserDetailsById method")
    @Test
    public  void  givenUserId_whenGetUserDetailsById_thenReturnUserObject(){

        given(iUserRepository.findById(1)).willReturn(Optional.of(userEntity));

        ResponseEntity response = iUserService.getUserDetailsById(1);
        Assertions.assertNotNull(response.getBody());
    }
    @DisplayName("JUnit test for getUserDetailsById method For Non Existing userId")
    @Test
    public  void giveNonExistingUserId_whenGetUserDetailsById_thenReturnErrorMsg(){

        given(iUserRepository.findById(1)).willReturn(Optional.empty());

        ResponseEntity response =iUserService.getUserDetailsById(1);
        Map<String,String> result=new HashMap<String,String>();
        result.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
        result.put(UserServiceConstant.error_message,UserServiceConstant.user_is_not_present_given_id);
        Assertions.assertEquals(result,response.getBody());

    }

    @DisplayName("JUnit test for deleteUser method")
    @Test
    public void givenUserId_whenDeleteUser_thenReturnSuccessMessage(){
        // given - precondition or setup
        Integer userId = 1;

        given(iUserRepository.findById(userId)).willReturn(Optional.of(userEntity));

        willDoNothing().given(iUserRepository).deleteById(userId);
        // when -  action or the behaviour that we are going test
        ResponseEntity response = iUserService.deleteUser(userId);
        // then - verify the output
        verify(iUserRepository, times(1)).deleteById(userId);

        Assertions.assertEquals(UserServiceConstant.user_successfully_deleted,response.getBody());
    }

    @DisplayName("JUnit test for getUserByEmail method")
    @Test
    public  void  givenEmailId_whenGetUserByEmail_thenReturnUserObject(){

        given(iUserRepository.findByEmail(prepaireUserDto().getEmail())).willReturn(Optional.of(userEntity));

        ResponseEntity response = iUserService.getUserByEmail(prepaireUserDto().getEmail());
        Assertions.assertNotNull(response.getBody());
    }
    @DisplayName("JUnit test for getUserByEmail method For Non Existing EmailID")
    @Test
    public  void giveNonExistingEmailId_whenGetUserByEmail_thenReturnErrorMsg(){

        given(iUserRepository.findByEmail(prepaireUserDto().getEmail())).willReturn(Optional.empty());

        ResponseEntity response =iUserService.getUserByEmail(prepaireUserDto().getEmail());
        Map<String,String> result=new HashMap<String,String>();
        result.put(UserServiceConstant.error_code,UserServiceConstant.BAD_REQUEST);
        result.put(UserServiceConstant.error_message,UserServiceConstant.email_not_present);
        Assertions.assertEquals(result,response.getBody());

    }

    @DisplayName("JUnit test for updateUser method")
    @Test
    public void givenUserId_whenUpdateUser_thenReturnObject(){
        // given - precondition or setup
        Integer userId = 1;

       // given(iUserRepository.findByEmail(prepaireUserDto().getEmail())).willReturn(Optional.empty());
        given(iUserRepository.findById(userId)).willReturn(Optional.of(userEntity));
         userEntity.setPhoneNumber("9420887276");
        given(iUserRepository.save(userEntity)).willReturn(userEntity);

         ResponseEntity response = iUserService.updateUser(prepaireUserDto());
         System.out.println(response.getBody());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    }
}
