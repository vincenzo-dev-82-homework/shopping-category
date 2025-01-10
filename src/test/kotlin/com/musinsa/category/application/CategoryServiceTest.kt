package com.musinsa.category.application

import com.musinsa.category.api.CategoryResources
import com.musinsa.category.domain.entity.Category
import com.musinsa.category.domain.repository.CategoryRepository
import com.musinsa.category.domain.service.CategoryDomainService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class CategoryServiceTest {
    private val categoryDomainService: CategoryDomainService = mockk()
    private val categoryRepository: CategoryRepository = mockk()

    private val categoryService = CategoryService(categoryDomainService, categoryRepository)

    @Test
    fun `getCategoryWithLowestPrice should return lowest prices and total price`() {
        // Given
        val categories =
            listOf(
                Category(id = 1L, name = "상의", type = mockk()),
                Category(id = 2L, name = "아우터", type = mockk()),
            )
        every { categoryRepository.findAll() } returns categories
        every { categoryDomainService.getLowestPriceForCategory(1L) } returns
            CategoryResources.CategoryProductDTO(
                categoryName = "상의",
                brandName = "Brand A",
                price = BigDecimal(10000),
            )
        every { categoryDomainService.getLowestPriceForCategory(2L) } returns
            CategoryResources.CategoryProductDTO(
                categoryName = "아우터",
                brandName = "Brand B",
                price = BigDecimal(12000),
            )

        // When
        val result = categoryService.getCategoryWithLowestPrice()

        // Then
        assertEquals(1, result.size)
        assertEquals(22000.toBigDecimal(), result[0].totalPrice)
        assertEquals(2, result[0].categories.size)
        assertEquals("상의", result[0].categories[0].categoryName)
        assertEquals("Brand A", result[0].categories[0].brandName)
        assertEquals(10000.toBigDecimal(), result[0].categories[0].price)
    }

    @Test
    fun `getCategoryPriceDetails should return lowest and highest price details`() {
        // Given
        val category = Category(id = 1L, name = "상의", type = mockk())
        every { categoryDomainService.findCategoryByName("상의") } returns category
        every { categoryDomainService.getLowestAndHighestPricesForCategory(1L) } returns
            Pair(
                CategoryResources.PriceDetailDTO(brandName = "Brand A", price = BigDecimal(10000)),
                CategoryResources.PriceDetailDTO(brandName = "Brand B", price = BigDecimal(20000)),
            )

        // When
        val result = categoryService.getCategoryPriceDetails("상의")

        // Then
        assertEquals("상의", result.categoryName)
        assertEquals("Brand A", result.lowestPrice[0].brandName)
        assertEquals(10000.toBigDecimal(), result.lowestPrice[0].price)
        assertEquals("Brand B", result.highestPrice[0].brandName)
        assertEquals(20000.toBigDecimal(), result.highestPrice[0].price)
    }

    @Test
    fun `getCategoryPriceDetails should throw exception if no products found`() {
        // Given
        val category = Category(id = 1L, name = "상의", type = mockk())
        every { categoryDomainService.findCategoryByName("상의") } returns category
        every { categoryDomainService.getLowestAndHighestPricesForCategory(1L) } returns Pair(null, null)

        // When & Then
        val exception =
            assertThrows<IllegalArgumentException> {
                categoryService.getCategoryPriceDetails("상의")
            }
        assertEquals("No products found for category '상의'", exception.message)
    }
}
