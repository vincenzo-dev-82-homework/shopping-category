package com.musinsa.product.infrastructure

import com.musinsa.product.domain.entity.Product
import com.musinsa.product.domain.repository.ProductRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository,
) : ProductRepository {
    override fun findAll(): List<Product> = productJpaRepository.findAll()

    override fun findById(id: Long): Optional<Product> = productJpaRepository.findById(id)

    override fun save(product: Product): Product = productJpaRepository.save(product)

    /**
     * Soft Delete
     */
    override fun delete(product: Product) {
        productJpaRepository.save(product)
    }
}
