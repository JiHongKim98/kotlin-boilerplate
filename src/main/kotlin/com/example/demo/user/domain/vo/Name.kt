package com.example.demo.user.domain.vo

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.util.*

@Embeddable
class Name(
    @Column(name = "name", nullable = false)
    val value: String = generateRandomValue(),
) {

    init {
        validate()
    }

    private fun validate() {
        require(value.length in MIN_LEN..MAX_LEN) {
            "name length must be between $MIN_LEN and $MAX_LEN characters"
        }

        require(value.matches(NAME_REGEX.toRegex())) {
            "name contains invalid characters or format"
        }
    }

    companion object NameGenerator {
        private const val MIN_LEN = 2
        private const val MAX_LEN = 10
        private const val NAME_REGEX = "^[가-힣a-zA-Z0-9._-]+\$"

        fun generateRandomValue(): String {
            return UUID.randomUUID().toString().replace("-", "").take(10)
        }
    }
}
