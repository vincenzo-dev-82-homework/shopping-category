package com.musinsa.product.ui

import com.musinsa.product.application.BrandService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "브랜드 생성 및 조회")
@RestController
@RequestMapping("/v1/musinsa/brands")
class BrandController(
    private val brandService: BrandService,
    private val brandResponseModelAssembler: BrandResponseModelAssembler,
) {
    @Operation(
        summary = "브랜드 등록 API",
        description = """
            - 브랜드를 등록 합니다.
            - 유니크한 브랜드 ID를 생성하여 저장합니다.
            - 브랜드명은 중복될 수 없습니다.
        """,
    )
    @PostMapping
    fun createBrand(
        @RequestBody request: BrandResources.RequestDTO,
    ): ResponseEntity<BrandResources.ResponseModel> {
        val brand = brandService.create(request)

        val brandModel = BrandResources.ResponseModel(id = brand.id!!)
        val brandWithLinks = brandResponseModelAssembler.toModel(brandModel)
        return ResponseEntity.status(HttpStatus.CREATED).body(brandWithLinks)
    }

    @Operation(
        summary = "브랜드 조회 API",
        description = """
            - 브랜드를 조회 합니다.
            - 유니크한 브랜드 ID로 브랜드를 조회합니다.
        """,
    )
    @GetMapping("/{brandId}")
    fun findBrandById(
        @PathVariable("brandId") id: Long,
    ): ResponseEntity<BrandResources.ResponseDTO> {
        val brand = brandService.findById(id)
        return ResponseEntity.status(HttpStatus.CREATED).body(brand)
    }

    @Operation(
        summary = "브랜드 조회 API",
        description = """
            - 브랜드를 조회 합니다.
            - 유니크한 브랜드 ID로 브랜드를 조회합니다.
        """,
    )
    @GetMapping("/")
    fun findAll(): ResponseEntity<List<BrandResources.ResponseDTO>> {
        val brands = brandService.findAll()
        return ResponseEntity.status(HttpStatus.CREATED).body(brands)
    }
}
