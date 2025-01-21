package com.project.banking_app_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.banking_app_application.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
