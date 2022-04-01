package com.heliosqs.rmmservicesserverapp.repositories

import com.heliosqs.rmmservicesserverapp.model.core.Customer
import org.springframework.data.repository.CrudRepository

interface CustomerRepository : CrudRepository<Customer, Long>
