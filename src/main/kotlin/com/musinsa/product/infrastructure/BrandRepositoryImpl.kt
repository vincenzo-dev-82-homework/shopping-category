package com.musinsa.product.infrastructure

import com.musinsa.product.domain.entity.Brand
import com.musinsa.product.domain.repository.BrandRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class BrandRepositoryImpl(
    private val brandJpaRepository: BrandJpaRepository,
) : BrandRepository {
    override fun findAll(): List<Brand> = brandJpaRepository.findAll()

    override fun findById(id: Long): Optional<Brand> = brandJpaRepository.findById(id)

    override fun save(brand: Brand): Brand = brandJpaRepository.save(brand)

    override fun delete(brand: Brand) {
        brandJpaRepository.save(brand)
    }
}
