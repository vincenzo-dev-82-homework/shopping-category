package com.musinsa.common.exception

import com.musinsa.common.ErrorCode
import com.musinsa.common.ErrorSource

open class CustomException(
    val errorSource: ErrorSource,
) : RuntimeException(errorSource.message)

class BrandAlreadyExistsException : CustomException {
    constructor(message: String, name: String) : super(ErrorSource(ErrorCode.BD01, message, mapOf("name" to name)))
}

class BrandNotFoundException : CustomException {
    constructor(message: String, id: Long) : super(ErrorSource(ErrorCode.BD02, message, mapOf("id" to id)))
}

class ProductAlreadyExistsException : CustomException {
    constructor(message: String, name: String) : super(ErrorSource(ErrorCode.PD01, message, mapOf("name" to name)))
}

class ProductNotFoundException : CustomException {
    constructor(message: String, id: Long) : super(ErrorSource(ErrorCode.PD02, message, mapOf("id" to id)))
}
