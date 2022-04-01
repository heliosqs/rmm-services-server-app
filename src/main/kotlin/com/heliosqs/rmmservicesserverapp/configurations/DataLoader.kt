package com.heliosqs.rmmservicesserverapp.configurations

import com.heliosqs.rmmservicesserverapp.model.core.Customer
import com.heliosqs.rmmservicesserverapp.model.core.OSPriceSegmentType
import com.heliosqs.rmmservicesserverapp.model.core.ServicePrices
import com.heliosqs.rmmservicesserverapp.model.core.Services
import com.heliosqs.rmmservicesserverapp.repositories.CustomerRepository
import com.heliosqs.rmmservicesserverapp.repositories.ServicePricesRepository
import com.heliosqs.rmmservicesserverapp.repositories.ServicesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class DataLoader : ApplicationListener<ContextRefreshedEvent> {
    var alreadySetup = false

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var servicesRepository: ServicesRepository

    @Autowired
    private lateinit var pricesRepository: ServicePricesRepository

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (!alreadySetup) {
            customerRepository.save(Customer(id = 1, name = "Elio Company"))
            customerRepository.save(Customer(id = 2, name = "Competitor Company"))
            val antivirus = servicesRepository.save(Services(id = 1, name = "Antivirus", contract = true))
            val cloudberry = servicesRepository.save(Services(id = 2, name = "Cloudberry", contract = true))
            val psa = servicesRepository.save(Services(id = 4, name = "PSA", contract = true))
            val teamviewer = servicesRepository.save(Services(id = 3, name = "Teamviewer", contract = true))
            val devices = servicesRepository.save(Services(id = 5, name = "Devices", contract = false))
            pricesRepository.save(ServicePrices(service = antivirus, osSegment = OSPriceSegmentType.WINDOWS, price = BigDecimal(5)))
            pricesRepository.save(ServicePrices(service = antivirus, osSegment = OSPriceSegmentType.MAC, price = BigDecimal(7)))
            pricesRepository.save(ServicePrices(service = cloudberry, osSegment = OSPriceSegmentType.ANY, price = BigDecimal(3)))
            pricesRepository.save(ServicePrices(service = psa, osSegment = OSPriceSegmentType.ANY, price = BigDecimal(2)))
            pricesRepository.save(ServicePrices(service = teamviewer, osSegment = OSPriceSegmentType.ANY, price = BigDecimal(1)))
            pricesRepository.save(ServicePrices(service = devices, osSegment = OSPriceSegmentType.ANY, price = BigDecimal(4)))
            alreadySetup = true
        }
    }
}
