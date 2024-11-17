package com.example.demo.auth.application

import com.example.demo.auth.createAuthToken
import com.example.demo.auth.domain.repository.AuthTokenRepository
import com.example.demo.support.IntegrationTest
import com.example.demo.support.RedisCleaner
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull

@IntegrationTest
class AuthFacadeServiceTest(
    private val authFacadeService: AuthFacadeService,
    private val authTokenRepository: AuthTokenRepository,
) : BehaviorSpec({
    listeners(RedisCleaner())

    Given("reissueToken 메서드는") {
        val tokenId = "123456789"
        val refreshToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl9pZCI6IjEyMzQ1Njc4OSIsImlhdCI6MTczMTg1NTk3MX0.Ml4tPD4APXt3UiPSDjag_sJud4mjpAn5x0BplfCrgAI"

        When("유효한 refresh 토큰이 들어올 때") {
            val savedAuthToken = createAuthToken(tokenId = tokenId)
                .run { authTokenRepository.save(this) }

            val sut = authFacadeService.reissueToken(refreshToken)

            Then("새로운 access 토큰과 refresh 토큰을 반환한다") {
                sut.accessToken.shouldNotBeNull()
                sut.refreshToken.shouldNotBeNull()
            }

            Then("기존의 인증 토큰은 삭제된다") {
                val expectEmptyAuthToken = authTokenRepository.findByTokenId(savedAuthToken.tokenId)
                expectEmptyAuthToken.shouldBeNull()
            }
        }
    }

    Given("deleteToken 메서드는") {
        val userId = 123456789L
        val tokenId = "123456789"
        val refreshToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl9pZCI6IjEyMzQ1Njc4OSIsImlhdCI6MTczMTg1NTk3MX0.Ml4tPD4APXt3UiPSDjag_sJud4mjpAn5x0BplfCrgAI"

        When("유효한 refresh 토큰과 유저 식별자가 들어올 때") {
            val savedAuthToken = createAuthToken(userId = userId, tokenId = tokenId)
                .run { authTokenRepository.save(this) }

            authFacadeService.deleteToken(userId, refreshToken)

            When("정상적으로 삭제된다") {
                val expectEmptyAuthToken = authTokenRepository.findByTokenId(savedAuthToken.tokenId)
                expectEmptyAuthToken.shouldBeNull()
            }
        }
    }
})
