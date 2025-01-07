package com.musinsa.product

import com.musinsa.product.domain.Brand
import com.musinsa.product.infrastructure.BrandJpaRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class BrandJpaRepositoryTest {
    @Autowired
    private lateinit var brandJpaRepository: BrandJpaRepository

    @DisplayName("브랜드를 생성하고 조회한다.")
    @Test
    fun `save then find brand`() {
        val brand = Brand.aDummy()
        val savedBrand = brandJpaRepository.save(brand)
        val findedBrand = brandJpaRepository.findById(savedBrand.id!!)

        assertTrue(findedBrand.isPresent)
        assertEquals("A", findedBrand.get().name)
    }
}
