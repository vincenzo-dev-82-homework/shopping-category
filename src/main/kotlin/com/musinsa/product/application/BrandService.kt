package com.musinsa.product.application

import com.musinsa.category.api.CategoryResources
import com.musinsa.category.domain.repository.CategoryProductRepository
import com.musinsa.category.domain.repository.CategoryRepository
import com.musinsa.common.exception.BrandAlreadyExistsException
import com.musinsa.common.exception.BrandNotFoundException
import com.musinsa.product.api.model.BrandResources
import com.musinsa.product.domain.entity.Brand
import com.musinsa.product.domain.repository.BrandRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

/**
 * ApplicationService는 비즈니스의 흐름과 트랜잭션을 관리한다.
 */
@Service
class BrandService(
    private val brandRepository: BrandRepository,
    private val categoryRepository: CategoryRepository,
    private val categoryProductRepository: CategoryProductRepository,
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

    @Transactional(readOnly = true)
    fun getLowestPriceBrand(): BrandResources.LowestPriceBrandResponse {
        // 모든 브랜드를 조회
        val brands = brandRepository.findAll()

        // 각 브랜드에서 모든 카테고리의 최저 가격 상품을 계산
        val brandTotals =
            brands.mapNotNull { brand ->
                val categoryPrices =
                    categoryRepository.findAll().mapNotNull { category ->
                        val lowestProduct = categoryProductRepository.findLowestPriceByBrandAndCategory(brand.id!!, category.id!!)
                        lowestProduct?.let {
                            CategoryResources.CategoryPriceDTO(
                                categoryName = category.name,
                                price = it.product.price,
                            )
                        }
                    }

                if (categoryPrices.size.toLong() == categoryRepository.count()) {
                    // 모든 카테고리의 상품이 존재하는 브랜드만 포함
                    val totalPrice = categoryPrices.sumOf { it.price }
                    BrandTotalPrice(brandName = brand.name, categoryPrices = categoryPrices, totalPrice = totalPrice)
                } else {
                    null
                }
            }

        // 최저가 브랜드를 선택
        val lowestPriceBrand =
            brandTotals.minByOrNull { it.totalPrice }
                ?: throw IllegalArgumentException("No brand meets the criteria for lowest price")

        // 최종 응답 생성
        return BrandResources.LowestPriceBrandResponse(
            brandName = lowestPriceBrand.brandName,
            categoryPrices = lowestPriceBrand.categoryPrices,
            totalPrice = lowestPriceBrand.totalPrice,
        )
    }

    data class BrandTotalPrice(
        val brandName: String,
        val categoryPrices: List<CategoryResources.CategoryPriceDTO>,
        val totalPrice: BigDecimal,
    )
}
