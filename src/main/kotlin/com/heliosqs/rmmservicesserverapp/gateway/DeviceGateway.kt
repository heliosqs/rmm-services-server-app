package com.heliosqs.rmmservicesserverapp.gateway

import com.heliosqs.rmmservicesserverapp.configurations.security.AuthToken
import com.heliosqs.rmmservicesserverapp.constants.ErrorConstants
import com.heliosqs.rmmservicesserverapp.model.core.Customer
import com.heliosqs.rmmservicesserverapp.model.core.DeviceType
import com.heliosqs.rmmservicesserverapp.model.core.Devices
import com.heliosqs.rmmservicesserverapp.model.exception.BadRequestException
import com.heliosqs.rmmservicesserverapp.model.request.DeviceRequest
import com.heliosqs.rmmservicesserverapp.model.response.common.AppResponse
import com.heliosqs.rmmservicesserverapp.services.Interfaces.DeviceServiceI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors
import javax.validation.Valid
import javax.validation.constraints.Pattern

@RestController
@Validated
@RequestMapping("/api/devices")
class DeviceGateway {
    @Autowired
    private lateinit var deviceService: DeviceServiceI

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid request: DeviceRequest): ResponseEntity<AppResponse> {
        val authentication = SecurityContextHolder.getContext().authentication as AuthToken
        val customer: Customer = authentication.principal as Customer
        val name: String = request.name.toString()
        val deviceType = validateType(request.type.toString())
        val device: Devices = deviceService.create(Devices(name = name, customer = customer, type = deviceType))
        return ResponseEntity.ok(AppResponse(device, null))
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): ResponseEntity<AppResponse> {
        val authentication = SecurityContextHolder.getContext().authentication as AuthToken
        val customer: Customer = authentication.principal as Customer
        val devices = deviceService.findAllByCustomer(customer.id)
        return ResponseEntity.ok(AppResponse(devices, null))
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable("id")
        @Pattern(regexp = "[0-9]+", message = ErrorConstants.NOT_NUMERIC)
        id: String
    ) {
        val authentication = SecurityContextHolder.getContext().authentication as AuthToken
        val customer: Customer = authentication.principal as Customer
        deviceService.delete(id.toLong(), customer)
    }

    @PatchMapping("/{id}")
    fun update(
        @PathVariable("id")
        @Pattern(regexp = "[0-9]+", message = ErrorConstants.NOT_NUMERIC)
        id: String,
        @RequestBody @Valid request: DeviceRequest
    ) {
        val authentication = SecurityContextHolder.getContext().authentication as AuthToken
        val customer: Customer = authentication.principal as Customer
        val deviceType = validateType(request.type.toString())
        deviceService.update(id.toLong(), request, deviceType, customer)
    }

    private fun validateType(typeStr: String): DeviceType {
        val types: List<DeviceType> = DeviceType.values().toList()
        val deviceType: List<DeviceType> = types.filter { it.name.equals(typeStr, ignoreCase = true) }
        if (deviceType.size != 1) {
            throw BadRequestException(
                message = "Type does not have a valid value",
                details = listOf("Valid values for type are: [${types.map{it.name}.stream().collect(Collectors.joining(","))}]")
            )
        }
        return deviceType[0]
    }
}
