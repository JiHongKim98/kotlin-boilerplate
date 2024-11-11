package com.example.demo.common.logging

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.util.StreamUtils
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

private val logger = KotlinLogging.logger {}

class ReqResLoggingFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val cachedRequest = ContentCachingRequestWrapper(request)
        val cachedResponse = ContentCachingResponseWrapper(response)

        filterChain.doFilter(cachedRequest, cachedResponse)

        doLogging(cachedRequest, cachedResponse)
    }

    companion object {
        private fun doLogging(request: ContentCachingRequestWrapper, response: ContentCachingResponseWrapper) {
            val status = response.status
            val method = request.method
            val remoteAddr = request.remoteAddr
            val requestURI = request.requestURI
            val formattedQueryString = getFormattedQueryString(request.queryString)
            val requestBody = getRequestBody(request)
            val responseBody = getResponseBody(response)

            val loggingMessage = buildString {
                append("\n| [REQUEST] ($method) $requestURI$formattedQueryString")
                append("\n| >> STATUS: $status")
                append("\n| >> REMOTE_ADDRESS: $remoteAddr")
                if (requestBody.isNotEmpty()) {
                    append("\n| >> REQUEST_BODY: $requestBody")
                }
                if (responseBody.isNotEmpty()) {
                    append("\n| >> RESPONSE_BODY: $responseBody")
                }
            }

            if (status < 500) {
                logger.info { loggingMessage }
            } else {
                logger.error { loggingMessage }
            }
        }

        private fun getFormattedQueryString(queryString: String?): String {
            return queryString?.takeIf { it.isNotBlank() }
                ?.let { "?$it" }
                ?: ""
        }

        private fun getRequestBody(request: ContentCachingRequestWrapper): String {
            return try {
                ByteArrayInputStream(request.contentAsByteArray).use { inputStream ->
                    StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8)
                }
            } catch (e: Exception) {
                logger.warn { "ReqResLoggingFilter::getRequestBody | get request body failed $e" }
                ""
            }
        }

        private fun getResponseBody(response: ContentCachingResponseWrapper): String {
            return try {
                response.contentInputStream.use { inputStream ->
                    val responseBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8)
                    response.copyBodyToResponse()
                    responseBody
                }
            } catch (e: Exception) {
                logger.warn { "ReqResLoggingFilter::getResponseBody | get response body failed $e" }
                ""
            }
        }
    }
}
