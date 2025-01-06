package com.musinsa.common.handler

import com.fasterxml.jackson.annotation.JsonFormat
import com.musinsa.common.ErrorSource
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ErrorResponse(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String? = null,
    val path: String,
    val info: Map<String, Any?>? = null,
) {
    companion object {
        fun from(
            request: HttpServletRequest,
            status: HttpStatus,
            ex: Exception,
            errorSource: ErrorSource,
        ): ErrorResponse =
            ErrorResponse(
                timestamp = LocalDateTime.now(),
                status = status.value(),
                error = status.reasonPhrase,
                message = ex.message,
                path = request.servletPath,
                info = errorSource.info,
            )
    }
}
