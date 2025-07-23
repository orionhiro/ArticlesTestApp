package com.orionhiro.ArticlesApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orionhiro.ArticlesApp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
