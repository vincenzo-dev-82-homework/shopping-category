package com.musinsa.category

import com.musinsa.category.domain.CategoryType
import com.musinsa.category.domain.entity.Category

internal class CategoryTest

internal fun Category.Companion.aDummy() =
    Category(
        id = 1L,
        parent = null,
        name = CategoryType.TOP.displayName,
        type = CategoryType.TOP,
    )
