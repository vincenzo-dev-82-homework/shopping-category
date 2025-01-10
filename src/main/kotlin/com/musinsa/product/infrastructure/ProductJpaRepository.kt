package com.musinsa.product.infrastructure

import com.musinsa.product.domain.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ProductJpaRepository : JpaRepository<Product, Long> {
    fun findByName(name: String): Optional<Product>
}
