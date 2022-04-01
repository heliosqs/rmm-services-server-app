package com.heliosqs.rmmservicesserverapp.model.core

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Services(
    @Id
    @GeneratedValue
    var id: Long,
    @Column(unique = true, nullable = false)
    var name: String,
    @JsonIgnore // This field is true if the user can manually include the service
    val contract: Boolean = false
)
