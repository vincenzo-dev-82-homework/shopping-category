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
    fun findLowestPriceByCategories(
        @Param("categoryId") categoryId: Long,
    ): List<CategoryProduct> // Optional에서 List로 변경

    @Query(
        """
        SELECT cp 
        FROM CategoryProduct cp
        JOIN FETCH cp.product p
        WHERE p.brand.id = :brandId AND cp.category.id = :categoryId
        ORDER BY p.price ASC
        LIMIT 1
        """,
    )
    fun findLowestPriceByBrandAndCategory(
        @Param("brandId") brandId: Long,
        @Param("categoryId") categoryId: Long,
    ): CategoryProduct?
//
//    @Query(
//        """
//        SELECT cp
//        FROM CategoryProduct cp
//        JOIN FETCH cp.product p
//        JOIN FETCH p.brand b
//        WHERE cp.category.id = :categoryId
//        ORDER BY p.price ASC
//        """,
//    )
//    fun findLowestPriceByCategoryId(
//        @Param("categoryId") categoryId: Long,
//    ): Optional<CategoryProduct>
//
//    @Query(
//        """
//        SELECT cp
//        FROM CategoryProduct cp
//        JOIN FETCH cp.product p
//        JOIN FETCH p.brand b
//        WHERE cp.category.id = :categoryId
//        ORDER BY p.price DESC
//        """,
//    )
//    fun findHighestPriceByCategoryId(
//        @Param("categoryId") categoryId: Long,
//    ): Optional<CategoryProduct>

    @Query(
        value = """
    SELECT cp.id AS categoryProductId, p.id AS productId, p.price AS productPrice, b.name AS brandName
    FROM category_product cp
    JOIN product p ON cp.product_id = p.id
    JOIN brand b ON p.brand_id = b.id
    WHERE cp.category_id = :categoryId
    ORDER BY p.price ASC, b.name ASC
    LIMIT 1
    """,
        nativeQuery = true,
    )
    fun findLowestPriceByCategoryId(
        @Param("categoryId") categoryId: Long,
    ): Map<String, Any>

    @Query(
        value = """
    SELECT cp.id AS categoryProductId, p.id AS productId, p.price AS productPrice, b.name AS brandName
    FROM category_product cp
    JOIN product p ON cp.product_id = p.id
    JOIN brand b ON p.brand_id = b.id
    WHERE cp.category_id = :categoryId
    ORDER BY p.price DESC, b.name ASC
    LIMIT 1
    """,
        nativeQuery = true,
    )
    fun findHighestPriceByCategoryId(
        @Param("categoryId") categoryId: Long,
    ): Map<String, Any>
}
