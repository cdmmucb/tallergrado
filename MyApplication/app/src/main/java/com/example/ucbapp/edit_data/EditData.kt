package com.example.ucbapp.edit_data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ucbapp.db.FirebaseClient
import com.example.ucbapp.model.Cliente
import com.example.ucbapp.sign_in.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun EditDataScreen(
    userData: UserData?,
    firebaseClient: FirebaseClient,
    applicationContext: Context,
    navController: NavController,
    onCancelar: () -> Unit,
) {

    var isDataPrevia by rememberSaveable { mutableStateOf(false) }
    var height by rememberSaveable { mutableStateOf("") }
    var weight by rememberSaveable { mutableStateOf("") }
    var unitWeight by rememberSaveable { mutableStateOf(0) }
    var unitHeight by rememberSaveable { mutableStateOf(0) }

    val radioOptions = listOf("Centímetros", "Pulgadas")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[unitHeight]) }
    val radioOptions2 = listOf("Kilos", "Libras")
    val (selectedOption2, onOptionSelected2) = remember { mutableStateOf(radioOptions2[unitWeight]) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Editar datos",
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = height,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            onValueChange = { height = it },
            label = { Text("Altura") }
        )

        Column {
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                            }
                        )
                        .padding(horizontal = 16.dp)
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) }
                    )
                    Text(
                        text = text,
                        /*style = MaterialTheme.typography.body1.merge(),*/
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }

        OutlinedTextField(
            value = weight,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            onValueChange = { weight = it },
            label = { Text("Peso") }
        )

        Column {
            radioOptions2.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == selectedOption2),
                            onClick = {
                                onOptionSelected2(text)
                            }
                        )
                        .padding(horizontal = 16.dp)
                ) {
                    RadioButton(
                        selected = (text == selectedOption2),
                        onClick = {
                            onOptionSelected2(text)

                        }
                    )
                    Text(
                        text = text,
                        /*style = MaterialTheme.typography.body1.merge(),*/
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }

        Button(onClick = {
            if (isDataPrevia) {
                //update
                CoroutineScope(Dispatchers.IO).launch {
                    firebaseClient.modificarCuenta(
                        Cliente(
                            userData!!.userId,
                            weight.toDouble(),
                            /*if (unitWeight == 0) {
                                "Kilos"
                            } else {
                                "Libras"
                            },*/
                            selectedOption2,
                            height.toDouble(),
                            /*if (unitHeight == 0) {
                                "Centímetros"
                            } else {
                                "Pulgadas"
                            },*/
                            selectedOption,
                            "",
                            0,
                            0,
                            0
                        ),
                        navController
                    )
                }
            } else {
                //alta
                CoroutineScope(Dispatchers.IO).launch {
                    firebaseClient.registrarUsuario(
                        Cliente(
                            userData!!.userId,
                            weight.toDouble(),
                            /*if (unitWeight == 0) {
                                "Kilos"
                            } else {
                                "Libras"
                            },*/
                            selectedOption2,
                            height.toDouble(),
                            /*if (unitHeight == 0) {
                                "Centímetros"
                            } else {
                                "Pulgadas"
                            },*/
                            selectedOption,
                            "",
                            0,
                            0,
                            0
                        ),
                        navController
                    )
                }
            }

            /*Toast.makeText(
                applicationContext,
                "exito edicion",
                Toast.LENGTH_LONG
            ).show()*/

            navController.popBackStack("profile", false)

            /* Toast.makeText(
                applicationContext,
                "fail",
                Toast.LENGTH_LONG
            ).show()*/
        }) {
            Text(text = "Confirmar cambios")
        }

        Button(onClick = onCancelar) {
            Text(text = "Cancelar")
        }

    }

    LaunchedEffect(key1 = "myKey") {
        CoroutineScope(Dispatchers.IO).launch {

            var cliente: Cliente? = firebaseClient.getCliente(userData!!.userId)
            Log.v("Log de cliente:", cliente.toString());

            if (cliente != null) {
                isDataPrevia = true
                height = cliente.altura.toString()
                weight = cliente.peso.toString()
                if (cliente.unidadAltura.toString() == "Pulgadas") {
                    unitHeight = 1
                    onOptionSelected("Pulgadas")
                }
                if (cliente.unidadPeso.toString() == "Libras") {
                    unitWeight = 1
                    onOptionSelected2("Libras")
                }

            }

        }
    }

}






