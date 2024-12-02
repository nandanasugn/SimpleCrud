package com.nandana.simplecrud.repository;

import com.nandana.simplecrud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    User findUserByUsername(String username);
}
