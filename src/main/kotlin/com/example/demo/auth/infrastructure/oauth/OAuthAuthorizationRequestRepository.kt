package com.example.demo.auth.infrastructure.oauth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Component
class OAuthAuthorizationRequestRepository : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    override fun loadAuthorizationRequest(request: HttpServletRequest?): OAuth2AuthorizationRequest {
        TODO("Not yet implemented")
    }

    override fun removeAuthorizationRequest(
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ): OAuth2AuthorizationRequest? {
        return request?.getParameter("state")?.let {
            requestStorage.remove(it)
        }
    }

    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ) {
        authorizationRequest?.let {
            requestStorage[it.state] = authorizationRequest
        }
    }

    companion object {
        private val requestStorage: ConcurrentMap<String, OAuth2AuthorizationRequest> = ConcurrentHashMap()
    }
}
