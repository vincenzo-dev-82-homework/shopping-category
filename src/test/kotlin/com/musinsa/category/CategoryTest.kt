package com.musinsa.category

import com.musinsa.category.domain.Category
import com.musinsa.category.domain.CategoryType

internal class CategoryTest

internal fun Category.Companion.aDummy() =
    Category(
        id = 1L,
        category = CategoryType.OUTER,
        description = CategoryType.OUTER.displayName,
    )
