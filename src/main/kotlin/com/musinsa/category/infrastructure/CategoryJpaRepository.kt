package com.musinsa.category.infrastructure

import com.musinsa.category.domain.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryJpaRepository : JpaRepository<Category, Long>
