package miron.gaskov.racelog.data

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import miron.gaskov.racelog.data.api.TrackStatsApi
import miron.gaskov.racelog.data.auth.AccessTokenInterceptor
import miron.gaskov.racelog.data.auth.RefreshTokenAuthenticator
import miron.gaskov.racelog.data.auth.TokenStore
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object NetworkModule {

    fun createApi(context: Context, baseUrl: String): Pair<TrackStatsApi, TokenStore> {
        val tokenStore = TokenStore(context)

        val json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
        val contentType = "application/json".toMediaType()

        val refreshRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        val refreshApi = refreshRetrofit.create(TrackStatsApi::class.java)

        val mainClient = OkHttpClient.Builder()
            .addInterceptor(AccessTokenInterceptor(tokenStore))
            .authenticator(
                RefreshTokenAuthenticator(
                    tokenStore = tokenStore,
                    refreshApiProvider = { refreshApi }
                )
            )
            .build()

        val mainRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(mainClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        val api = mainRetrofit.create(TrackStatsApi::class.java)

        return api to tokenStore
    }
}
