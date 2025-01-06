package com.musinsa.common

enum class ErrorCode(
    val desc: String,
) {
    ST00("시스템 에러입니다."),
    PD01("요청한 상품이 이미 존재합니다."),
    PD02("등록된 상품이 없습니다."),
}
