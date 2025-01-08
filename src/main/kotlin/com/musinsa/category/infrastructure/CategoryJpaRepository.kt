package com.musinsa.category.infrastructure

import com.musinsa.category.domain.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface CategoryJpaRepository : JpaRepository<Category, Long> {
    fun findByName(name: String): Optional<Category>
}
