package com.heliosqs.rmmservicesserverapp.model.exception

import org.springframework.http.HttpStatus

class NotFoundException(override val message: String?, override val details: List<String>?) :
    ResponseException(message = message, status = HttpStatus.NOT_FOUND, details = details)
