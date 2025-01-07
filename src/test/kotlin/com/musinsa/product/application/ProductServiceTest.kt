package com.musinsa.product.application

import com.musinsa.product.api.model.ProductResources
import com.musinsa.product.domain.Brand
import com.musinsa.product.domain.BrandRepository
import com.musinsa.product.domain.Product
import com.musinsa.product.domain.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.util.Optional

class ProductServiceTest {
    private val brandRepository: BrandRepository = mock()
    private val productRepository: ProductRepository = mock()
    private val productService = ProductService(brandRepository, productRepository)

    @Test
    fun `create should save and return product`() {
        // Given
        val brand = Brand(id = 1L, name = "BrandA")
        val request =
            ProductResources.CreateDTO(
                name = "ProductA",
                price = BigDecimal(1000),
                brandId = 1L,
            )
        val product =
            Product(
                id = 1L,
                name = request.name,
                price = request.price,
                brand = brand,
            )
        whenever(brandRepository.findById(1L)).thenReturn(Optional.of(brand))
        whenever(productRepository.save(any())).thenReturn(product)

        // When
        val result = productService.create(request)

        // Then
        assertEquals(request.name, result.name)
        assertEquals(request.price, result.price)
        verify(productRepository).save(any())
    }

    @Test
    fun `update should modify and return updated product`() {
        // Given
        val brand = Brand(id = 1L, name = "BrandA")
        val product =
            Product(
                id = 1L,
                name = "OldName",
                price = BigDecimal(500),
                brand = brand,
            )
        val request =
            ProductResources.UpdateDTO(
                id = 1L,
                name = "NewName",
                price = BigDecimal(1500),
            )
        whenever(productRepository.findById(1L)).thenReturn(Optional.of(product))
        whenever(productRepository.save(any())).thenReturn(product)

        // When
        val result = productService.update(request)

        // Then
        assertEquals(request.name, result.name)
        assertEquals(request.price, result.price)
        verify(productRepository).save(product)
    }

    @Test
    fun `findById should throw exception if product not found`() {
        // Given
        whenever(productRepository.findById(1L)).thenReturn(Optional.empty())

        // When & Then
        assertThrows<NoSuchElementException> {
            productService.findById(1L)
        }
    }
}
