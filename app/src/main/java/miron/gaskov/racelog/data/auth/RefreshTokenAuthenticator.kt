package miron.gaskov.racelog.data.auth

import kotlinx.coroutines.runBlocking
import miron.gaskov.racelog.data.api.Dto
import miron.gaskov.racelog.data.api.TrackStatsApi
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class RefreshTokenAuthenticator(
    private val tokenStore: TokenStore,
    private val refreshApiProvider: () -> TrackStatsApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        val path = response.request.url.encodedPath
        if (path.contains("/api/v1/auth/refresh")) return null

        val refresh = runBlocking { tokenStore.getRefresh() } ?: return null

        val refreshApi = refreshApiProvider()

        val newAccess: String? = runBlocking {
            try {
                refreshApi.refresh(Dto.TokenRefreshRequest(refreshToken = refresh))
            } catch (_: Exception) {
                null
            }
        }

        if (newAccess.isNullOrBlank()) {
            runBlocking { tokenStore.clear() }
            return null
        }

        runBlocking { tokenStore.updateAccess(newAccess) }

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccess")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var r: Response? = response
        var count = 1
        while (r?.priorResponse != null) {
            count++
            r = r.priorResponse
        }
        return count
    }
}
