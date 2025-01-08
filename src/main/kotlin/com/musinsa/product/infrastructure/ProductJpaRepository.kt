package com.musinsa.product.infrastructure

import com.musinsa.product.domain.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : JpaRepository<Product, Long>
