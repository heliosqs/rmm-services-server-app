package com.heliosqs.rmmservicesserverapp.services

import com.heliosqs.rmmservicesserverapp.model.core.Customer
import com.heliosqs.rmmservicesserverapp.model.core.Services
import com.heliosqs.rmmservicesserverapp.model.exception.UnprocessableException
import com.heliosqs.rmmservicesserverapp.repositories.CustomerRepository
import com.heliosqs.rmmservicesserverapp.repositories.ServicesRepository
import com.heliosqs.rmmservicesserverapp.services.Interfaces.CustomerServiceI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.stream.Collectors

@Service
class CustomerService : CustomerServiceI {
    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var serviceRepository: ServicesRepository

    override fun findById(id: Long): Optional<Customer> {
        return customerRepository.findById(id)
    }

    override fun addServices(serviceIds: Set<Long>, customer: Customer): List<Services> {
        val services = serviceRepository.findServicesByContractIsTrueAndIdIn(serviceIds)
        val nonExistentServiceIds = serviceIds.filter { !services.map { s -> s.id }.contains(it) }
        if (nonExistentServiceIds.isNotEmpty()) {
            throw UnprocessableException(message = "Services with id: [${nonExistentServiceIds.map { it.toString() }.stream().collect(Collectors.joining(","))}] do not exist", details = null)
        }
        val alreadyRegisteredServices = customer.services.map { it.id }.filter { serviceIds.contains(it) }
        if (alreadyRegisteredServices.isNotEmpty()) {
            throw UnprocessableException(message = "Services with id: [${alreadyRegisteredServices.map { it.toString() }.stream().collect(Collectors.joining(","))}] are already registered for the customer", details = null)
        }
        customer.services.addAll(services)
        customerRepository.save(customer)
        return customer.services
    }

    override fun deleteServices(serviceIds: Set<Long>, customer: Customer): List<Services> {
        val services = serviceRepository.findServicesByContractIsTrueAndIdIn(serviceIds)
        val nonExistentServiceIds = serviceIds.filter { !services.map { s -> s.id }.contains(it) }
        if (nonExistentServiceIds.isNotEmpty()) {
            throw UnprocessableException(message = "Services with id: [${nonExistentServiceIds.map { it.toString() }.stream().collect(Collectors.joining(","))}] do not exist", details = null)
        }
        val nonRegisteredServices = serviceIds.filter { !customer.services.map { c -> c.id }.contains(it) }
        if (nonRegisteredServices.isNotEmpty()) {
            throw UnprocessableException(message = "Services with id: [${nonRegisteredServices .map { it.toString() }.stream().collect(Collectors.joining(","))}] are not registered for the customer", details = null)
        }
        customer.services.removeAll(services.toSet())
        customerRepository.save(customer)
        return customer.services
    }
}
