package com.maveric.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.userservice.dto.UserDTO;
import com.maveric.userservice.repository.IUserRepository;
import com.maveric.userservice.service.UserService;
import com.maveric.userservice.utils.Utills;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WebAppConfiguration
@AutoConfigureMockMvc
public class userControllerTest {

     @Autowired
    private MockMvc mockMvc;

    @InjectMocks // auto inject helloRepository
    private UserService iUserService = new UserService();

    @Mock
    private IUserRepository iUserRepository;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Before("")
    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
    @Test
    public void createUserTest() throws JsonProcessingException {
     String uri="/api/v1/users";
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
        String inputJson = this.mapToJson(useDto);
        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

            int status=result.getResponse().getStatus();
            Assertions.assertEquals(201, status);
            String content = result.getResponse().getContentAsString();
            Assertions.assertEquals("User created successfully",content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void getUserTest(){
        String uri="/api/v1/users/2";
        try{
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                    .contentType(MediaType.APPLICATION_JSON)).andReturn();
            int status=result.getResponse().getStatus();
            String response = result.getResponse().getContentAsString();
          UserDTO dto = this.mapFromJson(response,UserDTO.class);
          //Assertions.assertEquals(dto.getId() != null);
          Assertions.assertNotNull(dto);
        }catch (Exception e){
            throw  new RuntimeException(e);
        }

    }

    @Test
    public void getUsersTest(){
        String uri="/api/v1/users?page=1&size=10";
        try{
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                    .contentType(MediaType.APPLICATION_JSON)).andReturn();
            int status=result.getResponse().getStatus();
            String response = result.getResponse().getContentAsString();
            //UserDTO[] dto = this.mapFromJson(response,UserDTO[].class);
           // System.out.println(dto);
            Assertions.assertEquals(200,status);
            //String uriToNextPage = extractURIByRel(result.getResponse().getHeader("Link"), "next");
            //Assertions.assertEquals("/api/v1/users?page=1&size=2", uriToNextPage);

        }catch (Exception e){
            throw  new RuntimeException(e);
        }

    }

    @Test
    public  void  updateUserTest() throws JsonProcessingException {
        String uri="/api/v1/users/2";
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
        String inputJson = this.mapToJson(useDto);
        try{
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(uri)
                    .contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
            int status=result.getResponse().getStatus();
            String response = result.getResponse().getContentAsString();
            Assertions.assertEquals(200,status);
            UserDTO dto = this.mapFromJson(response,UserDTO.class);
            Assertions.assertNotNull(dto);
        }catch (Exception e){
          throw new  RuntimeException(e);
        }
    }
    @Test
    public void deleteUser() throws Exception {
        String uri="/api/v1/users/1";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        int status=result.getResponse().getStatus();
        String response = result.getResponse().getContentAsString();
        Assertions.assertEquals(200,status);
        Assertions.assertEquals(response,"User successfully deleted");

    }

}
