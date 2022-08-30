package com.maveric.userservice.repository;

import com.maveric.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity,Integer> {

    public Optional<UserEntity> findById(Integer userID);

    public  UserEntity save(UserEntity userEntity);

    public  Optional<UserEntity> findByEmail(String email);
}
