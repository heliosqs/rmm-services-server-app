package com.heliosqs.rmmservicesserverapp.gateway

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.heliosqs.rmmservicesserverapp.configurations.security.AuthToken
import com.heliosqs.rmmservicesserverapp.model.core.Customer
import com.heliosqs.rmmservicesserverapp.model.core.DeviceType
import com.heliosqs.rmmservicesserverapp.model.core.Devices
import com.heliosqs.rmmservicesserverapp.model.request.DeviceRequest
import com.heliosqs.rmmservicesserverapp.services.Interfaces.DeviceServiceI
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class DeviceGatewayTest(@Autowired val mockMvc: MockMvc) {

    object MockitoHelper {
        fun <T> anyObject(): T {
            Mockito.any<T>()
            return uninitialized()
        }
        @Suppress("UNCHECKED_CAST")
        fun <T> uninitialized(): T = null as T
    }

    @MockBean
    private lateinit var deviceService: DeviceServiceI

    var mapper = ObjectMapper()

    private fun requestToJson(
        deviceRequest: DeviceRequest
    ): String {
        mapper.registerModule(JavaTimeModule())
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        return mapper.writeValueAsString(deviceRequest)
    }

    @WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
    annotation class WithMockCustomUser(val username: String = "elio", val name: String = "Elio Quevedo")

    class WithMockCustomUserSecurityContextFactory : WithSecurityContextFactory<WithMockCustomUser> {
        override fun createSecurityContext(annotation: WithMockCustomUser): SecurityContext {
            val context = SecurityContextHolder.createEmptyContext()
            val auth: Authentication = AuthToken(Customer(id = 1, name = annotation.name), "", null)
            auth.isAuthenticated = true
            context.authentication = auth
            return context
        }
    }

    @Test
    @WithMockCustomUser
    fun `Creates a new device if the name does not exist for the company`() {
        Mockito.`when`(deviceService.create(MockitoHelper.anyObject()))
            .thenReturn(Devices(id = 1, name = "A1", type = DeviceType.MAC, customer = Customer(id = 1, name = "Elio Quevedo")))
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/devices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestToJson(DeviceRequest(name = "A1", type = "MAC")))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.jsonPath("data.name")
                    .value("A1")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("error").isEmpty)
            .andDo(MockMvcResultHandlers.print())
    }
}
