package miron.gaskov.racelog.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResultsStubScreen() {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Результаты", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        Text("Заглушка. Позже: дисциплины + лучшие/последние результаты + добавление.")
    }
}
