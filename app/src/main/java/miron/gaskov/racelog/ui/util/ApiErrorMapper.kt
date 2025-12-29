package miron.gaskov.racelog.ui.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.HttpException

@Serializable
private data class ApiError(val message: String)

private val json = Json { ignoreUnknownKeys = true }

fun Throwable.toUiMessage(): String {
    if (this is HttpException) {
        val body = response()?.errorBody()?.string()
        if (!body.isNullOrBlank()) {
            runCatching { json.decodeFromString(ApiError.serializer(), body).message }
                .getOrNull()
                ?.let { return it }
        }
        return this.message() ?: "Ошибка запроса"
    }

    return message ?: "Ошибка"
}
