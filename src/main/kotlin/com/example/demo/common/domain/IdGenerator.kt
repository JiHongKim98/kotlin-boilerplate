package com.example.demo.common.domain

import com.example.demo.common.config.IdGeneratorConfig
import com.github.f4b6a3.tsid.TsidFactory

object IdGenerator {

    private val tsidFactory: TsidFactory = TsidFactory.builder()
        .withNode(IdGeneratorConfig.node)
        .build()

    fun generate(): Long = tsidFactory.create().toLong()
}
