package com.example.robopits.presentation.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class LoginRequest(val Email: String, val Password: String)
data class LoginResponse(val success: Boolean, val id: String, val Nombre: String)

data class Producto(
    val productId: String,
    val name: String,
    val quantity: Int,
    val price: Double,
    val image: String
)

data class Cliente(
    val id: String,
    val nombre: String
)

data class Pedido(
    val cliente: Cliente,
    val _id: String,
    val productos: List<Producto>,
    val descuento: Double,
    val total: Double,
    val direccion: String,
    val puntoDeRetiro: String?,
    val estado: String,
    val createdAt: String,
    val updatedAt: String
)

data class PedidoResponse(val pedidos: List<Pedido>)

interface ApiService {
    @POST("/api/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("/api/pedidosCliente/{userId}")
    suspend fun getPedidos(@Path("userId") userId: String): PedidoResponse // Cambiado a PedidoResponse
}
