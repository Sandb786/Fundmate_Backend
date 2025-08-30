package com.fundmate.app.fundmate.Entity_Classes;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class User 
{

    @Id
    private String id;

    private String name;
    private String email;
    private String password;

    private boolean emailVerified = false;

    @Transient
    private String verificationToken;
    
    @Transient
    private LocalDateTime tokenExpiry;
}


/*
 * cretain json file for this class
 * {
 *   "name": "John Doe",
 *  "email": "demo@kk",
 * "password": "123456",
 * "emailVerified": false,
 * "verificationToken":"sffhsd45645esijio",
 * "tokenExpiry": "2023-10-01T12:00:00"
 * }
 */