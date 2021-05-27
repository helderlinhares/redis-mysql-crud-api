package me.hl.redisapi.exception

import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionHandler(val messageSource: MessageSource) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(ItemNotFoundException::class)
    fun handleItemNotFoundException(exception: ItemNotFoundException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        return buildResponseAndLog(
            HttpStatus.NOT_FOUND,
            request,
            ErrorResponse(listOf(
                Error(code = exception.message, message = messageSource.getMessage(
                    exception.message, null, request.locale))
            ))
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val errors = exception.bindingResult.allErrors.map {
            val args = when (it) {
                is FieldError -> it.rejectedValue?.let { r -> arrayOf(it.field, r) } ?: arrayOf(it.field)
                is ObjectError -> arrayOf(it.objectName)
                else -> emptyArray()
            }
            Error(it.defaultMessage!!, messageSource.getMessage(it.defaultMessage!!, args, request.locale))
        }
        val errorResponse = ErrorResponse(errors)
        return buildResponseAndLog(HttpStatus.BAD_REQUEST, request, errorResponse)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(exception: HttpRequestMethodNotSupportedException, request: HttpServletRequest) =
        buildResponseAndLog(HttpStatus.METHOD_NOT_ALLOWED, request)

    @ExceptionHandler(HttpMediaTypeNotAcceptableException::class)
    fun handleHttpMediaTypeNotAcceptableException(exception: HttpMediaTypeNotAcceptableException, request: HttpServletRequest) =
        buildResponseAndLog(HttpStatus.NOT_ACCEPTABLE, request)

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleHttpMediaTypeNotSupportedException(exception: HttpMediaTypeNotSupportedException, request: HttpServletRequest) =
        buildResponseAndLog(HttpStatus.UNSUPPORTED_MEDIA_TYPE, request)

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(exception: MethodArgumentTypeMismatchException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val error = Error(exception.message, messageSource.getMessage(ErrorCode.INVALID_REQUEST_BODY, arrayOf(exception.name, exception.requiredType?.simpleName, exception.value), request.locale))
        val errorResponse = ErrorResponse(listOf(error))
        return buildResponseAndLog(HttpStatus.BAD_REQUEST, request, errorResponse)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(exception: HttpMessageNotReadableException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val error = Error(ErrorCode.INVALID_REQUEST_BODY, messageSource.getMessage(ErrorCode.INVALID_REQUEST_BODY, null, request.locale))
        val errorResponse = ErrorResponse(listOf(error))
        return buildResponseAndLog(HttpStatus.BAD_REQUEST, request, errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleUnknownException(exception: Exception, request: HttpServletRequest) =
        buildResponseAndLog(HttpStatus.INTERNAL_SERVER_ERROR, request)


    private fun buildResponseAndLog(status: HttpStatus, request: HttpServletRequest, payload: ErrorResponse? = null): ResponseEntity<ErrorResponse> {
        logResponse(status, payload, request)
        return ResponseEntity.status(status).body(payload)
    }

    private fun logResponse(status: HttpStatus, errorResponse: ErrorResponse?, request: HttpServletRequest) {
        val logString =
            "statusCode=${status.value()}, response=$errorResponse, requestPath=${request.requestURI}, methodValue=${request.method}"
        if (status.is5xxServerError)
            logger.error(logString)
        else
            logger.info(logString)
    }
}
