package com.musinsa.product.api.model

import com.musinsa.product.api.BrandController
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class BrandResponseModelAssembler : RepresentationModelAssembler<BrandResources.ResponseModel, BrandResources.ResponseModel> {
    override fun toModel(brand: BrandResources.ResponseModel): BrandResources.ResponseModel {
        val selfLink =
            linkTo(
                methodOn(BrandController::class.java).findBrandById(brand.id),
            ).withSelfRel()

        val listLink =
            linkTo(
                methodOn(BrandController::class.java).findAll(),
            ).withRel("[GET]브랜드 목록 조회 API")
        val singleLink =
            linkTo(
                methodOn(BrandController::class.java).findBrandById(brand.id),
            ).withRel("[GET]브랜드 단일 조회 API")
        brand.add(selfLink, listLink, singleLink)
        return brand
    }
}
