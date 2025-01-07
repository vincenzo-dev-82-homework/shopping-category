package com.musinsa

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest
class ShoppingCategoryApplicationTests {
    @Test
    fun contextLoads() {
        println("Test profile is active!")
    }
}
