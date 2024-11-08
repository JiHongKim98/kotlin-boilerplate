package com.example.demo.auth.domain.repository

import com.example.demo.auth.domain.model.AuthUser
import com.example.demo.auth.domain.vo.SocialType
import org.springframework.data.jpa.repository.JpaRepository

interface AuthUserRepository : JpaRepository<AuthUser, Long> {

    fun findBySocialIdAndSocialType(socialId: String, socialType: SocialType): AuthUser?
}
