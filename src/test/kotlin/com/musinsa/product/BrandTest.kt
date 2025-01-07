package com.musinsa.product

import com.musinsa.product.domain.Brand

internal class BrandTest

internal fun Brand.Companion.aDummy() =
    Brand(
        id = 1L,
        name = "A",
    )
