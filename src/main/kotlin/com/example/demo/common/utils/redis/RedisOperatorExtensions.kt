package com.example.demo.common.utils.redis

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.redis.core.ValueOperations
import java.time.Duration
import kotlin.reflect.KClass

private val objectMapper = jacksonObjectMapper()

fun ValueOperations<String, String>.setWithSerialize(key: String, value: Any, millisTTL: Long? = null) {
    val valueAsString = objectMapper.writeValueAsString(value)
    millisTTL
        ?.let { set(key, valueAsString, Duration.ofMillis(millisTTL)) }
        ?: set(key, valueAsString)
}

fun <V : Any> ValueOperations<String, String>.getWithDeserialize(key: String, convertClass: KClass<V>): V? {
    val value = get(key)
    return value?.let { objectMapper.readValue(it, convertClass.java) }
}

fun <V : Any> ValueOperations<String, String>.getAndDeleteWithDeserialize(key: String, convertClass: KClass<V>): V? {
    val value = getAndDelete(key)
    return value?.let { objectMapper.readValue(it, convertClass.java) }
}
