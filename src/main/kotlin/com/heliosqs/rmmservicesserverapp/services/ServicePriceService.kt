package com.heliosqs.rmmservicesserverapp.services

import com.heliosqs.rmmservicesserverapp.model.core.ServicePrices
import com.heliosqs.rmmservicesserverapp.repositories.ServicePricesRepository
import com.heliosqs.rmmservicesserverapp.services.Interfaces.ServicePriceServiceI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ServicePriceService : ServicePriceServiceI {
    @Autowired
    private lateinit var servicePricesRepository: ServicePricesRepository

    override fun find(serviceIds: Set<Long>): List<ServicePrices> {
        return servicePricesRepository.findServicePricesByService_IdIn(serviceIds)
    }
}
