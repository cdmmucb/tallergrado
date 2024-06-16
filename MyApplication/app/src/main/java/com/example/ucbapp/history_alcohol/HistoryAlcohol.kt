package com.example.ucbapp.history_alcohol

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ucbapp.db.FirebaseClient
import com.example.ucbapp.model.LogAlcohol
import com.example.ucbapp.model.LogGas
import com.example.ucbapp.sign_in.UserData
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HistoryAlcoholScreen(
    userData: UserData?,
    firebaseClient: FirebaseClient,
    onHistoryAlcohol: () -> Unit,
) {

    var startDate by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var endDate by rememberSaveable { mutableStateOf(LocalDate.now()) }

    var list = remember { mutableStateListOf<LogAlcohol>() }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Historial de consumo de alcohol",
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))


        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                list.clear()

                val aux = firebaseClient.getLogAlcohol(userData!!.userId)
                if (aux != null && aux.isNotEmpty()) {

                    list.addAll(aux)
                }
                Log.v("Lista de niveles de alcohol:", aux.toString());
                Log.v("Lista de niveles de alcohol 2:", list.toString());
            }
        }) {
            Text(text = "Generar historial sobre consumo de alcohol")
        }

        var fechaInicial by remember { mutableStateOf(LocalDate.now()) }
        var fechaFinal by remember { mutableStateOf(LocalDate.now()) }

        val formattedDate by remember {
            derivedStateOf {
                DateTimeFormatter.ofPattern("dd MMM yyyy").format(fechaInicial)
            }
        }

        val formattedDate2 by remember {
            derivedStateOf {
                DateTimeFormatter.ofPattern("dd MMM yyyy").format(fechaFinal)
            }
        }

        val dateDialogState = rememberMaterialDialogState()
        val dateDialogState2 = rememberMaterialDialogState()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Text(text = "Fecha inicial: " + formattedDate)

                Button(onClick = {
                    dateDialogState.show()
                }) {
                    Text(text = "Cambiar")
                }
            }

            Row {
                Text(text = "Fecha final: " + formattedDate2)

                Button(onClick = {
                    dateDialogState2.show()
                }) {
                    Text(text = "Cambiar")
                }

            }

            Spacer(modifier = Modifier.height(16.dp))
        }
        MaterialDialog(dialogState = dateDialogState, buttons = {
            positiveButton(text = "Ok") {
                /*Toast.makeText(
                    applicationContext,
                    "Clicked ok",
                    Toast.LENGTH_LONG
                ).show()*/
            }
            negativeButton(text = "Cancelar")
        }) {
            datepicker(initialDate = LocalDate.now(), title = "Cambiar", allowedDateValidator = {
                it <= LocalDate.now()
            }) {
                fechaInicial = it
            }
        }



        MaterialDialog(dialogState = dateDialogState2, buttons = {
            positiveButton(text = "Ok") {
                /*Toast.makeText(
                    applicationContext,
                    "Clicked ok",
                    Toast.LENGTH_LONG
                ).show()*/
            }
            negativeButton(text = "Cancelar")
        }) {
            datepicker(initialDate = LocalDate.now(), title = "Cambiar", allowedDateValidator = {
                it >= fechaInicial && it <= LocalDate.now()
            }) {
                fechaFinal = it
            }
        }



        if (list.isNotEmpty()) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

            ) {
                list.sortBy {
                    LocalDateTime.of(
                        LocalDate.of(it.year, it.month, it.day),
                        LocalTime.of(it.hour, it.minute, it.second)
                    )
                }
                items(list) { item ->
                    if (LocalDate.of(item.year, item.month, item.day) in fechaInicial..fechaFinal) {
                        Text(text = item.nivelAlcohol.toString() + " " + item.unidadAlcohol)
                        Text(
                            text = "Fecha: " + item.day.toString() + "/" +
                                    item.month.toString() + "/" +
                                    item.year.toString() + "   Hora: " +
                                    item.hour.toString() + ":" +
                                    item.minute.toString() + ":" +
                                    item.second.toString()
                        )
                        Divider(color = Color.Black)
                    }
                }
            }
        }
    }
}