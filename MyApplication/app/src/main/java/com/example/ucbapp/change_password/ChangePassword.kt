package com.example.ucbapp.change_password


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ucbapp.sign_in.UserData
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun ChangePasswordScreen(
    userData: UserData?,
    onChangePassword: () -> Unit,
    onCancelar: () -> Unit,
) {

    var oldPass by rememberSaveable { mutableStateOf("") }
    var newPass1 by rememberSaveable { mutableStateOf("") }
    var newPass2 by rememberSaveable { mutableStateOf("") }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Cambiar contraseña",
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = oldPass,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { oldPass = it },
            label = { Text("Contraseña actual") }
        )

        OutlinedTextField(
            value = newPass1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { newPass1 = it },
            label = { Text("Nueva contraseña") }
        )

        OutlinedTextField(
            value = newPass2,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { newPass2 = it },
            label = { Text("Repita la nueva contraseña") }
        )

        Button(onClick = onChangePassword) {
            Text(text = "Cambiar contraseña")
        }

        Button(onClick = onCancelar) {
            Text(text = "Cancelar")
        }


    }


}

