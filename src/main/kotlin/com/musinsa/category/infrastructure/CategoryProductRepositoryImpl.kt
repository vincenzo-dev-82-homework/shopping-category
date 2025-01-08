package com.musinsa.category.infrastructure

import com.musinsa.category.domain.entity.CategoryProduct
import com.musinsa.category.domain.repository.CategoryProductRepository
import org.springframework.stereotype.Repository

@Repository
class CategoryProductRepositoryImpl(
    private val categoryProductJpaRepository: CategoryProductJpaRepository,
) : CategoryProductRepository {
    override fun findLowestPriceByCategories(categoryId: Long): List<CategoryProduct> =
        categoryProductJpaRepository.findLowestPriceByCategories(categoryId)

    override fun findLowestPriceByBrandAndCategory(
        brandId: Long,
        categoryId: Long,
    ): CategoryProduct? = categoryProductJpaRepository.findLowestPriceByBrandAndCategory(brandId, categoryId)

    override fun findLowestPriceByCategoryId(categoryId: Long): Map<String, Any>? =
        categoryProductJpaRepository.findLowestPriceByCategoryId(categoryId)

    override fun findHighestPriceByCategoryId(categoryId: Long): Map<String, Any>? =
        categoryProductJpaRepository.findHighestPriceByCategoryId(categoryId)
}
