package com.heliosqs.rmmservicesserverapp.model.exception

import org.springframework.http.HttpStatus

class BadRequestException(override val message: String?, override val details: List<String>?) :
    ResponseException(message = message, status = HttpStatus.BAD_REQUEST, details = details)
