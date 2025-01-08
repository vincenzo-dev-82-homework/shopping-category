package com.musinsa.category.domain.repository

import com.musinsa.category.domain.entity.Category

interface CategoryRepository {
    fun findAll(): List<Category>

    fun findById(id: Long): Category?

    fun save(category: Category): Category

    fun delete(category: Category)
}
