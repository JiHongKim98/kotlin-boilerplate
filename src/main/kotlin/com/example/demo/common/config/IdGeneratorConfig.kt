package com.example.demo.common.config

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

private val logger = KotlinLogging.logger {}

@Configuration
class IdGeneratorConfig(
    @Value("\${server.node:0}") private val nodeNumber: Int
) {

    @PostConstruct
    fun init() {
        node = nodeNumber
        logger.info { "Start Spring Application with node $node" }
    }

    companion object {
        var node: Int = 0
            private set
    }
}
