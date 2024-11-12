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
        require(value.length in MIN_LEN..MAX_LEN) { "이름이 너무 길거나 짧습니다." }
        require(value.matches(NAME_REGEX.toRegex())) { "형식에 맞지 않는 이름입니다." }
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
