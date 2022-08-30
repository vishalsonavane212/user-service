package com.maveric.userservice.entity;

import com.maveric.userservice.utils.Gender;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

/*@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter*/

@Entity
@Table(name = "user_details")
public class UserEntity {
 @Id
 @Column(name="user_id")
 @GeneratedValue(strategy = GenerationType.AUTO)
 private  Integer id;

 private String firstName;
 private String middleName;
 private  String lastName;
 private  String email;
 private  String phoneNumber;
 private  String address;
 private Date dateOfBirth;
 private  Date createdAt;
 private  Date updatedAt;
 private  String password;
 private  String role;

 private Gender gender;
 //need to add gender

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

 public Gender getGender() {
  return gender;
 }

 public void setGender(Gender gender) {
  this.gender = gender;
 }
}
