package com.example.demo.user.application

import com.example.demo.support.IntegrationTest
import com.example.demo.support.MysqlCleaner
import com.example.demo.user.createUser
import com.example.demo.user.domain.repository.UserRepository
import com.example.demo.user.exception.UserNotExistsException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

@IntegrationTest
class UserServiceTest(
    private val userService: UserService,
    private val userRepository: UserRepository,
) : BehaviorSpec({
    listeners(MysqlCleaner())

    Given("findUserInfoByUserId 메서드는") {
        val userId = 123456789L

        When("저장된 유저의 식별자로 조회한다면") {
            val savedUser = createUser(userId = userId)
                .run { userRepository.save(this) }

            val sut = userService.findUserInfoByUserId(userId)

            Then("유저 정보가 반환된다") {
                sut.shouldNotBeNull()
                sut.name shouldBe savedUser.name.value
                sut.userId shouldBe userId
            }
        }

        When("저장되지 않은 유저의 식별자로 조회하면") {
            Then("예외가 발생한다") {
                shouldThrow<UserNotExistsException> {
                    userService.findUserInfoByUserId(userId)
                }
            }
        }
    }
})
