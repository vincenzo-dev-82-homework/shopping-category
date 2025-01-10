package com.musinsa.category.domain

import com.musinsa.category.domain.entity.Category

internal class CategoryTest

internal fun Category.Companion.aDummy() =
    Category(
        id = 1L,
        parent = null,
        name = CategoryType.TOP.displayName,
        type = CategoryType.TOP,
    )
