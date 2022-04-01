package com.heliosqs.rmmservicesserverapp.model.core

import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "service_prices")
data class ServicePrices(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var price: BigDecimal,
    @Enumerated(EnumType.STRING)
    var osSegment: OSPriceSegmentType,
    @ManyToOne
    @JoinColumn(name = "service_id")
    var service: Services,
)
