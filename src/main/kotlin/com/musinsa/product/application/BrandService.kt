package com.musinsa.product.application

import com.musinsa.product.api.model.BrandResources
import com.musinsa.product.domain.Brand
import com.musinsa.product.domain.BrandRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BrandService(
    private val brandRepository: BrandRepository,
) {
    @Transactional
    fun create(request: BrandResources.RequestDTO): Brand {
        val brand =
            Brand(
                name = request.name,
                status = Brand.Status.ON,
            )
        return brandRepository.save(brand)
    }

    @Transactional
    fun update(request: BrandResources.UpdateDTO): Brand {
        var brand =
            brandRepository
                .findById(request.id)
                .orElseThrow { IllegalArgumentException("Brand ID ${request.id} not found") }

        request.name.let { brand.name = it }
        request.status.let {
            val status =
                Brand.Status.fromDesc(it)
                    ?: throw IllegalArgumentException("Invalid status: $it")
            brand.status = status
        }
        return brandRepository.save(brand)
    }

    @Transactional
    fun delete(request: BrandResources.DeleteDTO) {
        var brand =
            brandRepository
                .findById(request.id)
                .orElseThrow { IllegalArgumentException("Brand ID ${request.id} not found") }

        brand.terminate()
        brandRepository.delete(brand)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<BrandResources.ResponseDTO> {
        val brands = brandRepository.findAll()
        return BrandResources.ResponseDTO.toResponse(brands)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): BrandResources.ResponseDTO {
        val brand = brandRepository.findById(id).get()
        return BrandResources.ResponseDTO.toResponse(brand)
    }
}
