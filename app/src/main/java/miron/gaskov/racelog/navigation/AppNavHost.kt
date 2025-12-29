package miron.gaskov.racelog.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import miron.gaskov.racelog.data.auth.AuthRepository
import miron.gaskov.racelog.ui.components.BottomBar
import miron.gaskov.racelog.ui.screens.auth.LoginScreen
import miron.gaskov.racelog.ui.screens.auth.RegisterConfirmScreen
import miron.gaskov.racelog.ui.screens.auth.RegisterFormScreen
import miron.gaskov.racelog.ui.screens.main.GroupsStubScreen
import miron.gaskov.racelog.ui.screens.main.ProfileStubScreen
import miron.gaskov.racelog.ui.screens.main.ResultsStubScreen
import miron.gaskov.racelog.ui.screens.main.SplashScreen

@Composable
fun AppNavHost(authRepo: AuthRepository) {
    val nav = rememberNavController()
    val scope = rememberCoroutineScope()

    val backStack by nav.currentBackStackEntryAsState()
    val route = backStack?.destination?.route

    val showBottomBar = route in setOf(
        Route.Profile.path,
        Route.Groups.path,
        Route.Results.path
    )

    Scaffold(
        bottomBar = { if (showBottomBar) BottomBar(nav) }
    ) { padding ->

        NavHost(
            navController = nav,
            startDestination = Route.Splash.path,
            modifier = Modifier.padding(padding)
        ) {

            composable(Route.Splash.path) {
                SplashScreen()

                LaunchedEffect(Unit) {
                    val ok = authRepo.bootstrap()
                    nav.navigate(if (ok) Route.Profile.path else Route.Login.path) {
                        popUpTo(Route.Splash.path) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            composable(Route.Login.path) {
                LoginScreen(
                    onLogin = { login, pass ->
                        authRepo.login(login.trim(), pass)
                        nav.navigate(Route.Profile.path) {
                            popUpTo(Route.Login.path) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onOpenRegister = {
                        nav.navigate(Route.RegisterForm.path) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Route.RegisterForm.path) {
                RegisterFormScreen(
                    authRepository = authRepo,
                    onGoConfirm = { phone, login, password, role, testCode ->
                        nav.currentBackStackEntry?.savedStateHandle?.apply {
                            set("phone", phone)
                            set("login", login)
                            set("password", password)
                            set("role", role)
                            set("testCode", testCode)
                        }
                        nav.navigate(Route.RegisterConfirm.path) {
                            launchSingleTop = true
                        }
                    },
                    onBack = { nav.popBackStack() }
                )
            }

            composable(Route.RegisterConfirm.path) {
                val prev = nav.previousBackStackEntry?.savedStateHandle

                val phone = prev?.get<String>("phone").orEmpty()
                val login = prev?.get<String>("login").orEmpty()
                val password = prev?.get<String>("password").orEmpty()
                val role = prev?.get<String>("role") ?: "ATHLETE"
                val testCode = prev?.get<String>("testCode").orEmpty()

                RegisterConfirmScreen(
                    authRepository = authRepo,
                    phone = phone,
                    login = login,
                    password = password,
                    role = role,
                    initialTestCode = testCode,
                    onRegisterSuccess = {
                        nav.navigate(Route.Profile.path) {
                            popUpTo(Route.Login.path) { inclusive = true }
                            popUpTo(Route.RegisterForm.path) { inclusive = true }
                            popUpTo(Route.RegisterConfirm.path) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onBack = { nav.popBackStack() }
                )
            }

            composable(Route.Profile.path) {
                ProfileStubScreen(
                    onLogout = {
                        scope.launch {
                            authRepo.logout()

                            nav.navigate(Route.Login.path) {
                                popUpTo(nav.graph.findStartDestination().id) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            composable(Route.Groups.path) { GroupsStubScreen() }
            composable(Route.Results.path) { ResultsStubScreen() }
        }
    }
}
