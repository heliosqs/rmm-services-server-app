package com.heliosqs.rmmservicesserverapp.services

import com.heliosqs.rmmservicesserverapp.model.core.Customer
import com.heliosqs.rmmservicesserverapp.model.core.DeviceType
import com.heliosqs.rmmservicesserverapp.model.core.Devices
import com.heliosqs.rmmservicesserverapp.model.exception.ConflictException
import com.heliosqs.rmmservicesserverapp.model.exception.NotFoundException
import com.heliosqs.rmmservicesserverapp.model.exception.UnauthorizedException
import com.heliosqs.rmmservicesserverapp.model.request.DeviceRequest
import com.heliosqs.rmmservicesserverapp.repositories.DeviceRepository
import com.heliosqs.rmmservicesserverapp.services.Interfaces.DeviceServiceI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class DeviceService : DeviceServiceI {
    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    override fun create(device: Devices): Devices {
        if (!deviceRepository.existsDevicesByName(device.name)) {
            return deviceRepository.save(device)
        } else {
            throw ConflictException(message = "A device with name ${device.name} already exists", details = listOf())
        }
    }

    override fun findAllByCustomer(customerId: Long): List<Devices> {
        return deviceRepository.findAllDevicesByCustomer_Id(customerId)
    }

    override fun delete(deviceId: Long, customer: Customer) {
        val optionalDevice: Optional<Devices> = deviceRepository.findById(deviceId)
        if (optionalDevice.isEmpty) {
            throw NotFoundException(message = "Device not found", details = null)
        }
        val device: Devices = optionalDevice.get()
        if (device.customer.id != customer.id) {
            throw UnauthorizedException(message = "Not authorized to access the device", details = null)
        }
        deviceRepository.delete(device)
    }

    override fun update(deviceId: Long, request: DeviceRequest, type: DeviceType, customer: Customer) {
        val optionalDevice: Optional<Devices> = deviceRepository.findById(deviceId)
        if (optionalDevice.isEmpty) {
            throw NotFoundException(message = "Device not found", details = null)
        }
        val device: Devices = optionalDevice.get()
        if (device.customer.id != customer.id) {
            throw UnauthorizedException(message = "Not authorized to access the device", details = null)
        }
        device.name = request.name.toString()
        device.type = type
        deviceRepository.save(device)
    }
}
