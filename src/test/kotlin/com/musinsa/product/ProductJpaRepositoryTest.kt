package com.musinsa.product

import com.musinsa.product.domain.Brand
import com.musinsa.product.domain.Product
import com.musinsa.product.infrastructure.BrandJpaRepository
import com.musinsa.product.infrastructure.ProductJpaRepository
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
class ProductJpaRepositoryTest {
    @Autowired
    private lateinit var brandJpaRepository: BrandJpaRepository

    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository

    @DisplayName("상품을 생성하고 조회한다.")
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
