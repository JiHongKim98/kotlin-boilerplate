package com.example.demo.user.application

import com.example.demo.auth.domain.event.AuthUserCreateEvent
import com.example.demo.support.IntegrationTest
import com.example.demo.support.MysqlCleaner
import com.example.demo.user.domain.repository.UserRepository
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.context.ApplicationEventPublisher
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition

@IntegrationTest
class UserInternalEventListenerTest(
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val transactionManager: PlatformTransactionManager,
) : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    listeners(MysqlCleaner())

    Given("authUserCreateListener 메서드는") {
        val userId = 123456789L
        val authUserId = 987654321L
        val event = AuthUserCreateEvent(userId = userId, authUserId = authUserId)

        When("트랜잭션 내에서 새로운 인증 유저 생성 이벤트가 발생할 때") {
            val transaction = transactionManager.getTransaction(DefaultTransactionDefinition())

            eventPublisher.publishEvent(event)

            Then("트랜잭션이 커밋된다면 새로운 유저를 생성해야한다") {
                transactionManager.commit(transaction)

                val users = userRepository.findAll()
                users.size shouldBe 1

                val userSut = users[0]
                userSut.id shouldBe userId
                userSut.authUserId shouldBe authUserId
            }

            Then("트랜잭션이 롤백된다면 새로운 유저를 생성하지 않는다") {
                transactionManager.rollback(transaction)

                val users = userRepository.findAll()
                users.size shouldBe 0
            }
        }
    }
})
