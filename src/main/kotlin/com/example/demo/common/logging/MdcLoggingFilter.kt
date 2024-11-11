package com.example.demo.common.logging

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

class MdcLoggingFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        MDC.putCloseable(REQUEST_ID, getXRequestId(request)).use {
            filterChain.doFilter(request, response)
        }
    }

    companion object {
        private const val REQUEST_ID = "request_id"
        private const val X_REQUEST_ID = "X-Request-ID"

        private fun getXRequestId(request: HttpServletRequest): String {
            return request.getHeader(X_REQUEST_ID)
                ?: UUID.randomUUID().toString().replace("-", "")
        }
    }
}
