package com.example.ucbapp.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ucbapp.sign_in.UserData

@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    onLog: () -> Unit,
    onHistoryAlcohol: () -> Unit,
    onHistoryGas: () -> Unit,
    onReportAlcohol: () -> Unit,
    onInfoGas: () -> Unit,
    onInfoAlcohol: () -> Unit,
    onSettings: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (userData?.profilePictureUrl != null) {
            AsyncImage(
                model = userData.profilePictureUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (userData?.username != null) {
            Text(
                text = userData.username,
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Button(onClick = onSignOut) {
            Text(text = "Cerrar Sesión")
        }

        Button(onClick = onLog) {
            Text(text = "Log")
        }
        Button(onClick = onHistoryAlcohol) {
            Text(text = "Historial de consumo de Alcohol")
        }
        Button(onClick = onHistoryGas) {
            Text(text = "Historial de exposición a Gases")
        }
        Button(onClick = onReportAlcohol) {
            Text(text = "Reporte de consumo de Alcohol")
        }
        Button(onClick = onInfoGas) {
            Text(text = "Información de gases")
        }
        Button(onClick = onInfoAlcohol) {
            Text(text = "Información de alcohol")
        }
        Button(onClick = onSettings) {
            Text(text = "Ajustes")
        }


    }
}