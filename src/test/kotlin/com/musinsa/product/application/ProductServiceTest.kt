package com.musinsa.product.application

import com.musinsa.common.exception.ProductNotFoundException
import com.musinsa.product.api.model.ProductResources
import com.musinsa.product.domain.entity.Brand
import com.musinsa.product.domain.entity.Product
import com.musinsa.product.domain.repository.BrandRepository
import com.musinsa.product.domain.repository.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
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
        val productId = 1L
        val originalProduct =
            Product(
                id = productId,
                name = "나이스_샹의",
                price = BigDecimal("10000"),
                brand = Brand(id = 1L, name = "나이스", status = Brand.Status.ON),
            )
        val updateDTO =
            ProductResources.UpdateDTO(
                id = productId,
                name = "나이스_바지",
                price = BigDecimal("20000"),
            )

        whenever(productRepository.findById(productId)).thenReturn(Optional.of(originalProduct))
        whenever(productRepository.save(any())).thenAnswer { it.arguments[0] as Product }

        // When
        val updatedProduct = productService.update(updateDTO)

        // Then
        verify(productRepository).findById(productId)
        verify(productRepository).save(updatedProduct)

        assertEquals("나이스_바지", updatedProduct.name)
        assertEquals(BigDecimal("20000"), updatedProduct.price)
    }

    @Test
    fun `update throw exception if product not found`() {
        // Given
        val productId = 1L
        val updateDTO =
            ProductResources.UpdateDTO(
                id = productId,
                name = "나이스_바지",
                price = BigDecimal("20000"),
            )

        whenever(productRepository.findById(productId)).thenReturn(Optional.empty())

        // When & Then
        val exception =
            assertThrows<ProductNotFoundException> {
                productService.update(updateDTO)
            }

        assertEquals("Product ID $productId not found", exception.message)
        verify(productRepository).findById(productId)
    }

    @Test
    fun `delete terminate and save product`() {
        // Given
        val productId = 1L
        val originalProduct =
            Product(
                id = productId,
                name = "나이스_샹의",
                price = BigDecimal("10000"),
                brand = Brand(id = 1L, name = "나이스", status = Brand.Status.ON),
            )
        val deleteDTO = ProductResources.DeleteDTO(id = productId)

        whenever(productRepository.findById(productId)).thenReturn(Optional.of(originalProduct))
        whenever(productRepository.save(any())).thenAnswer { it.arguments[0] as Product }

        // When
        productService.delete(deleteDTO)

        // Then
        verify(productRepository).findById(productId)
        verify(productRepository).save(originalProduct)

        assertTrue(originalProduct.isTerminated())
    }

    @Test
    fun `delete throw exception if product not found`() {
        // Given
        val productId = 1L
        val deleteDTO = ProductResources.DeleteDTO(id = productId)

        whenever(productRepository.findById(productId)).thenReturn(Optional.empty())

        // When & Then
        val exception =
            assertThrows<ProductNotFoundException> {
                productService.delete(deleteDTO)
            }

        assertEquals("Product ID $productId not found", exception.message)
        verify(productRepository).findById(productId)
        verify(productRepository, never()).save(any())
    }

    @Test
    fun `findById should return product response DTO`() {
        // Given
        val productId = 1L
        val product =
            Product(
                id = productId,
                name = "나이스_샹의",
                price = BigDecimal("10000"),
                brand = Brand(id = 1L, name = "나이스", status = Brand.Status.ON),
            )
        whenever(productRepository.findById(productId)).thenReturn(Optional.of(product))

        // When
        val response = productService.findById(productId)

        // Then
        assertNotNull(response)
        assertEquals(productId, response.id)
        verify(productRepository).findById(productId)
    }
}
