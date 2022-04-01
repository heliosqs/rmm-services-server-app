package com.heliosqs.rmmservicesserverapp.model.core

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "customer")
data class Customer(
    @Id
    var id: Long,
    @Column(nullable = false, unique = true)
    var name: String,
    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE, CascadeType.REFRESH])
    var services: MutableList<Services> = mutableListOf()
)
