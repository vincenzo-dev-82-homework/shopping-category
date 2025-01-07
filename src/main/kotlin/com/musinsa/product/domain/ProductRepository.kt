package com.musinsa.product.domain

interface ProductRepository {
    fun findAll(): List<Product>

    fun findById(id: Long): Product?

    fun save(product: Product): Product

    fun delete(product: Product)
}
