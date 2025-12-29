package miron.gaskov.racelog.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import miron.gaskov.racelog.R
import miron.gaskov.racelog.ui.components.AppBackground
import miron.gaskov.racelog.ui.components.AppTextField
import miron.gaskov.racelog.ui.theme.BlackText
import miron.gaskov.racelog.ui.theme.Forgot
import miron.gaskov.racelog.ui.theme.Purple
import miron.gaskov.racelog.ui.util.toUiMessage

@Composable
fun LoginScreen(
    onLogin: suspend (String, String) -> Unit,
    onOpenRegister: () -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .padding(top = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo_runner),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )

            Spacer(Modifier.height(35.dp))

            Text(
                "Журнал забегов",
                style = MaterialTheme.typography.headlineSmall,
                color = BlackText,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(35.dp))

            Text(
                "Вход",
                style = MaterialTheme.typography.titleLarge,
                color = BlackText,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(35.dp))

            Column(
                modifier = Modifier.width(315.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Логин", style = MaterialTheme.typography.bodyMedium, color = androidx.compose.ui.graphics.Color.Black)
                    AppTextField(
                        value = login,
                        onValueChange = { login = it },
                        placeholder = "Введите логин"
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Пароль", style = MaterialTheme.typography.bodyMedium, color = androidx.compose.ui.graphics.Color.Black)
                    AppTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Введите пароль",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation()
                    )

                }
            }

            Spacer(Modifier.height(60.dp))

            Column(
                modifier = Modifier.width(315.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    enabled = !loading,
                    onClick = {
                        error = null
                        loading = true
                        scope.launch {
                            try {
                                onLogin(login.trim(), password)
                            } catch (e: Exception) {
                                error = e.toUiMessage()
                            } finally {
                                loading = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .shadow(12.dp, RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    Text(
                        text = if (loading) "..." else "Войти",
                        style = MaterialTheme.typography.titleLarge,
                        color = androidx.compose.ui.graphics.Color.White
                    )
                }

                Spacer(Modifier.height(15.dp))

                TextButton(onClick = onOpenRegister) {
                    Text("Зарегистрироваться", style = MaterialTheme.typography.bodyMedium, color = Purple)
                }

                TextButton(onClick = { /* TODO */ }) {
                    Text("Забыли пароль?", style = MaterialTheme.typography.bodyMedium, color = Forgot)
                }

                error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
