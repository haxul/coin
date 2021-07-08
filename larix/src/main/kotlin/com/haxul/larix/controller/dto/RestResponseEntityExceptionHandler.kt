package com.haxul.larix.controller.dto

import com.haxul.larix.exception.ErrorMessage
import com.haxul.larix.exception.ExceedWalletBalanceTxException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    private val logger: Logger = LogManager.getLogger(ResponseEntityExceptionHandler::class.java)

    @ExceptionHandler(value = [ExceedWalletBalanceTxException::class])
    protected fun handleExceedWalletBalance(ex: ExceedWalletBalanceTxException): ResponseEntity<Any> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorMessage("ExceedWalletBalance", "Balance of the wallet is too small"))
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleException(ex: Exception): ResponseEntity<Any> {
        logger.error("exception happens", ex)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorMessage("UnknownError", "Something gets wrong"))
    }


}