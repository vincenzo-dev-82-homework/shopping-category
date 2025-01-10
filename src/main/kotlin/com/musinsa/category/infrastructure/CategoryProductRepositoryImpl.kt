package com.musinsa.category.infrastructure

import com.musinsa.category.domain.entity.CategoryProduct
import com.musinsa.category.domain.repository.CategoryProductRepository
import org.springframework.stereotype.Repository

@Repository
class CategoryProductRepositoryImpl(
    private val categoryProductJpaRepository: CategoryProductJpaRepository,
    private val categoryProductDSLRepository: CategoryProductDSLRepository,
) : CategoryProductRepository {
    override fun findLowestPriceByCategories(categoryId: Long): List<CategoryProduct> =
        categoryProductDSLRepository.findLowestPriceByCategories(categoryId)

    override fun findLowestPriceByBrandAndCategory(
        brandId: Long,
        categoryId: Long,
    ): CategoryProduct? = categoryProductDSLRepository.findLowestPriceByBrandAndCategory(brandId, categoryId)

    override fun findLowestPriceByCategoryId(categoryId: Long): Map<String, Any>? =
        categoryProductDSLRepository.findLowestPriceByCategoryId(categoryId)

    override fun findHighestPriceByCategoryId(categoryId: Long): Map<String, Any>? =
        categoryProductDSLRepository.findHighestPriceByCategoryId(categoryId)

    override fun save(categoryProduct: CategoryProduct): CategoryProduct = categoryProductJpaRepository.save(categoryProduct)

    override fun count(): Long = categoryProductJpaRepository.count()
}
