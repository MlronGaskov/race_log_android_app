package miron.gaskov.racelog.data.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AccessTokenInterceptor(
    private val tokenStore: TokenStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val access = runBlocking { tokenStore.getAccess() }

        val req = if (!access.isNullOrBlank()) {
            chain.request().newBuilder()
                .header("Authorization", "Bearer $access")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(req)
    }
}
