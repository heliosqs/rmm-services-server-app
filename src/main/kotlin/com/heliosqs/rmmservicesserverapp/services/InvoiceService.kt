package com.heliosqs.rmmservicesserverapp.services

import com.heliosqs.rmmservicesserverapp.model.core.Customer
import com.heliosqs.rmmservicesserverapp.model.core.DeviceType
import com.heliosqs.rmmservicesserverapp.model.core.OSPriceSegmentType
import com.heliosqs.rmmservicesserverapp.model.response.invoice.Invoice
import com.heliosqs.rmmservicesserverapp.services.Interfaces.DeviceServiceI
import com.heliosqs.rmmservicesserverapp.services.Interfaces.InvoiceServiceI
import com.heliosqs.rmmservicesserverapp.services.Interfaces.ServicePriceServiceI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class InvoiceService : InvoiceServiceI {
    @Autowired
    private lateinit var deviceService: DeviceServiceI

    @Autowired
    private lateinit var servicePriceService: ServicePriceServiceI

    override fun calculateMonthlyPrice(customer: Customer): Invoice {
        val customerDevices = deviceService.findAllByCustomer(customer.id)
        val servicePrices = servicePriceService.find(customer.services.map { it.id }.toSet()).groupBy { it.service.id }
        var totalPrice = BigDecimal(0)
        customerDevices.forEach { device ->
            run {
                totalPrice = totalPrice.plus(BigDecimal(4))
                customer.services.forEach {
                    val prices = servicePrices[it.id]?.associateBy { p -> p.osSegment }
                    if (prices?.keys?.contains(OSPriceSegmentType.ANY) == true) {
                        prices[OSPriceSegmentType.ANY]?.price?.let { p -> totalPrice = totalPrice.plus(p) }
                    } else if (device.type == DeviceType.MAC && prices?.keys?.contains(OSPriceSegmentType.MAC) == true) {
                        prices[OSPriceSegmentType.MAC]?.price?.let { p -> totalPrice = totalPrice.plus(p) }
                    } else if (listOf(DeviceType.WINDOWS_SERVER, DeviceType.WINDOWS_WORKSTATION).contains(device.type) && prices?.keys?.contains(OSPriceSegmentType.WINDOWS) == true) {
                        prices[OSPriceSegmentType.WINDOWS]?.price?.let { p -> totalPrice = totalPrice.plus(p) }
                    }
                }
            }
        }
        return Invoice(total = totalPrice)
    }
}
