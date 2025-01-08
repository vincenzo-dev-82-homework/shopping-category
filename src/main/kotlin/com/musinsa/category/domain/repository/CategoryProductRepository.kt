package com.musinsa.category.domain.repository

import com.musinsa.category.domain.entity.CategoryProduct

interface CategoryProductRepository {
    fun findLowestPriceByCategoryId(categoryId: Long): List<CategoryProduct>
}
