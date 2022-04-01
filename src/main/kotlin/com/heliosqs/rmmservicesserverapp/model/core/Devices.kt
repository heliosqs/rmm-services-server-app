package com.heliosqs.rmmservicesserverapp.model.core

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "devices", uniqueConstraints = [UniqueConstraint(columnNames = ["customer_id", "name"])])
data class Devices(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    @Enumerated(EnumType.STRING)
    var type: DeviceType,
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    var customer: Customer,
)
