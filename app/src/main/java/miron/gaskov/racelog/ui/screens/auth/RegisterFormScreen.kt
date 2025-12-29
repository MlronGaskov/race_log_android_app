package miron.gaskov.racelog.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import miron.gaskov.racelog.R
import miron.gaskov.racelog.data.api.Dto
import miron.gaskov.racelog.data.auth.AuthRepository
import miron.gaskov.racelog.ui.components.AppBackground
import miron.gaskov.racelog.ui.components.AppTextField
import miron.gaskov.racelog.ui.theme.BlackText
import miron.gaskov.racelog.ui.theme.Purple
import miron.gaskov.racelog.ui.theme.PurplePrimary
import miron.gaskov.racelog.ui.util.toUiMessage

@Composable
fun RegisterFormScreen(
    authRepository: AuthRepository,
    onGoConfirm: (
        phone: String,
        login: String,
        password: String,
        role: String,
        testCode: String
    ) -> Unit,
    onBack: () -> Unit
) {
    var phone by remember { mutableStateOf("+7") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("ATHLETE") }

    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    fun isPhoneValid(v: String) = true
    fun isLoginValid(v: String) = true
    fun isPasswordValid(v: String) = true
    fun isRoleValid(v: String) = true

    val canSendCode = !loading

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 30.dp)
                .padding(top = 70.dp, bottom = 24.dp),
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
                "Регистрация",
                style = MaterialTheme.typography.titleLarge,
                color = BlackText,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            Column(
                modifier = Modifier.width(315.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Телефон", style = MaterialTheme.typography.bodyMedium)
                    AppTextField(
                        value = phone,
                        onValueChange = { raw ->
                            phone = raw.filter { it.isDigit() || it == '+' }
                        },
                        placeholder = "+7XXXXXXXXXX"
                    )
                    if (phone.isNotBlank() && !isPhoneValid(phone)) {
                        Text(
                            "Формат: +7 и 10 цифр (например +79991234567)",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Логин", style = MaterialTheme.typography.bodyMedium)
                    AppTextField(
                        value = login,
                        onValueChange = { login = it },
                        placeholder = "Введите логин"
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Пароль", style = MaterialTheme.typography.bodyMedium)
                    AppTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Введите пароль",
                        visualTransformation = PasswordVisualTransformation()
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Роль", style = MaterialTheme.typography.bodyMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilterChip(
                            selected = role == "ATHLETE",
                            onClick = { role = "ATHLETE" },
                            label = { Text("Спортсмен") }
                        )
                        FilterChip(
                            selected = role == "COACH",
                            onClick = { role = "COACH" },
                            label = { Text("Тренер") }
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            Column(
                modifier = Modifier.width(315.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    enabled = canSendCode,
                    onClick = {
                        error = null
                        loading = true
                        scope.launch {
                            try {
                                val resp = authRepository.registerStart(
                                    Dto.RegisterStartRequest(
                                        phone = phone.trim(),
                                        login = login.trim(),
                                        password = password,
                                        role = role
                                    )
                                )
                                onGoConfirm(
                                    phone.trim(),
                                    login.trim(),
                                    password,
                                    role,
                                    resp.code
                                )
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
                        text = if (loading) "..." else "Зарегистрироваться",
                        style = MaterialTheme.typography.titleLarge,
                        color = androidx.compose.ui.graphics.Color.White
                    )
                }

                Spacer(Modifier.height(10.dp))

                TextButton(onClick = onBack) {
                    Text("Назад", color = PurplePrimary)
                }

                error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
                }
            }
        }
    }
}
