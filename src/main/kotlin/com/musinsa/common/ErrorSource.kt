package com.musinsa.common

data class ErrorSource(
    val code: ErrorCode,
    val message: String,
    val info: Map<String, Any?>? = null,
    val cause: Throwable? = null,
)
