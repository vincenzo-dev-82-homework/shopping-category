package com.musinsa.category.domain.repository

import com.musinsa.category.domain.entity.CategoryProduct

interface CategoryProductRepository {
    fun findLowestPriceByCategories(categoryId: Long): List<CategoryProduct>

    fun findLowestPriceByBrandAndCategory(
        brandId: Long,
        categoryId: Long,
    ): CategoryProduct?

//    fun findLowestPriceByCategoryId(categoryId: Long): Optional<CategoryProduct>
//
//    fun findHighestPriceByCategoryId(categoryId: Long): Optional<CategoryProduct>

    fun findLowestPriceByCategoryId(categoryId: Long): Map<String, Any>?

    fun findHighestPriceByCategoryId(categoryId: Long): Map<String, Any>?
}
