package com.fundmate.app.fundmate.Reposetries;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fundmate.app.fundmate.Entity_Classes.User;

public interface User_Reposetry extends MongoRepository<User, String> 
{
    public boolean existsByEmail(String email);
    public User findByEmail(String email);
    public User findByEmailAndPassword(String email, String password);
    
}

