package com.nandana.simplecrud.service.impl;

import com.nandana.simplecrud.model.User;
import com.nandana.simplecrud.model.enums.EUserRole;
import com.nandana.simplecrud.repository.IUserRepository;
import com.nandana.simplecrud.service.IUserService;
import com.nandana.simplecrud.util.ObjectValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class UserServiceImpl implements IUserService {
    private final ObjectValidator objectValidator;
    private final IUserRepository userRepository;

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public void createUser(User user) {
        objectValidator.validate(user);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error creating user", e);
        }
    }

    @Override
    public void updateUser(User user) {
        objectValidator.validate(user);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error updating user", e);
        }
    }

    @Override
    public void deleteCustomer(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null || user.getRole() != EUserRole.CUSTOMER) {
            throw new UsernameNotFoundException("User not found");
        }
        userRepository.delete(user);
    }

    @Override
    public void deleteAdmin(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null || user.getRole() != EUserRole.ADMIN) {
            throw new UsernameNotFoundException("User not found");
        }
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username);
    }
}
