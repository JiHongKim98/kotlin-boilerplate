package com.example.demo.user.application

import com.example.demo.auth.domain.event.AuthUserCreateEvent
import com.example.demo.user.domain.model.User
import com.example.demo.user.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Service
class UserInternalEventListener(
    private val userRepository: UserRepository,
) {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun authUserCreateListener(event: AuthUserCreateEvent) {
        val user = User.create(event.userId, event.authUserId)
        userRepository.save(user)
    }
}
