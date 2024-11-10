package com.example.demo.auth.infrastructure.oauth

import com.example.demo.auth.application.SocialLoginFacadeService
import com.example.demo.common.presentation.cookie.CookieHandler
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuthAuthenticationSuccessHandler(
    private val socialLoginFacadeService: SocialLoginFacadeService,
    private val cookieHandler: CookieHandler,

    @Value("\${spring.security.oauth2.success-redirect-url}")
    private val successRedirectUrl: String,
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        require(authentication is OAuth2AuthenticationToken)

        val tokenPairResponse = socialLoginFacadeService.loginOrCreateAuthUser(
            socialId = authentication.principal.name,
            stringSocialType = authentication.authorizedClientRegistrationId,
        )
        cookieHandler.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, tokenPairResponse.refreshToken)
        response.sendRedirect(successRedirectUrl)
    }

    companion object {
        private const val REFRESH_TOKEN_COOKIE_NAME = "refresh_token"
    }
}
