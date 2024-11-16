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

    Given("userService에서") {

        When("존재하는 user id를 조회하면") {
            val userId = 1234567890L
            val user = userRepository.save(createUser(userId = userId))

            val sut = userService.findUserInfoByUserId(userId)

            Then("사용자 정보가 반환된다") {
                sut.shouldNotBeNull()
                sut.name shouldBe user.name.value
                sut.userId shouldBe userId
            }
        }

        When("존재하지 않는 user id를 조회하면") {
            val nonExistentUserId = -1L

            Then("UserNotExistsException 예외가 발생한다") {
                shouldThrow<UserNotExistsException> {
                    userService.findUserInfoByUserId(nonExistentUserId)
                }
            }
        }
    }
})
