package com.heliosqs.rmmservicesserverapp.services.Interfaces

import com.heliosqs.rmmservicesserverapp.model.core.Customer
import com.heliosqs.rmmservicesserverapp.model.core.DeviceType
import com.heliosqs.rmmservicesserverapp.model.core.Devices
import com.heliosqs.rmmservicesserverapp.model.exception.ConflictException
import com.heliosqs.rmmservicesserverapp.model.request.DeviceRequest
import org.springframework.stereotype.Service

@Service
interface DeviceServiceI {
    /**
     * Create a new device for the customer
     * @param device [Devices] Information to create a new device
     * @return Devices Returns the created device
     * @throws ConflictException if the device name has been registered for the customer
     */
    fun create(device: Devices): Devices

    /***
     * Get a list of all the devices for a customer
     * @param customerId [Long] Identifier of the customer
     * @return List<Devices> devices for the customer
     */
    fun findAllByCustomer(customerId: Long): List<Devices>

    /***
     * Deletes an specific device for the customer
     * @param deviceId [Long] identifier of the device to remove from a customer
     * @param customer [Customer] information of the customer
     * @throws NotFoundException If the device id is not valid
     * @throws UnauthorizedException If the customer is not the owner of the device
     */
    fun delete(deviceId: Long, customer: Customer)

    /***
     * Updates an specific device information
     * @param deviceId [Long] identifier of the device to remove from a customer
     * @param customer [Customer] information of the customer
     * @param type [Customer] device operating system
     * @throws NotFoundException If the device id is not valid
     * @throws UnauthorizedException If the customer is not the owner of the device
     */
    fun update(deviceId: Long, request: DeviceRequest, type: DeviceType, customer: Customer)
}
