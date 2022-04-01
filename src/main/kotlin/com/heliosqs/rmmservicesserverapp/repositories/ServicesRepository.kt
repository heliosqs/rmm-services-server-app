package com.heliosqs.rmmservicesserverapp.repositories

import com.heliosqs.rmmservicesserverapp.model.core.Services
import org.springframework.data.repository.CrudRepository

interface ServicesRepository : CrudRepository<Services, Long> {
    fun findServicesByContractIsTrueAndIdIn(ids: Set<Long>): List<Services>
}
