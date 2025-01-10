package com.musinsa.category.domain

import com.musinsa.category.domain.entity.Category
import com.musinsa.category.domain.entity.CategoryProduct
import com.musinsa.category.domain.repository.CategoryProductRepository
import com.musinsa.category.domain.repository.CategoryRepository
import com.musinsa.category.domain.service.CategoryDomainService
import com.musinsa.common.exception.CategoryNotFoundException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class CategoryDomainServiceTest {
    private val categoryRepository: CategoryRepository = mockk()
    private val categoryProductRepository: CategoryProductRepository = mockk()

    private val categoryDomainService = CategoryDomainService(categoryRepository, categoryProductRepository)

    @Test
    fun `getLowestPriceForCategory return lowest price for a category`() {
        // Given
        val categoryId = 1L
        val category = Category(id = categoryId, name = "상의", type = mockk())
        val product =
            mockk<CategoryProduct> {
                every { this@mockk.category.name } returns "상의"
                every { product.brand.name } returns "Brand A"
                every { product.price } returns BigDecimal(10000)
            }
        every { categoryProductRepository.findLowestPriceByCategories(categoryId) } returns listOf(product)

        // When
        val result = categoryDomainService.getLowestPriceForCategory(categoryId)

        // Then
        assertEquals("상의", result?.categoryName)
        assertEquals("Brand A", result?.brandName)
        assertEquals(BigDecimal(10000), result?.price)
    }

    @Test
    fun `getLowestPriceForCategory return null if no products found`() {
        // Given
        val categoryId = 1L
        every { categoryProductRepository.findLowestPriceByCategories(categoryId) } returns emptyList()

        // When
        val result = categoryDomainService.getLowestPriceForCategory(categoryId)

        // Then
        assertNull(result)
    }

    @Test
    fun `getLowestAndHighestPricesForCategory return lowest and highest prices`() {
        // Given
        val categoryId = 1L
        val lowestProduct = mapOf("brandName" to "Brand A", "productPrice" to BigDecimal(10000))
        val highestProduct = mapOf("brandName" to "Brand B", "productPrice" to BigDecimal(20000))
        every { categoryProductRepository.findLowestPriceByCategoryId(categoryId) } returns lowestProduct
        every { categoryProductRepository.findHighestPriceByCategoryId(categoryId) } returns highestProduct

        // When
        val (lowestPrice, highestPrice) = categoryDomainService.getLowestAndHighestPricesForCategory(categoryId)

        // Then
        assertEquals("Brand A", lowestPrice?.brandName)
        assertEquals(BigDecimal(10000), lowestPrice?.price)
        assertEquals("Brand B", highestPrice?.brandName)
        assertEquals(BigDecimal(20000), highestPrice?.price)
    }

    @Test
    fun `findCategoryByName return category if found`() {
        // Given
        val categoryName = "상의"
        val category = Category(id = 1L, name = categoryName, type = mockk())
        every { categoryRepository.findByName(categoryName) } returns category

        // When
        val result = categoryDomainService.findCategoryByName(categoryName)

        // Then
        assertEquals(categoryName, result.name)
    }

    @Test
    fun `findCategoryByName throw exception if category not found`() {
        // Given
        val categoryName = "상의"
        every { categoryRepository.findByName(categoryName) } returns null

        // When & Then
        val exception =
            assertThrows<CategoryNotFoundException> {
                categoryDomainService.findCategoryByName(categoryName)
            }
        assertEquals("CategoryName $categoryName not found", exception.message)
    }

    @Test
    fun `getCategoryPricesForBrand return category prices for a brand`() {
        // Given
        val brandId = 1L
        val category = Category(id = 1L, name = "상의", type = mockk())
        val product =
            mockk<CategoryProduct> {
                every { this@mockk.category.name } returns "상의"
                every { product.price } returns BigDecimal(10000)
            }
        every { categoryRepository.findAll() } returns listOf(category)
        every { categoryProductRepository.findLowestPriceByBrandAndCategory(brandId, category.id!!) } returns product

        // When
        val result = categoryDomainService.getCategoryPricesForBrand(brandId)

        // Then
        assertEquals(1, result.size)
        assertEquals("상의", result[0].categoryName)
        assertEquals(BigDecimal(10000), result[0].price)
    }

    @Test
    fun `countCategories return the count of categories`() {
        // Given
        every { categoryRepository.count() } returns 5L

        // When
        val result = categoryDomainService.countCategories()

        // Then
        assertEquals(5L, result)
    }
}
