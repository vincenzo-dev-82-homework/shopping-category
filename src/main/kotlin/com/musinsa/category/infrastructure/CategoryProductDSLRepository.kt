package com.musinsa.category.infrastructure

import com.musinsa.category.domain.entity.CategoryProduct
import com.musinsa.category.domain.entity.QCategoryProduct.categoryProduct
import com.musinsa.product.domain.entity.QBrand.brand
import com.musinsa.product.domain.entity.QProduct.product
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CategoryProductDSLRepository : QuerydslRepositorySupport(CategoryProduct::class.java) {
    fun findLowestPriceByCategories(categoryId: Long): List<CategoryProduct> =
        from(categoryProduct)
            .join(categoryProduct.product(), product)
            .fetchJoin()
            .join(product.brand(), brand)
            .fetchJoin()
            .where(categoryProduct.category().id.eq(categoryId))
            .orderBy(product.price.asc())
            .select(categoryProduct)
            .fetch()

    fun findLowestPriceByBrandAndCategory(
        brandId: Long,
        categoryId: Long,
    ): CategoryProduct? =
        from(categoryProduct)
            .join(categoryProduct.product(), product)
            .fetchJoin()
            .where(
                product
                    .brand()
                    .id
                    .eq(brandId)
                    .and(categoryProduct.category().id.eq(categoryId)),
            ).orderBy(product.price.asc())
            .select(categoryProduct)
            .fetchFirst()

    fun findLowestPriceByCategoryId(categoryId: Long): Map<String, Any>? =
        from(categoryProduct)
            .join(categoryProduct.product(), product)
            .join(product.brand(), brand)
            .where(categoryProduct.category().id.eq(categoryId))
            .orderBy(product.price.asc(), brand.name.asc())
            .select(
                categoryProduct.id.`as`("categoryProductId"),
                product.id.`as`("productId"),
                product.price.`as`("productPrice"),
                brand.name.`as`("brandName"),
            ).fetchFirst()
            ?.toArray()
            ?.let {
                mapOf(
                    "categoryProductId" to it[0],
                    "productId" to it[1],
                    "productPrice" to it[2],
                    "brandName" to it[3],
                )
            }

    fun findHighestPriceByCategoryId(categoryId: Long): Map<String, Any>? =
        from(categoryProduct)
            .join(categoryProduct.product(), product)
            .join(product.brand(), brand)
            .where(categoryProduct.category().id.eq(categoryId))
            .orderBy(product.price.desc(), brand.name.asc())
            .select(
                categoryProduct.id.`as`("categoryProductId"),
                product.id.`as`("productId"),
                product.price.`as`("productPrice"),
                brand.name.`as`("brandName"),
            ).fetchFirst()
            ?.toArray()
            ?.let {
                mapOf(
                    "categoryProductId" to it[0],
                    "productId" to it[1],
                    "productPrice" to it[2],
                    "brandName" to it[3],
                )
            }
}
