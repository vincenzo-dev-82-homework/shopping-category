package com.musinsa.product.infrastructure

import com.musinsa.product.domain.Brand
import com.musinsa.product.domain.Product
import com.musinsa.product.domain.aDummy
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
class ProductJpaRepositoryTest {
    @Autowired
    private lateinit var brandJpaRepository: BrandJpaRepository

    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository

    @DisplayName("상품을 생성한다.")
    @Test
    fun `save then find product`() {
        val brand = Brand.aDummy()
        val savedBrand = brandJpaRepository.save(brand)

        val product = Product.aDummy(savedBrand)
        val saved = productJpaRepository.save(product)
        val finded = productJpaRepository.findById(saved.id!!)

        assertTrue(finded.isPresent)
        assertEquals(product.name, finded.get().name)
        assertEquals(brand.name, finded.get().brand.name)
    }
}
