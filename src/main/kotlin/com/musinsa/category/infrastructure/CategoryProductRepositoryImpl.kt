package com.musinsa.category.infrastructure

import com.musinsa.category.domain.entity.CategoryProduct
import com.musinsa.category.domain.repository.CategoryProductRepository
import org.springframework.stereotype.Repository

@Repository
class CategoryProductRepositoryImpl(
    private val categoryProductJpaRepository: CategoryProductJpaRepository,
) : CategoryProductRepository {
    override fun findLowestPriceByCategoryId(categoryId: Long): List<CategoryProduct> =
        categoryProductJpaRepository.findLowestPriceByCategoryId(categoryId)
}
