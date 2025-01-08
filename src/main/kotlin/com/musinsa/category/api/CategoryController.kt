package com.musinsa.category.api

import com.musinsa.category.application.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/musinsa/categories")
class CategoryController(
    private val categoryService: CategoryService,
) {
    /**
     * 구현 1) - 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
     */
    @GetMapping("/lowest-price")
    fun getCategoryLowestPrice(): ResponseEntity<List<CategoryResources.LowestPriceResponse>> {
        val response = categoryService.getCategoryWithLowestPrice()
        return ResponseEntity.ok(response)
    }

    /**
     * 구현 2) - 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
     */
    @GetMapping("/lowest-price-brand")
    fun getLowestPriceBrand(): ResponseEntity<CategoryResources.LowestPriceBrandResponse> {
        val response = categoryService.getLowestPriceBrand()
        return ResponseEntity.ok(response)
    }
}
