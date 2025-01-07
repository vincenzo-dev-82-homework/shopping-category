package com.musinsa.product.infrastructure

import com.musinsa.product.domain.Product
import com.musinsa.product.domain.ProductRepository
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository,
) : ProductRepository {
    override fun findAll(): List<Product> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): Product? {
        TODO("Not yet implemented")
    }

    override fun save(product: Product): Product {
        TODO("Not yet implemented")
    }

    override fun delete(product: Product) {
        TODO("Not yet implemented")
    }
}
