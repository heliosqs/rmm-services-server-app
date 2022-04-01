package com.heliosqs.rmmservicesserverapp.model.exception

import org.springframework.http.HttpStatus

class ConflictException(override val message: String?, override val details: List<String>?) :
    ResponseException(message = message, status = HttpStatus.CONFLICT, details = details)
