package com.maveric.userservice.dto;

import com.maveric.userservice.utils.Gender;
import com.maveric.userservice.utils.UserServiceConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Component

public class UserDTO implements Serializable {
    private  Integer id;

    @NotEmpty(message = UserServiceConstant.FIRST_NAME_MUST_NOT_BE_EMPTY)
    private String firstName;

    private String middleName;

    //@NotEmpty(message = UserServiceConstant.LAST_NAME_MUST_NOT_BE_EMPTY)
    private  String lastName;

    @NotBlank
    @Email
    private  String email;

    @NotEmpty(message = UserServiceConstant.PHONE_NUMBER_REQUIRED)
    private  String phoneNumber;
    @NotEmpty(message = UserServiceConstant.ADDRESS_IS_REQUIRED)
    private  String address;

    //@PastOrPresent
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;
    private  Date createdAt;
    private  Date updatedAt;

    private  String password;
    private  String role;

    private Gender gender;

}
