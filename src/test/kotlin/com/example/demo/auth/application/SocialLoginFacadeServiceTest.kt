package com.example.demo.auth.application

import com.example.demo.support.IntegrationTest
import com.example.demo.support.MysqlCleaner
import com.example.demo.support.RedisCleaner
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull

@IntegrationTest
class SocialLoginFacadeServiceTest(
    private val socialLoginFacadeService: SocialLoginFacadeService,
) : BehaviorSpec({
    listeners(MysqlCleaner(), RedisCleaner())

    Given("loginOrCreateAuthUser 메서드는") {
        val socialId = "123456789"
        val stringSocialType = "kakao"

        When("유효한 소셜 인증 정보가 들어올 경우") {
            val sut = socialLoginFacadeService.loginOrCreateAuthUser(socialId, stringSocialType)

            Then("새로운 access 토큰과 refresh 토큰을 반환한다") {
                sut.accessToken.shouldNotBeNull()
                sut.refreshToken.shouldNotBeNull()
            }
        }
    }
})
