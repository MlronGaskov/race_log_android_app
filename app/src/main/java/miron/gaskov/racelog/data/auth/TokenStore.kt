package miron.gaskov.racelog.data.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth")

data class Tokens(val access: String, val refresh: String)

class TokenStore(private val context: Context) {
    private val KEY_ACCESS = stringPreferencesKey("access_token")
    private val KEY_REFRESH = stringPreferencesKey("refresh_token")

    suspend fun getAccess(): String? =
        context.dataStore.data.map { it[KEY_ACCESS] }.firstOrNull()

    suspend fun getRefresh(): String? =
        context.dataStore.data.map { it[KEY_REFRESH] }.firstOrNull()

    suspend fun setTokens(access: String, refresh: String) {
        context.dataStore.edit {
            it[KEY_ACCESS] = access
            it[KEY_REFRESH] = refresh
        }
    }

    suspend fun updateAccess(newAccess: String) {
        context.dataStore.edit { it[KEY_ACCESS] = newAccess }
    }

    suspend fun clear() {
        context.dataStore.edit {
            it.remove(KEY_ACCESS)
            it.remove(KEY_REFRESH)
        }
    }
}
