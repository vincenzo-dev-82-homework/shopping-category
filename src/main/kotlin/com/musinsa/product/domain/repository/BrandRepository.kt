package com.musinsa.product.domain.repository

import com.musinsa.product.domain.entity.Brand
import java.util.Optional

interface BrandRepository {
    fun findAll(): List<Brand>

    fun findById(id: Long): Optional<Brand>

    fun save(brand: Brand): Brand

    fun delete(brand: Brand)
}
