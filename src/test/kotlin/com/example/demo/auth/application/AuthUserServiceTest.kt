package com.example.demo.auth.application

import com.example.demo.auth.createAuthUser
import com.example.demo.auth.domain.repository.AuthUserRepository
import com.example.demo.auth.domain.vo.SocialType
import com.example.demo.support.IntegrationTest
import com.example.demo.support.MysqlCleaner
import com.example.demo.user.domain.repository.UserRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

@IntegrationTest
class AuthUserServiceTest(
    private val userRepository: UserRepository,
    private val authUserService: AuthUserService,
    private val authUserRepository: AuthUserRepository,
) : BehaviorSpec({
    listeners(MysqlCleaner())

    Given("getOrRegisterAuthUser 메서드는") {
        val socialId = "123456789"
        val stringSocialType = "kakao"

        When("새로운 소셜 유저의 정보가 들어올 때") {
            val sut = authUserService.getOrRegisterAuthUser(socialId, stringSocialType)

            Then("새로운 인증 유저를 반환한다") {
                sut.id.shouldNotBeNull()
                sut.userId.shouldNotBeNull()
                sut.socialId shouldBe socialId
                sut.socialType shouldBe SocialType.KAKAO
            }

            Then("새로운 인증 유저가 생성된다") {
                val authUsers = authUserRepository.findAll()
                authUsers.size shouldBe 1

                val authUser = authUsers[0]
                authUser.socialId shouldBe socialId
                authUser.socialType shouldBe SocialType.KAKAO
            }

            Then("소셜 유저에 대응하는 새로운 유저가 생성된다") {
                val users = userRepository.findAll()
                users.size shouldBe 1

                val user = users[0]
                user.id shouldBe sut.userId
                user.authUserId shouldBe sut.id
            }
        }

        When("기존 소셜 유저의 정보가 들어올 때") {
            val savedAuthUser = createAuthUser(socialId = socialId, socialType = SocialType.KAKAO)
                .run { authUserRepository.save(this) }

            val sut = authUserService.getOrRegisterAuthUser(socialId, stringSocialType)

            Then("기존 인증 유저를 반환한다") {
                val authUsers = authUserRepository.findAll()
                authUsers.size shouldBe 1

                sut.id shouldBe savedAuthUser.id
            }
        }
    }
})
