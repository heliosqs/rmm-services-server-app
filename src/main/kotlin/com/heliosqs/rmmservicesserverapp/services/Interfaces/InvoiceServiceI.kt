package com.heliosqs.rmmservicesserverapp.services.Interfaces

import com.heliosqs.rmmservicesserverapp.model.core.Customer
import com.heliosqs.rmmservicesserverapp.model.response.invoice.Invoice
import org.springframework.stereotype.Service

@Service
interface InvoiceServiceI {

    /***
     * Calculates the total per month that a customer should pay based on the devices and contracted services
     * @param customer [Customer] information of the customer
     * @return invoice's total price
     */
    fun calculateMonthlyPrice(customer: Customer): Invoice
}
