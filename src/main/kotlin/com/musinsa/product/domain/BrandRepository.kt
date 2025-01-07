package com.musinsa.product.domain

import java.util.Optional

interface BrandRepository {
    fun findAll(): List<Brand>

    fun findById(id: Long): Optional<Brand>

    fun save(brand: Brand): Brand

    fun delete(brand: Brand)
}
