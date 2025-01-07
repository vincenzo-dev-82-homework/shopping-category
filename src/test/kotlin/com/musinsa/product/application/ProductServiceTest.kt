package com.musinsa.product.application

import com.musinsa.product.api.model.ProductResources
import com.musinsa.product.domain.Brand
import com.musinsa.product.domain.BrandRepository
import com.musinsa.product.domain.Product
import com.musinsa.product.domain.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {
    private val brandRepository: BrandRepository = mock()
    private val productRepository: ProductRepository = mock()
    private val productService = ProductService(brandRepository, productRepository)

    @Test
    fun `create save and return product`() {
        // Given
        val brand = Brand(id = 1L, name = "나이스")
        val expected =
            ProductResources.CreateDTO(
                name = "나이스_샹의",
                price = BigDecimal(1000),
                brandId = 1L,
            )
        val product =
            Product(
                id = 1L,
                name = expected.name,
                price = expected.price,
                brand = brand,
            )
        whenever(brandRepository.findById(eq(1L))).thenReturn(Optional.of(brand))
        whenever(productRepository.save(any<Product>())).thenReturn(product)

        // When
        val actual = productService.create(expected)

        // Then
        assertEquals(expected.name, actual.name)
        assertEquals(expected.price, actual.price)
        verify(productRepository).save(any<Product>())
    }

    @Test
    fun `update modify and return updated product`() {
        // Given
        val brand = Brand(id = 1L, name = "나이스")
        val product =
            Product(
                id = 1L,
                name = "나이스_샹의",
                price = BigDecimal(10000),
                brand = brand,
            )
        val expected =
            ProductResources.UpdateDTO(
                id = 1L,
                name = "나이스_바지",
                price = BigDecimal(20000),
            )
        whenever(productRepository.findById(eq(1L))).thenReturn(Optional.of(product))
        whenever(productRepository.save(any<Product>())).thenReturn(product)

        // When
        val actual = productService.update(expected)

        // Then
        assertEquals(expected.name, actual.name)
        assertEquals(expected.price, actual.price)
        verify(productRepository).save(product)
    }

    @Test
    fun `findById throw exception if product not found`() {
        // Given
        whenever(productRepository.findById(eq(1L))).thenReturn(Optional.empty())

        // When & Then
        assertThrows<NoSuchElementException> {
            productService.findById(1L)
        }
    }
}
