package com.maveric.userservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.userservice.dto.UserDTO;
import com.maveric.userservice.repository.IUserRepository;
import com.maveric.userservice.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

@SpringBootTest
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}
	protected MockMvc mockMvc;

	@InjectMocks // auto inject helloRepository
	private UserService iUserService = new UserService();

	@Mock
	private IUserRepository iUserRepository;
	@Autowired
	WebApplicationContext webApplicationContext;

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
		useDto.setFirstName("ABC");
		useDto.setMiddleName("abc");
		useDto.setLastName("XYZ");
		useDto.setAddress("pune");
		useDto.setEmail("vikas@gmail.com");
		useDto.setPhoneNumber("9404074081");
		useDto.setPassword("1234");
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

}
