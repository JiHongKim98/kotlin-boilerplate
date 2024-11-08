package com.example.demo.auth.application

import com.example.demo.auth.domain.event.AuthUserCreateEvent
import com.example.demo.auth.domain.model.AuthUser
import com.example.demo.auth.domain.repository.AuthUserRepository
import com.example.demo.auth.domain.vo.SocialType
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthUserService(
    private val authUserRepository: AuthUserRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    @Transactional
    fun getOrRegisterAuthUser(socialId: String, stringSocialType: String): AuthUser {
        val socialType = SocialType.valueOf(stringSocialType.uppercase())

        return authUserRepository.findBySocialIdAndSocialType(socialId, socialType)
            ?: AuthUser.create(socialId, socialType).let {
                eventPublisher.publishEvent(AuthUserCreateEvent.create(it))
                authUserRepository.save(it)
            }
    }
}
