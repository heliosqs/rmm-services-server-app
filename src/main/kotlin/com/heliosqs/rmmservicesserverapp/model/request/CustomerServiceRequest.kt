package com.heliosqs.rmmservicesserverapp.model.request

import com.heliosqs.rmmservicesserverapp.constants.ErrorConstants
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class CustomerServiceRequest(
    @get:NotNull(message = ErrorConstants.REQUIRED_MESSAGE)
    @get:NotEmpty(message = ErrorConstants.NOT_EMPTY_MESSAGE)
    @get:Size(min = 1)
    val serviceIds: List<Long>?
)
