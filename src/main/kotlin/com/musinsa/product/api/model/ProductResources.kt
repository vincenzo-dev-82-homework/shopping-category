package com.musinsa.product.api.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.musinsa.product.domain.entity.Product
import org.springframework.hateoas.RepresentationModel
import java.math.BigDecimal
import java.time.LocalDateTime

class ProductResources {
    data class CreateDTO(
        val id: Long? = null, // 상품 ID
        val name: String, // 상품 이름
        val price: BigDecimal, // 상품 가격
        val status: String? = null, // 상품 상태
        val brandId: Long, // 브랜드 ID
    )

    data class UpdateDTO(
        val id: Long, // 상품 ID
        val name: String, // 상품 이름
        val price: BigDecimal, // 상품 가격
        val status: String? = null, // 상품 상태
    )

    data class DeleteDTO(
        val id: Long, // 상품 ID
    )

    data class ResponseModel(
        val id: Long, // 상품 ID
    ) : RepresentationModel<ResponseModel>()

    data class ResponseDTO(
        val id: Long, // 상품 ID
        val name: String, // 상품 이름
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        val createdDateTime: LocalDateTime?, // 상품 생성 시간
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        val updateDateTime: LocalDateTime?, // 상품 수정 시간
    ) {
        companion object {
            fun toResponse(product: Product): ResponseDTO =
                ResponseDTO(
                    id = product.id!!,
                    name = product.name,
                    createdDateTime = product.createdAt,
                    updateDateTime = product.modifiedAt,
                )

            fun toResponse(products: List<Product>): List<ResponseDTO> =
                products.map { brand ->
                    ResponseDTO(
                        id = brand.id!!,
                        name = brand.name,
                        createdDateTime = brand.createdAt,
                        updateDateTime = brand.modifiedAt,
                    )
                }
        }
    }
}
