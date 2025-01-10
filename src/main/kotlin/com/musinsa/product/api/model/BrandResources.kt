package com.musinsa.product.api.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.musinsa.category.api.CategoryResources.CategoryPriceDTO
import com.musinsa.product.domain.entity.Brand
import org.springframework.hateoas.RepresentationModel
import java.math.BigDecimal
import java.time.LocalDateTime

class BrandResources {
    data class RequestDTO(
        val name: String, // 브랜드 이름
    )

    data class UpdateDTO(
        val id: Long, // 브랜드 ID
        val name: String, // 브랜드 이름
        val status: String, // 브랜드 상태
    )

    data class DeleteDTO(
        val id: Long, // 브랜드 ID
    )

    data class ResponseModel(
        val id: Long, // 광고 ID
    ) : RepresentationModel<ResponseModel>()

    data class ResponseDTO(
        val id: Long, // 브랜드 ID
        val name: String, // 브랜드 이름
        val status: String, // 브랜드 상태
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        val createdDateTime: LocalDateTime?, // 브랜드 생성 시간
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        val updateDateTime: LocalDateTime?, // 브랜드 수정 시간
    ) {
        companion object {
            fun toResponse(brand: Brand): ResponseDTO =
                ResponseDTO(
                    id = brand.id!!,
                    name = brand.name,
                    status = brand.status.name,
                    createdDateTime = brand.createdAt,
                    updateDateTime = brand.modifiedAt,
                )

            fun toResponse(brands: List<Brand>): List<ResponseDTO> =
                brands.map { brand ->
                    ResponseDTO(
                        id = brand.id!!,
                        name = brand.name,
                        status = brand.status.name,
                        createdDateTime = brand.createdAt,
                        updateDateTime = brand.modifiedAt,
                    )
                }
        }
    }

    /**
     * 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가 정보 응답
     */
    data class LowestPriceBrandResponse(
        val brandName: String, // 최저가 브랜드 이름
        val categoryPrices: List<CategoryPriceDTO>, // 카테고리별 상품 가격 정보
        val totalPrice: BigDecimal, // 총합 가격
    )
}
