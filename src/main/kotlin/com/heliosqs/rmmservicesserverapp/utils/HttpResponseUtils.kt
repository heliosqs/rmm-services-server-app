package com.heliosqs.rmmservicesserverapp.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.heliosqs.rmmservicesserverapp.model.response.common.AppResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import javax.servlet.http.HttpServletResponse

object HttpResponseUtils {
    fun buildResponse(appResponse: AppResponse, responseServlet: HttpServletResponse, httpStatus: HttpStatus) {
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        responseServlet.status = httpStatus.value()
        responseServlet.contentType = MediaType.APPLICATION_JSON_VALUE
        mapper.writeValue(responseServlet.writer, appResponse)
    }
}
