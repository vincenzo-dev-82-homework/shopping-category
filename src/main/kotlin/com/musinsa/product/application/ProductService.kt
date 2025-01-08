package com.musinsa.product.application

import com.musinsa.product.api.model.ProductResources
import com.musinsa.product.domain.BrandRepository
import com.musinsa.product.domain.Product
import com.musinsa.product.domain.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository,
) {
    @Transactional
    fun create(request: ProductResources.CreateDTO): Product {
        val brand = brandRepository.findById(request.brandId).get()
        var product =
            Product(
                name = request.name,
                price = request.price,
                brand = brand,
            )
        return productRepository.save(product)
    }

    @Transactional
    fun update(request: ProductResources.UpdateDTO): Product {
        var product =
            productRepository
                .findById(request.id)
                .orElseThrow { IllegalArgumentException("Brand ID ${request.id} not found") }

        request.name.let { product.name = it }
        request.price.let { product.price = it }

        return productRepository.save(product)
    }

    @Transactional
    fun delete(request: ProductResources.DeleteDTO) {
        var product =
            productRepository
                .findById(request.id)
                .orElseThrow { IllegalArgumentException("Brand ID ${request.id} not found") }

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
