package com.musinsa.product.application

import com.musinsa.common.exception.BrandAlreadyExistsException
import com.musinsa.common.exception.BrandNotFoundException
import com.musinsa.product.api.model.BrandResources
import com.musinsa.product.domain.entity.Brand
import com.musinsa.product.domain.repository.BrandRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BrandService(
    private val brandRepository: BrandRepository,
) {
    @Transactional
    fun create(request: BrandResources.RequestDTO): Brand {
        try {
            val brand =
                Brand(
                    name = request.name,
                    status = Brand.Status.ON,
                )
            return brandRepository.save(brand)
        } catch (ex: DataIntegrityViolationException) {
            throw BrandAlreadyExistsException("Brand Name ${request.name} Already found", request.name)
        }
    }

    @Transactional
    fun update(request: BrandResources.UpdateDTO): Brand {
        var brand =
            brandRepository
                .findById(request.id)
                .orElseThrow { BrandNotFoundException("Brand ID ${request.id} not found", request.id) }

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
                .orElseThrow { BrandNotFoundException("Brand ID ${request.id} not found", request.id) }

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
