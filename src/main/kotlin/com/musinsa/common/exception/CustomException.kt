package com.musinsa.common.exception

import com.musinsa.common.ErrorCode
import com.musinsa.common.ErrorSource

open class CustomException(
    val errorSource: ErrorSource,
) : RuntimeException(errorSource.message)

class DataAlreadyExistsException : CustomException {
    constructor(message: String, name: String) : super(ErrorSource(ErrorCode.PD01, message, mapOf("name" to name)))
}

class DataNotFoundException : CustomException {
    constructor(message: String, id: Long) : super(ErrorSource(ErrorCode.PD02, message, mapOf("id" to id)))
}
