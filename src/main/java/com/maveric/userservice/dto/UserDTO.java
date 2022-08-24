package com.maveric.userservice.dto;

import org.springframework.stereotype.Component;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;


@Component

public class UserDTO implements Serializable {
    private  Integer id;

    @NotEmpty(message = "first name must not be empty")
    @Size(min = 3,message = "FirstName should be at least 3 chars")
    private String firstName;

    @NotEmpty(message = "middle name must not be empty")
    @Size(min = 4,message = "middleName should be at least 3 chars")
    private String middleName;

    @NotEmpty(message = "last name must not be empty")
    @Size(min = 4,message = "lastName should be at least 3 chars")
    private  String lastName;

    @NotBlank
    @Email
    private  String email;
    private  String phoneNumber;
    private  String address;

    @PastOrPresent
    private Date dateOfBirth;
    private  Date createdAt;
    private  Date updatedAt;
    private  String password;
    private  String role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
