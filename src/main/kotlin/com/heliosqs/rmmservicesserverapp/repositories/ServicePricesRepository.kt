package com.heliosqs.rmmservicesserverapp.repositories

import com.heliosqs.rmmservicesserverapp.model.core.ServicePrices
import org.springframework.data.repository.CrudRepository

interface ServicePricesRepository : CrudRepository<ServicePrices, Long> {
    fun findServicePricesByService_IdIn(serviceIds: Set<Long>): List<ServicePrices>
}
