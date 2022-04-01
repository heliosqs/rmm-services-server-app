package com.heliosqs.rmmservicesserverapp.model.exception

import org.springframework.http.HttpStatus

class UnprocessableException(override val message: String?, override val details: List<String>?) :
    ResponseException(message = message, status = HttpStatus.UNPROCESSABLE_ENTITY, details = details)
