package com.musinsa.category.api

import org.springframework.hateoas.RepresentationModel
import java.math.BigDecimal

class CategoryResources {
    data class CreateDTO(
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

    /**
     * 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API 응답
     */
    data class LowestPriceResponse(
        val categories: List<CategoryProductDTO>, // 카테고리별 정보 리스트
        val totalPrice: BigDecimal, // 총합 가격
    )

    data class CategoryProductDTO(
        val categoryName: String, // 카테고리 이름
        val brandName: String, // 브랜드 이름
        val price: BigDecimal, // 가격
    ) {
        companion object {
            fun toResponse(
                categoryName: String,
                brandName: String,
                price: BigDecimal,
            ): CategoryProductDTO =
                CategoryProductDTO(
                    categoryName = categoryName,
                    brandName = brandName,
                    price = price,
                )

            fun toResponse(categories: List<Pair<String, Pair<String, BigDecimal>>>): List<CategoryProductDTO> =
                categories.map { (categoryName, brandInfo) ->
                    CategoryProductDTO(
                        categoryName = categoryName,
                        brandName = brandInfo.first,
                        price = brandInfo.second,
                    )
                }
        }
    }

    companion object {
        fun from(categories: List<CategoryProductDTO>): LowestPriceResponse {
            val totalPrice = categories.sumOf { it.price }
            return LowestPriceResponse(categories = categories, totalPrice = totalPrice)
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

    data class CategoryPriceDTO(
        val categoryName: String, // 카테고리 이름
        val price: BigDecimal, // 가격
    )

    /**
     * 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회 응답
     */
    data class PriceResponse(
        val categoryName: String, // 카테고리 이름
        val lowestPrice: List<PriceDetailDTO>, // 최저가 브랜드 및 가격
        val highestPrice: List<PriceDetailDTO>, // 최고가 브랜드 및 가격
    )

    data class PriceDetailDTO(
        val brandName: String, // 브랜드 이름
        val price: BigDecimal, // 가격
    )
}
