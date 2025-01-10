package com.musinsa.product.api

import com.musinsa.product.api.model.ProductResources
import com.musinsa.product.api.model.ProductResponseModelAssembler
import com.musinsa.product.application.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 구현 4) 브랜드 및 상품을 추가 / 업데이트 / 삭제하는 API
 */
@Tag(name = "상품 생성 및 조회")
@RestController
@RequestMapping("/v1/musinsa/products")
class ProductController(
    private val productService: ProductService,
    private val productResponseModelAssembler: ProductResponseModelAssembler,
) {
    @Operation(
        summary = "상품 등록 API",
        description = """
            - 상품을 등록 합니다.
            - 유니크한 상품 ID를 생성하여 저장합니다.
            - 브랜드 ID가 필요합니다.
        """,
    )
    @PostMapping
    fun createProduct(
        @RequestBody request: ProductResources.CreateDTO,
    ): ResponseEntity<ProductResources.ResponseModel> {
        val product = productService.create(request)
        val productModel = ProductResources.ResponseModel(id = product.id!!)
        val productWithLinks = productResponseModelAssembler.toModel(productModel)
        return ResponseEntity.status(HttpStatus.CREATED).body(productWithLinks)
    }

    @Operation(
        summary = "상품 수정 API",
        description = """
            - 상품 정보를 수정합니다.
            - 상품 ID가 필요합니다.
        """,
    )
    @PutMapping("/{productId}")
    fun updateProduct(
        @PathVariable("productId") id: Long,
        @RequestBody request: ProductResources.UpdateDTO,
    ): ResponseEntity<ProductResources.ResponseModel> {
        val product = productService.update(request)
        val productModel = ProductResources.ResponseModel(id = product.id!!)
        val productWithLinks = productResponseModelAssembler.toModel(productModel)
        return ResponseEntity.status(HttpStatus.OK).body(productWithLinks)
    }

    @Operation(
        summary = "상품 삭제 API",
        description = """
        - 상품을 삭제합니다.
        - 상품 ID가 필요합니다.
    """,
    )
    @DeleteMapping("/{productId}")
    fun deleteProduct(
        @PathVariable("productId") id: Long,
    ): ResponseEntity<Void> {
        productService.delete(ProductResources.DeleteDTO(id = id))
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @Operation(
        summary = "상품 조회 API",
        description = """
            - 상품목록을 조회 합니다.
        """,
    )
    @GetMapping("/")
    fun findAll(): ResponseEntity<List<ProductResources.ResponseDTO>> {
        val products = productService.findAll()
        return ResponseEntity.status(HttpStatus.OK).body(products)
    }

    @Operation(
        summary = "상품 단일 조회 API",
        description = """
            - 상품을 조회 합니다.
            - 유니크한 상품 ID로 상품을 조회합니다.
        """,
    )
    @GetMapping("/{productId}")
    fun findProductById(
        @PathVariable("productId") id: Long,
    ): ResponseEntity<ProductResources.ResponseDTO> {
        val product = productService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(product)
    }
}
