package com.musinsa.product.application

import com.musinsa.product.domain.Brand
import com.musinsa.product.domain.BrandRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BrandService(
    private val brandRepository: BrandRepository,
) {
    @Transactional
    fun create(brand: Brand): Brand = brandRepository.save(brand)

    @Transactional
    fun update(brand: Brand): Brand = brandRepository.save(brand)

    @Transactional
    fun delete(brand: Brand) {
        brand.off()
        brandRepository.delete(brand)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<Brand> = brandRepository.findAll()
}
