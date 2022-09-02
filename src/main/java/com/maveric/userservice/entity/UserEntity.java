package com.maveric.userservice.entity;

import com.maveric.userservice.utils.Gender;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;


@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "user_details")
public class UserEntity {
 @Id
 @Column(name = "user_id")
 @GeneratedValue(strategy = GenerationType.AUTO)
 private Integer id;

 private String firstName;
 private String middleName;
 @NonNull
 private String lastName;
 private String email;
 private String phoneNumber;
 private String address;
 private Date dateOfBirth;
 private Date createdAt;
 private Date updatedAt;
 private String password;
 private String role;

 private Gender gender;

}