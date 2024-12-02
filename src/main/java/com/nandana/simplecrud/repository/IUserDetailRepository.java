package com.nandana.simplecrud.repository;

import com.nandana.simplecrud.model.UserDetail;
import com.nandana.simplecrud.model.enums.EUserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUserDetailRepository extends JpaRepository<UserDetail, UUID> {

    UserDetail findUserDetailByEmail(String email);

    Page<UserDetail> findUserDetailsByUser_RoleOrderByCreatedDateDesc(EUserRole role, Pageable pageable);

    Page<UserDetail> findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseAndUser_RoleOrderByCreatedDateDesc(String firstName, String lastName, EUserRole role, Pageable pageable);
}
