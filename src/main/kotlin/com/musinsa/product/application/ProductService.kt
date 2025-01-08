package com.musinsa.product.application

import com.musinsa.common.exception.BrandNotFoundException
import com.musinsa.common.exception.ProductAlreadyExistsException
import com.musinsa.common.exception.ProductNotFoundException
import com.musinsa.product.api.model.ProductResources
import com.musinsa.product.domain.entity.Product
import com.musinsa.product.domain.repository.BrandRepository
import com.musinsa.product.domain.repository.ProductRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository,
) {
    @Transactional
    fun create(request: ProductResources.CreateDTO): Product {
        val brand =
            brandRepository
                .findById(request.brandId)
                .orElseThrow { BrandNotFoundException("Brand ${request.brandId} not found", request.brandId) }
        try {
            var product =
                Product(
                    name = request.name,
                    price = request.price,
                    brand = brand,
                )
            return productRepository.save(product)
        } catch (ex: DataIntegrityViolationException) {
            throw ProductAlreadyExistsException("Product Name ${request.name} Already found", request.name)
        }
    }

    @Transactional
    fun update(request: ProductResources.UpdateDTO): Product {
        var product =
            productRepository
                .findById(request.id)
                .orElseThrow { ProductNotFoundException("Product ID ${request.id} not found", request.id) }

        request.name.let { product.name = it }
        request.price.let { product.price = it }

        return productRepository.save(product)
    }

    @Transactional
    fun delete(request: ProductResources.DeleteDTO) {
        var product =
            productRepository
                .findById(request.id)
                .orElseThrow { ProductNotFoundException("Product ID ${request.id} not found", request.id) }

        // TODO check 이미 종료된 것인지 확인

        product.terminate()
        productRepository.save(product)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<ProductResources.ResponseDTO> {
        val products = productRepository.findAll()
        return ProductResources.ResponseDTO.toResponse(products)
    }

    @Transactional(readOnly = true)
    fun findById(productId: Long): ProductResources.ResponseDTO {
        val product = productRepository.findById(productId).get()
        return ProductResources.ResponseDTO.toResponse(product)
    }
}
