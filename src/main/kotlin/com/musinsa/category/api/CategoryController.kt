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
    @GetMapping("/lowest-price")
    fun getCategoryLowestPrice(): ResponseEntity<List<CategoryResources.LowestPriceResponse>> {
        val response = categoryService.getCategoryWithLowestPrice()
        return ResponseEntity.ok(response)
    }
}
