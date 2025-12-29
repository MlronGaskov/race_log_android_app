package miron.gaskov.racelog.data.api

import miron.gaskov.racelog.data.api.Dto.LoginRequest
import miron.gaskov.racelog.data.api.Dto.LoginResponse
import miron.gaskov.racelog.data.api.Dto.RegisterConfirmRequest
import miron.gaskov.racelog.data.api.Dto.RegisterStartRequest
import miron.gaskov.racelog.data.api.Dto.RegisterStartResponse
import miron.gaskov.racelog.data.api.Dto.TokenRefreshRequest
import miron.gaskov.racelog.data.api.Dto.UserMeResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TrackStatsApi {

    @POST("/api/v1/auth/login")
    suspend fun login(@Body req: LoginRequest): LoginResponse

    @POST("/api/v1/auth/register/start")
    suspend fun registerStart(@Body req: RegisterStartRequest): RegisterStartResponse

    @POST("/api/v1/auth/register/confirm")
    suspend fun registerConfirm(
        @Body confirm: RegisterConfirmRequest,
        @Query("login") login: String,
        @Query("password") password: String,
        @Query("role") role: String
    ): LoginResponse

    @POST("/api/v1/auth/refresh")
    suspend fun refresh(@Body req: TokenRefreshRequest): String

    @GET("/api/v1/users/me")
    suspend fun me(): UserMeResponse
}
