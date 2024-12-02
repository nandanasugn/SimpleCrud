package com.nandana.simplecrud.service;

import com.nandana.simplecrud.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

    User getUserByUsername(String username);

    void createUser(User user);

    void updateUser(User user);

    void deleteCustomer(String username);

    void deleteAdmin(String username);
}
