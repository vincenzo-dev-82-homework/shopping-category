package com.musinsa.product.infrastructure

import com.musinsa.product.domain.aDummy
import com.musinsa.product.domain.entity.Brand
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // DataJpaTest 내부 기본설정 때문에 profile 이 안 먹인다
@DataJpaTest
@Transactional
class BrandJpaRepositoryTest {
    @Autowired
    private lateinit var brandJpaRepository: BrandJpaRepository

    @DisplayName("브랜드를 생성한다.")
    @Test
    fun `save then find brand`() {
        val brand = Brand.aDummy()
        val savedBrand = brandJpaRepository.save(brand)
        val findedBrand = brandJpaRepository.findById(savedBrand.id!!)

        assertTrue(findedBrand.isPresent)
        assertEquals("A", findedBrand.get().name)
    }
}
