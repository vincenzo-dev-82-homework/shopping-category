package com.musinsa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class ShoppingCategoryApplication

fun main(args: Array<String>) {
    runApplication<ShoppingCategoryApplication>(*args)
}
