package com.musinsa.category.domain.service

import com.musinsa.category.api.CategoryResources
import com.musinsa.category.domain.repository.CategoryProductRepository
import com.musinsa.category.domain.repository.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryDomainService(
    private val categoryRepository: CategoryRepository,
    private val categoryProductRepository: CategoryProductRepository,
) {
    fun getCategoryPricesForBrand(brandId: Long): List<CategoryResources.CategoryPriceDTO> {
        val categories = categoryRepository.findAll()
        return categories.mapNotNull { category ->
            val lowestProduct = categoryProductRepository.findLowestPriceByBrandAndCategory(brandId, category.id!!)
            lowestProduct?.let {
                CategoryResources.CategoryPriceDTO(
                    categoryName = category.name,
                    price = it.product.price,
                )
            }
        }
    }

    fun countCategories(): Long = categoryRepository.count()
}
