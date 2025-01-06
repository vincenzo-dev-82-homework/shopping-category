package com.musinsa.common.swagger

import io.swagger.v3.oas.models.media.Schema
import org.springdoc.core.models.GroupedOpenApi
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Configuration
class SwaggerConfiguration {
    init {
        SpringDocUtils.getConfig().replaceWithSchema(
            LocalDateTime::class.java,
            Schema<LocalDateTime>().apply {
                example(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                type("string")
            },
        )
        SpringDocUtils.getConfig().replaceWithSchema(
            LocalTime::class.java,
            Schema<LocalTime>().apply {
                example(LocalTime.now().format(DateTimeFormatter.ISO_TIME))
                type("string")
            },
        )
        SpringDocUtils.getConfig().replaceWithSchema(
            LocalDate::class.java,
            Schema<LocalDate>().apply {
                example(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                type("string")
            },
        )
    }

    @Bean
    fun default(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("API")
            .addOpenApiCustomizer {
                it.info.version("v1")
            }.packagesToScan("com.musinsa")
            .pathsToMatch("/v1/musinsa/**")
            .build()
}
