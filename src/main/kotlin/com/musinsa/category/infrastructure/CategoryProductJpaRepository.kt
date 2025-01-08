package com.musinsa.category.infrastructure

import com.musinsa.category.domain.entity.CategoryProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CategoryProductJpaRepository : JpaRepository<CategoryProduct, Long> {
    @Query(
        """
    SELECT cp 
    FROM CategoryProduct cp
    JOIN FETCH cp.product p
    JOIN FETCH p.brand b
    WHERE cp.category.id = :categoryId
    ORDER BY p.price ASC
    """,
    )
    fun findLowestPriceByCategoryId(
        @Param("categoryId") categoryId: Long,
    ): List<CategoryProduct> // Optional에서 List로 변경
}
