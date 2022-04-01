package com.heliosqs.rmmservicesserverapp.services.Interfaces

import com.heliosqs.rmmservicesserverapp.model.core.Customer
import com.heliosqs.rmmservicesserverapp.model.core.Services
import com.heliosqs.rmmservicesserverapp.model.exception.UnprocessableException
import org.springframework.stereotype.Service
import java.util.Optional

@Service
interface CustomerServiceI {
    /**
     * Find an existing customer by its ID
     * @param id [Long] Customer that will receive the token
     * @return Optional<Customer> Returns the customer information if it exists
     */
    fun findById(id: Long): Optional<Customer>

    /***
     * Adds a new service that the user contracted. You can't add the same service twice or a service that does not exist.
     * @param serviceIds [Set<Long>] set of identifiers for services that the user wants to add
     * @param customer [Customer] customer that is contracting a service
     * @throws UnprocessableException if the service identifier is not valid or it is already included in the customer's plan
     */
    fun addServices(serviceIds: Set<Long>, customer: Customer): List<Services>

    /***
     * Deletes a service contracted by a customer
     * @param serviceIds [Set<Long>] set of identifiers for services that the user wants to remove
     * @param customer [Customer] customer that is removing a contracted service
     * @throws UnprocessableException if the service identifier is not valid or it is not included in the customer's plan
     */
    fun deleteServices(serviceIds: Set<Long>, customer: Customer): List<Services>
}
