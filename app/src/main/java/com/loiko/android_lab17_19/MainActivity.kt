package com.loiko.android_lab17_19

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.loiko.android_lab17_19.ui.theme.Android_lab17_19Theme
import com.loiko.android_lab17_19.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Android_lab17_19Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WeatherDashboardScreen()
                }
            }
        }
    }
}

@Composable
fun WeatherDashboardScreen(viewModel: WeatherViewModel = viewModel()) {
    val weatherState by viewModel.weatherState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "⛅ Weather Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        WeatherCard(
            emoji = "🌡️",
            title = "Температура",
            value = weatherState.temperature?.let { "$it°C" } ?: "—",
            isLoading = weatherState.isLoading && weatherState.temperature == null
        )

        Spacer(modifier = Modifier.height(8.dp))

        WeatherCard(
            emoji = "💧",
            title = "Влажность",
            value = weatherState.humidity?.let { "$it%" } ?: "—",
            isLoading = weatherState.isLoading && weatherState.humidity == null
        )

        Spacer(modifier = Modifier.height(8.dp))

        WeatherCard(
            emoji = "🌪️",
            title = "Скорость ветра",
            value = weatherState.windSpeed?.let { "$it м/с" } ?: "—",
            isLoading = weatherState.isLoading && weatherState.windSpeed == null
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (weatherState.weatherIndex != null) {
            WeatherCard(
                emoji = "📊",
                title = "Weather Index",
                value = "${weatherState.weatherIndex}",
                isLoading = false
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.loadWeatherData() },
            enabled = !weatherState.isLoading
        ) {
            Text("🔄 Refresh Weather")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { viewModel.toggleErrorSimulation() }
        ) {
            Text("⚠️ Simulate Error")
        }

        if (weatherState.error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = weatherState.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (weatherState.loadingProgress.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = weatherState.loadingProgress,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Автообновление каждые 10 сек",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
fun WeatherCard(
    emoji: String,
    title: String,
    value: String,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = emoji, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, style = MaterialTheme.typography.bodyLarge)
            }
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text(text = value, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
