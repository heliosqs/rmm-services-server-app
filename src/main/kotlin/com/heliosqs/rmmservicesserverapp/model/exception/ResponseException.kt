package com.heliosqs.rmmservicesserverapp.model.exception

import org.springframework.http.HttpStatus

abstract class ResponseException(override val message: String?, val status: HttpStatus, open val details: List<String>?) : Exception()
