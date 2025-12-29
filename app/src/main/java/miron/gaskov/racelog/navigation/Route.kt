package miron.gaskov.racelog.navigation

sealed class Route(val path: String) {
    data object Splash : Route("splash")
    data object Login : Route("login")

    data object RegisterForm : Route("register_form")
    data object RegisterConfirm : Route("register_confirm")

    data object Profile : Route("profile")
    data object Groups : Route("groups")
    data object Results : Route("results")
}
