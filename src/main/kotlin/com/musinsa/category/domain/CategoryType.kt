package com.musinsa.category.domain

enum class CategoryType(
    val displayName: String,
) {
    TOP("상의"),
    OUTER("아우터"),
    PANTS("바지"),
    SNEAKERS("스니커즈"),
    BAG("가방"),
    HAT("모자"),
    SOCKS("양말"),
    ACCESSORY("액세서리"),
    ;

    companion object {
        fun fromDisplayName(displayName: String): CategoryType? = values().find { it.displayName == displayName }
    }
}
