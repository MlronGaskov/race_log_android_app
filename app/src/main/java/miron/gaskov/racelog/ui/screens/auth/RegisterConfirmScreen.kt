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
fun RegisterConfirmScreen(
    authRepository: AuthRepository,
    phone: String,
    login: String,
    password: String,
    role: String,
    initialTestCode: String,
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit
) {
    var smsCode by remember { mutableStateOf("") }
    var testCode by remember { mutableStateOf(initialTestCode) }

    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    val canConfirm = !loading
    val canResend = !loading

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
                "Подтверждение",
                style = MaterialTheme.typography.titleLarge,
                color = BlackText,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(60.dp))

            Column(modifier = Modifier.width(315.dp)) {
                Text(
                    text = "Тестовый код: $testCode",
                    color = PurplePrimary,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("Код из SMS", style = MaterialTheme.typography.bodyMedium)
                    AppTextField(
                        value = smsCode,
                        onValueChange = { smsCode = it.filter(Char::isDigit) },
                        placeholder = "Введите код"
                    )
                }

                Spacer(Modifier.height(40.dp))

                Button(
                    enabled = canConfirm,
                    onClick = {
                        error = null
                        loading = true
                        scope.launch {
                            try {
                                authRepository.registerConfirm(
                                    confirm = Dto.RegisterConfirmRequest(phone = phone, code = smsCode.trim()),
                                    login = login,
                                    password = password,
                                    role = role
                                )
                                onRegisterSuccess()
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
                        text = if (loading) "..." else "Подтвердить",
                        style = MaterialTheme.typography.titleLarge,
                        color = androidx.compose.ui.graphics.Color.White
                    )
                }

                Spacer(Modifier.height(10.dp))

                TextButton(
                    enabled = canResend,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        error = null
                        loading = true
                        scope.launch {
                            try {
                                val resp = authRepository.registerStart(
                                    Dto.RegisterStartRequest(
                                        phone = phone,
                                        login = login,
                                        password = password,
                                        role = role
                                    )
                                )
                                testCode = resp.code
                            } catch (e: Exception) {
                                error = e.toUiMessage()
                            } finally {
                                loading = false
                            }
                        }
                    }
                ) {
                    Text("Отправить код ещё раз", color = PurplePrimary)
                }

                TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
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
