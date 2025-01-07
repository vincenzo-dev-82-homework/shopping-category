package com.musinsa.product.domain

interface BrandRepository {
    fun findAll(): List<Brand>

    fun findById(id: Long): Brand?

    fun save(brand: Brand): Brand

    fun delete(brand: Brand)
}
