package com.heliosqs.rmmservicesserverapp.repositories

import com.heliosqs.rmmservicesserverapp.model.core.Customer
import com.heliosqs.rmmservicesserverapp.model.core.DeviceType
import com.heliosqs.rmmservicesserverapp.model.core.Devices
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class DeviceRepositorytest {
    @Autowired
    private lateinit var deviceRepository: DeviceRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Test
    fun `Creates a device id the name does not exist for the company`() {
        val customer = entityManager.persist(Customer(id = 1, name = "Elio Q"))
        val device = Devices(name = "Elio", type = DeviceType.MAC, customer = customer)
        deviceRepository.save(device)

        val devices = deviceRepository.findAllById(mutableListOf(device.id))
        assertEquals(1, devices.count())
    }
}
