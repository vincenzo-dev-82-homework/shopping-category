package com.musinsa.product.infrastructure

import com.musinsa.product.domain.Brand
import com.musinsa.product.domain.BrandRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class BrandRepositoryImpl(
    private val brandJpaRepository: BrandJpaRepository,
) : BrandRepository {
    override fun findAll(): List<Brand> = brandJpaRepository.findAll()

    override fun findById(id: Long): Brand? = brandJpaRepository.findByIdOrNull(id)

    override fun save(brand: Brand): Brand = brandJpaRepository.save(brand)

    override fun delete(brand: Brand) {
        brandJpaRepository.save(brand)
    }
}
