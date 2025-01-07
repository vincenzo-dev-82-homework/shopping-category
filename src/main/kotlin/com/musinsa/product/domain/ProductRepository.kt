package com.musinsa.product.domain

import java.util.Optional

interface ProductRepository {
    fun findAll(): List<Product>

    fun findById(id: Long): Optional<Product>

    fun save(product: Product): Product

    /**
     * Soft Delete
     */
    fun delete(product: Product)
}
