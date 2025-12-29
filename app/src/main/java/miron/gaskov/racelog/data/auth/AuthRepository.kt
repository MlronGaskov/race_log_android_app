package miron.gaskov.racelog.data.auth

import miron.gaskov.racelog.data.api.Dto
import miron.gaskov.racelog.data.api.TrackStatsApi

class AuthRepository(
    private val api: TrackStatsApi,
    private val tokenStore: TokenStore
) {
    suspend fun login(login: String, password: String): Dto.LoginResponse {
        val resp = api.login(Dto.LoginRequest(login = login, password = password))
        tokenStore.setTokens(resp.accessToken, resp.refreshToken)
        return resp
    }

    suspend fun registerStart(req: Dto.RegisterStartRequest): Dto.RegisterStartResponse {
        return api.registerStart(req)
    }

    suspend fun registerConfirm(
        confirm: Dto.RegisterConfirmRequest,
        login: String,
        password: String,
        role: String
    ): Dto.LoginResponse {
        val resp = api.registerConfirm(
            confirm = confirm,
            login = login,
            password = password,
            role = role
        )
        tokenStore.setTokens(resp.accessToken, resp.refreshToken)
        return resp
    }

    suspend fun bootstrap(): Boolean {
        val access = tokenStore.getAccess()
        val refresh = tokenStore.getRefresh()
        if (access.isNullOrBlank() || refresh.isNullOrBlank()) return false

        return try {
            api.me()
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun logout() {
        tokenStore.clear()
    }

    suspend fun me(): Dto.UserMeResponse = api.me()
}
