package com.musinsa.product

import com.musinsa.product.domain.Brand
import com.musinsa.product.domain.Product
import java.math.BigDecimal

internal class ProductTest

internal fun Product.Companion.aDummy(brand: Brand) =
    Product(
        id = 1L,
        name = "A_상의",
        price = BigDecimal(10_000),
        brand = brand,
    )
