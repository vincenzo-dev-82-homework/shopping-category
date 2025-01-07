package com.musinsa.product.domain

internal class BrandTest

internal fun Brand.Companion.aDummy() =
    Brand(
        id = 1L,
        name = "A",
        status = Brand.Status.ON,
    )

internal fun Brand.Companion.create(
    id: Long,
    name: String,
) = Brand(
    id = id,
    name = name,
    status = Brand.Status.ON,
)
