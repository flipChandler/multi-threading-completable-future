package com.javatechie.multithreadingexample.repository;

import com.javatechie.multithreadingexample.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
