package com.musinsa.category.api

import com.musinsa.category.application.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
     * 구현 3) - 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
     * TODO 일단 과제의 내용에 id 조회가 없으므로 당장의 충돌은 없다고 하자
     */
    @GetMapping("/{categoryName}/prices")
    fun getCategoryPriceDetails(
        @PathVariable(name = "categoryName", required = true) categoryName: String,
    ): ResponseEntity<CategoryResources.PriceResponse> {
        val response = categoryService.getCategoryPriceDetails(categoryName)
        return ResponseEntity.ok(response)
    }
}
