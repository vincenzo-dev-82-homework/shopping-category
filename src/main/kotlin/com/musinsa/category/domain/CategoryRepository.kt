package com.musinsa.category.domain

interface CategoryRepository {
    fun findAll(): List<Category>

    fun findById(id: Long): Category?

    fun save(category: Category): Category

    fun delete(category: Category)
}
