package miron.gaskov.racelog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import miron.gaskov.racelog.data.NetworkModule
import miron.gaskov.racelog.data.auth.AuthRepository
import miron.gaskov.racelog.navigation.AppNavHost
import miron.gaskov.racelog.ui.theme.RaceLogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val baseUrl = "http://10.0.2.2:8080/"
        val (api, tokenStore) = NetworkModule.createApi(applicationContext, baseUrl)
        val authRepo = AuthRepository(api, tokenStore)

        setContent {
            RaceLogTheme {
                AppNavHost(authRepo = authRepo)
            }
        }
    }
}
