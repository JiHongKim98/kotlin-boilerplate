package com.example.demo.common.config

import com.example.demo.auth.infrastructure.oauth.OAuthAuthenticationSuccessHandler
import com.example.demo.auth.infrastructure.oauth.OAuthAuthorizationRequestRepository
import com.example.demo.common.security.CustomAuthenticationEntryPoint
import com.example.demo.common.security.JwtAuthenticationFilter
import com.example.demo.common.security.JwtAuthenticationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.NegatedRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(
        httpSecurity: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        oAuthAuthenticationSuccessHandler: OAuthAuthenticationSuccessHandler,
        oAuthAuthorizationRequestRepository: OAuthAuthorizationRequestRepository,
    ): SecurityFilterChain {
        return httpSecurity
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .logout { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { request ->
                request
                    .requestMatchers("/api/v1/auth/reissue").permitAll()
                    .requestMatchers("/oauth2/authorization/kakao").permitAll()
                    .requestMatchers("/login/oauth2/code/kakao").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .oauth2Login { oauthConfig ->
                oauthConfig.userInfoEndpoint {
                    it.userService(DefaultOAuth2UserService())
                }.authorizationEndpoint { endpoint ->
                    endpoint.authorizationRequestRepository(oAuthAuthorizationRequestRepository)
                }.successHandler(oAuthAuthenticationSuccessHandler)
            }
            .build()
    }

    @Bean
    fun jwtAuthenticationFilter(
        jwtAuthenticationProvider: JwtAuthenticationProvider,
        customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
    ): JwtAuthenticationFilter {
        val providerManager = ProviderManager(jwtAuthenticationProvider)
        val filter = JwtAuthenticationFilter(generatedRequestMatcher()).apply {
            setAuthenticationManager(providerManager)
            setAuthenticationFailureHandler(AuthenticationEntryPointFailureHandler(customAuthenticationEntryPoint))
        }
        return filter
    }

    private fun generatedRequestMatcher() = NegatedRequestMatcher(
        OrRequestMatcher(
            AntPathRequestMatcher("/api/v1/auth/reissue"),
            AntPathRequestMatcher("/oauth2/authorization/kakao"),
            AntPathRequestMatcher("/login/oauth2/code/kakao")
        )
    )

    private fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfig = CorsConfiguration().apply {
            addAllowedOrigin("*")
            addAllowedMethod("*")
            addAllowedHeader("*")
            allowCredentials = true
        }
        source.registerCorsConfiguration("/**", corsConfig)
        return source
    }
}

