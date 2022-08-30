package com.maveric.userservice.service;

import com.maveric.userservice.dto.UserDTO;
import com.maveric.userservice.repository.IUserRepository;
import com.maveric.userservice.utils.UserServiceConstant;
import com.maveric.userservice.utils.Utills;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private IUserRepository iUserRepository;

    @InjectMocks // auto inject helloRepository
    private UserService iUserService = new UserServiceImpl();

    public UserDTO prepaireUserDto(){
        UserDTO useDto=new UserDTO();
        useDto.setId(1);
        useDto.setFirstName("ABCD");
        useDto.setMiddleName("abcd");
        useDto.setLastName("XYZo");
        useDto.setAddress("pune");
        useDto.setEmail("vikas@gmail.com");
        useDto.setPhoneNumber("9404074081");
        useDto.setPassword("1234");
        useDto.setRole("admin");
        useDto.setDateOfBirth(Utills.getCurrentDate());
        useDto.setCreatedAt(Utills.getCurrentDate());
        useDto.setUpdatedAt(Utills.getCurrentDate());
        return  useDto;
    }
    @Test
    public  void  givenUserObject_whenSaveUserObject_ThenReturnUser(){
        String dto = iUserService.saveUserDetails(prepaireUserDto());
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(UserServiceConstant.user_created_successfully,dto);
    }
}
