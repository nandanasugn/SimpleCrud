package com.nandana.simplecrud.model;

import com.nandana.simplecrud.audit.Auditable;
import com.nandana.simplecrud.dto.response.UserResponseDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_details")
@SuperBuilder
public class UserDetail extends Auditable {

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @Column(nullable = false)
    private String lastName;

    private String email;

    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Date dateOfBirth;

    private String documentUrl;

    private String videoUrl;

    private String imageUrl;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserResponseDto convertToResponseDto() {
        return UserResponseDto.builder()
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .username(user.getUsername())
                .email(email)
                .phoneNumber(phoneNumber)
                .address(address)
                .dateOfBirth(dateOfBirth)
                .documentUrl(documentUrl)
                .videoUrl(videoUrl)
                .imageUrl(imageUrl)
                .build();
    }
}
