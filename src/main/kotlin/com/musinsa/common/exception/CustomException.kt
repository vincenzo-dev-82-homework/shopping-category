package com.musinsa.common.exception

import com.musinsa.common.ErrorCode
import com.musinsa.common.ErrorSource

open class CustomException(
    val errorSource: ErrorSource,
) : RuntimeException(errorSource.message)

open class DataAlreadyExistsException(
    val errorSource: ErrorSource,
) : RuntimeException(errorSource.message)

open class DataNotFoundException(
    val errorSource: ErrorSource,
) : RuntimeException(errorSource.message)

class BrandAlreadyExistsException : DataAlreadyExistsException {
    constructor(message: String, name: String) : super(ErrorSource(ErrorCode.BD01, message, mapOf("name" to name)))
}

class BrandNotFoundException : DataNotFoundException {
    constructor(message: String, id: Long) : super(ErrorSource(ErrorCode.BD02, message, mapOf("id" to id)))
}

class ProductAlreadyExistsException : DataAlreadyExistsException {
    constructor(message: String, name: String) : super(ErrorSource(ErrorCode.PD01, message, mapOf("name" to name)))
}

class ProductNotFoundException : DataNotFoundException {
    constructor(message: String, id: Long) : super(ErrorSource(ErrorCode.PD02, message, mapOf("id" to id)))
}
