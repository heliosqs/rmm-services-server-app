package com.heliosqs.rmmservicesserverapp.configurations

import com.heliosqs.rmmservicesserverapp.model.exception.ResponseException
import com.heliosqs.rmmservicesserverapp.model.response.common.AppResponse
import com.heliosqs.rmmservicesserverapp.model.response.common.ErrorResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.Exception
import java.util.stream.Collectors

@EnableWebMvc
@ControllerAdvice
class RestExceptionHandler() : ResponseEntityExceptionHandler() {
    /**
     * Handles the errors thrown by javax validator to use our error format
     */
    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val objectErrors: List<String> = ex.bindingResult.allErrors.stream()
            .map { e: ObjectError ->
                val message = e.defaultMessage
                val codes = e.codes?.get(1)
                val field = codes?.substring(codes.indexOf('.').plus(1))
                "Field $field: $message"
            }.collect(Collectors.toList())
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Bad Request", details = objectErrors)
        val applicationResponse = AppResponse(null, errorResponse)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(applicationResponse)
    }

    @ExceptionHandler(ResponseException::class)
    fun handleAppErrors(ex: ResponseException, request: WebRequest?): ResponseEntity<Any?>? {
        val errorResponse = ErrorResponse(ex.status.toString(), ex.message.orEmpty(), details = ex.details)
        val applicationResponse = AppResponse(null, errorResponse)
        return ResponseEntity.status(ex.status).body(applicationResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleAll(ex: Exception, request: WebRequest?): ResponseEntity<Any?>? {
        val errorResponse = ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Server Error", details = listOfNotNull(ex.message))
        val applicationResponse = AppResponse(null, errorResponse)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(applicationResponse)
    }
}
