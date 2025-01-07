package com.musinsa.product.infrastructure

import com.musinsa.product.domain.Brand
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandJpaRepository : JpaRepository<Brand, Long>
