package com.musinsa.product.api.model

import com.musinsa.product.api.ProductController
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ProductResponseModelAssembler : RepresentationModelAssembler<ProductResources.ResponseModel, ProductResources.ResponseModel> {
    override fun toModel(product: ProductResources.ResponseModel): ProductResources.ResponseModel {
        val selfLink =
            linkTo(
                methodOn(ProductController::class.java).createProduct(
                    ProductResources.CreateDTO(
                        name = "A_상품",
                        price = BigDecimal.valueOf(10_000L),
                        brandId = 1L,
                    ),
                ),
            ).withSelfRel()
        val listLink =
            linkTo(
                methodOn(ProductController::class.java).findAll(),
            ).withRel("[GET]상품 목록 조회 API")
        val singleLink =
            linkTo(
                methodOn(ProductController::class.java).findProductById(product.id),
            ).withRel("[GET]상품 단일 조회 API")
        product.add(selfLink, listLink, singleLink)
        return product
    }
}
