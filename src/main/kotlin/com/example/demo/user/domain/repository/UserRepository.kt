package com.example.demo.user.domain.repository

import com.example.demo.user.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>
