package com.heliosqs.rmmservicesserverapp.services.Interfaces

import com.heliosqs.rmmservicesserverapp.model.core.ServicePrices
import org.springframework.stereotype.Service

@Service
interface ServicePriceServiceI {
    /***
     * Finds a list of service prices by using its ids, it does not return the service if the id is not found
     * @param serviceIds [Set<Long>] list of service identifiers
     * @return a list of devices that were found
     */
    fun find(serviceIds: Set<Long>): List<ServicePrices>
}
