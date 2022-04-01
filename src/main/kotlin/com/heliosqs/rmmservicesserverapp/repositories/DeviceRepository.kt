package com.heliosqs.rmmservicesserverapp.repositories

import com.heliosqs.rmmservicesserverapp.model.core.Devices
import org.springframework.data.repository.CrudRepository

interface DeviceRepository : CrudRepository<Devices, Long> {
    fun existsDevicesByName(name: String): Boolean
    fun findAllDevicesByCustomer_Id(customerId: Long): List<Devices>
}
