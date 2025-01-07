package com.musinsa.product.infrastructure

import com.musinsa.product.domain.Brand
import com.musinsa.product.domain.BrandRepository
import org.springframework.stereotype.Repository

@Repository
class BrandRepositoryImpl(
    private val brandJpaRepository: BrandJpaRepository,
) : BrandRepository {
    override fun findAll(): List<Brand> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): Brand? {
        TODO("Not yet implemented")
    }

    override fun save(brand: Brand): Brand {
        TODO("Not yet implemented")
    }

    override fun delete(brand: Brand) {
        TODO("Not yet implemented")
    }
}
