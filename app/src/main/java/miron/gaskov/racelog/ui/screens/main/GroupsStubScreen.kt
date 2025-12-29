package miron.gaskov.racelog.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GroupsStubScreen() {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Мои группы", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        Text("Заглушка. Позже: список групп + создать/войти по коду.")
    }
}
