package com.heliosqs.rmmservicesserverapp.model.response.common

import java.time.LocalDateTime
import java.time.ZoneOffset

data class ErrorResponse(
    val code: String,
    val message: String,
    val details: List<String>? = listOf(),
    val timestamp: LocalDateTime? = LocalDateTime.now(ZoneOffset.UTC),
)
