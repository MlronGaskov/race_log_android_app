package miron.gaskov.racelog.data.api

import kotlinx.serialization.Serializable

object Dto {
    @Serializable
    data class LoginRequest(val login: String, val password: String)

    @Serializable
    data class LoginResponse(
        val accessToken: String,
        val refreshToken: String,
        val userId: Long,
        val login: String,
        val role: String
    )

    @Serializable
    data class RegisterStartRequest(
        val phone: String,
        val login: String,
        val password: String,
        val role: String
    )

    @Serializable
    data class RegisterStartResponse(
        val phone: String,
        val code: String
    )

    @Serializable
    data class RegisterConfirmRequest(
        val phone: String,
        val code: String
    )

    @Serializable
    data class TokenRefreshRequest(val refreshToken: String)

    @Serializable
    data class UserMeResponse(
        val id: Long,
        val phone: String,
        val login: String,
        val role: String
    )
}
