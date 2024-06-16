package com.example.ucbapp.settings

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
fun SettingsScreen(
    userData: UserData?,
    onChangePassword: () -> Unit,
    onDeleteAccount: () -> Unit,
    onEditData: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Ajustes",
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))

        /*Button(onClick = onChangePassword) {
            Text(text = "Cambiar contraseña")
        }*/

        Button(onClick = onDeleteAccount) {
            Text(text = "Borrar cuenta")
        }

        Button(onClick = onEditData) {
            Text(text = "Cambiar datos personales")
        }


    }
}