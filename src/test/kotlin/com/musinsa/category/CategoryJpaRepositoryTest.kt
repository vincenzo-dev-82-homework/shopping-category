package com.musinsa.category

import com.musinsa.category.domain.Category
import com.musinsa.category.domain.CategoryType
import com.musinsa.category.infrastructure.CategoryJpaRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CategoryJpaRepositoryTest {
    @Autowired
    private lateinit var categoryJpaRepository: CategoryJpaRepository

    @Test
    fun `save and retrieve category`() {
        val category = Category.aDummy()
        val savedCategory = categoryJpaRepository.save(category)
        val findedCategory = categoryJpaRepository.findById(savedCategory.id!!)

        assertTrue(findedCategory.isPresent)
        assertEquals(CategoryType.OUTER.name, findedCategory.get().category.name)
    }
}
