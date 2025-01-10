package com.musinsa.product.domain.repository

import com.musinsa.product.domain.entity.Product
import java.util.Optional

interface ProductRepository {
    fun findAll(): List<Product>

    fun findById(id: Long): Optional<Product>

    fun findByName(name: String): Optional<Product>

    fun save(product: Product): Product

    /**
     * Soft Delete
     */
    fun delete(product: Product)
}
