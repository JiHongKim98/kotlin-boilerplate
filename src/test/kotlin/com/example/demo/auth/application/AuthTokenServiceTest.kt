package com.example.demo.auth.application

import com.example.demo.auth.createAuthToken
import com.example.demo.auth.domain.repository.AuthTokenRepository
import com.example.demo.auth.exception.NotExistsTokenException
import com.example.demo.auth.exception.TokenUserIdMismatchException
import com.example.demo.support.IntegrationTest
import com.example.demo.support.RedisCleaner
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

@IntegrationTest
class AuthTokenServiceTest(
    private val authTokenService: AuthTokenService,
    private val authTokenRepository: AuthTokenRepository,
) : BehaviorSpec({
    listeners(RedisCleaner())

    Given("createNewAuthToken 메서드는") {
        val userId = 123456789L

        When("유저 식별자가 들어왔을 때") {
            val sut = authTokenService.createNewAuthToken(userId)

            Then("새로운 인증 토큰이 저장된다") {
                val expectExistsAuthToken = authTokenRepository.findByTokenId(sut.tokenId)
                expectExistsAuthToken.shouldNotBeNull()

                sut.tokenId.shouldNotBeNull()
                sut.userId shouldBe userId
            }
        }
    }

    Given("regenerateAuthToken 메서드는") {
        val tokenId = "123456789"

        When("저장되어있는 인증 토큰의 식별자가 주어질 때") {
            val savedAuthToken = createAuthToken(tokenId = tokenId)
                .run { authTokenRepository.save(this) }

            val sut = authTokenService.regenerateAuthToken(tokenId)

            Then("기존의 토큰은 삭제되고 새로운 인증 토큰이 생성된다") {
                val expectEmptyAuthToken = authTokenRepository.findByTokenId(savedAuthToken.tokenId)
                expectEmptyAuthToken.shouldBeNull()

                val expectExistsAuthToken = authTokenRepository.findByTokenId(sut.tokenId)
                expectExistsAuthToken.shouldNotBeNull()
            }
        }

        When("유효하지 않는 인증 토큰의 식별자가 주어질 때") {
            Then("예외가 발생한다") {
                shouldThrow<NotExistsTokenException> {
                    authTokenService.regenerateAuthToken(tokenId)
                }
            }
        }
    }

    Given("removeTokenIfMatchUserId 메서드는") {
        val tokenId = "123456789"
        val userId = 123456789L

        When("유효한 인증 토큰 식별자와 유저 식별자가 주어질 때") {
            val savedAuthToken = createAuthToken(tokenId = tokenId, userId = userId)
                .run { authTokenRepository.save(this) }

            authTokenService.removeTokenIfMatchUserId(tokenId, userId)

            Then("해당 인증 토큰을 삭제한다") {
                val expectEmptyAuthToken = authTokenRepository.findByTokenId(savedAuthToken.tokenId)
                expectEmptyAuthToken.shouldBeNull()
            }
        }

        When("인증 토큰의 유저 식별자와 일치하지 않는 값이 주어질 때") {
            val differentUserId = 0L
            createAuthToken(tokenId = tokenId, userId = differentUserId)
                .run { authTokenRepository.save(this) }

            Then("예외가 발생한다") {
                shouldThrow<TokenUserIdMismatchException> {
                    authTokenService.removeTokenIfMatchUserId(tokenId, userId)
                }
            }
        }

        When("유효하지 않는 인증 토큰 식별자가 주어질 경우") {
            Then("예외가 발생한다") {
                shouldThrow<NotExistsTokenException> {
                    authTokenService.removeTokenIfMatchUserId(tokenId, userId)
                }
            }
        }
    }
})
