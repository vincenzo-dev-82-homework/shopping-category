package com.musinsa.category.application

import com.musinsa.category.api.CategoryResources
import com.musinsa.category.domain.entity.Category
import com.musinsa.category.domain.repository.CategoryRepository
import com.musinsa.category.domain.service.CategoryDomainService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryDomainService: CategoryDomainService,
    private val categoryRepository: CategoryRepository,
) {
    @Transactional
    fun create(category: Category): Category = categoryRepository.save(category)

    @Transactional(readOnly = true)
    fun getCategoryWithLowestPrice(): List<CategoryResources.LowestPriceResponse> {
        val categories = categoryRepository.findAll()

        val categoryProducts =
            categories.mapNotNull { category ->
                categoryDomainService.getLowestPriceForCategory(category.id!!)
            }

        val totalPrice = categoryProducts.sumOf { it.price }

        return listOf(CategoryResources.LowestPriceResponse(categories = categoryProducts, totalPrice = totalPrice))
    }

    @Transactional(readOnly = true)
    fun getCategoryPriceDetails(categoryName: String): CategoryResources.PriceResponse {
        val category = categoryDomainService.findCategoryByName(categoryName)

        val (lowestPrice, highestPrice) = categoryDomainService.getLowestAndHighestPricesForCategory(category.id!!)

        if (lowestPrice == null || highestPrice == null) {
            throw IllegalArgumentException("No products found for category '$categoryName'")
        }

        return CategoryResources.PriceResponse(
            categoryName = category.name,
            lowestPrice = listOf(lowestPrice),
            highestPrice = listOf(highestPrice),
        )
    }
}
