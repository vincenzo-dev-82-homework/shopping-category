package com.musinsa.common.handler

import com.musinsa.common.ErrorCode
import com.musinsa.common.ErrorSource
import com.musinsa.common.exception.DataAlreadyExistsException
import com.musinsa.common.exception.DataNotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(annotations = [RestController::class])
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(value = [RuntimeException::class])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleRuntimeException(
        ex: RuntimeException,
        request: HttpServletRequest,
    ): ErrorResponse {
        log.error("classification|error|${ex.javaClass.simpleName}|${ex.message}", ex)
        return ErrorResponse.from(
            request,
            HttpStatus.INTERNAL_SERVER_ERROR,
            ex,
            ErrorSource(ErrorCode.ST00, ErrorCode.ST00.desc),
        )
    }

    @ExceptionHandler(
        value = [
            IllegalArgumentException::class,
        ],
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleIllegalRequestException(
        ex: RuntimeException,
        request: HttpServletRequest,
    ): ErrorResponse {
        log.error("classification|error|${ex.javaClass.simpleName}|${ex.message}", ex)
        return ErrorResponse.from(
            request,
            HttpStatus.INTERNAL_SERVER_ERROR,
            ex,
            ErrorSource(ErrorCode.ST00, ErrorCode.ST00.desc),
        )
    }

    @ExceptionHandler(
        value = [
            DataNotFoundException::class,
        ],
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleDataNotFoundException(
        ex: DataNotFoundException,
        request: HttpServletRequest,
    ): ErrorResponse {
        log.error("classification|error|${ex.javaClass.simpleName}|${ex.message}", ex)
        return ErrorResponse.from(request, HttpStatus.BAD_REQUEST, ex, ex.errorSource)
    }

    /**
     * 409 Conflict는 현재 상태와 요청이 충돌할 때 사용되고,
     * 현재 상황에서는 상품명이 이미 존재하기 때문에 등록이 충돌하는 경우다.
     */
    @ExceptionHandler(
        value = [
            DataAlreadyExistsException::class,
        ],
    )
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    fun handleDataAlreadyExistsException(
        ex: DataAlreadyExistsException,
        request: HttpServletRequest,
    ): ErrorResponse {
        log.error("classification|error|${ex.javaClass.simpleName}|${ex.message}", ex)
        return ErrorResponse.from(request, HttpStatus.CONFLICT, ex, ex.errorSource)
    }
}
