package com.example.ucbapp.report_alcohol

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
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
import com.example.ucbapp.model.LogAlcoholAgregado
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
fun ReportAlcoholScreen(
    userData: UserData?,
    firebaseClient: FirebaseClient,
    onReportAlcohol: () -> Unit
) {

    var startDate by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var endDate by rememberSaveable { mutableStateOf(LocalDate.now()) }

    var list = remember { mutableStateListOf<LogAlcohol>() }
    var listMaxAlcohol = remember { mutableStateListOf<LogAlcoholAgregado>() }

    var fechaInicial by remember { mutableStateOf(LocalDate.now()) }
    var fechaFinal by remember { mutableStateOf(LocalDate.now()) }

    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd MMM yyyy")
                .format(fechaInicial)
        }
    }

    val formattedDate2 by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("dd MMM yyyy")
                .format(fechaFinal)
        }
    }

    val dateDialogState = rememberMaterialDialogState()
    val dateDialogState2 = rememberMaterialDialogState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Reporte sobre consumo de alcohol",
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                list.clear()
                listMaxAlcohol.clear()
                val aux = firebaseClient.getLogAlcohol(userData!!.userId)
                if (aux != null && aux.isNotEmpty()) {

                    list.addAll(aux)
                }

                list.sortBy {
                    LocalDateTime.of(
                        LocalDate.of(it.year, it.month, it.day),
                        LocalTime.of(it.hour, it.minute, it.second)
                    )
                }
                for (i in list.indices) {
                    if (LocalDate.of(
                            list[i].year,
                            list[i].month,
                            list[i].day
                        ) in fechaInicial..fechaFinal
                    ) {

                        if (listMaxAlcohol.size > 0) {
                            if (list[i].day == listMaxAlcohol[listMaxAlcohol.size - 1].day &&
                                list[i].month == listMaxAlcohol[listMaxAlcohol.size - 1].month &&
                                list[i].year == listMaxAlcohol[listMaxAlcohol.size - 1].year
                            ) {
                                if (list[i].nivelAlcohol > listMaxAlcohol[listMaxAlcohol.size - 1].nivelAlcohol) {
                                    listMaxAlcohol[listMaxAlcohol.size - 1].nivelAlcohol =
                                        list[i].nivelAlcohol
                                }
                            } else {
                                listMaxAlcohol.add(
                                    LogAlcoholAgregado(
                                        list[i].nivelAlcohol,
                                        list[i].unidadAlcohol,
                                        list[i].day,
                                        list[i].month,
                                        list[i].year
                                    )
                                )
                            }
                        } else {
                            listMaxAlcohol.add(
                                LogAlcoholAgregado(
                                    list[i].nivelAlcohol,
                                    list[i].unidadAlcohol,
                                    list[i].day,
                                    list[i].month,
                                    list[i].year
                                )
                            )
                        }

                    }
                }
            }
        }) {
            Text(text = "Generar reporte")
        }


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
        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                    /*Toast.makeText(
                        applicationContext,
                        "Clicked ok",
                        Toast.LENGTH_LONG
                    ).show()*/
                }
                negativeButton(text = "Cancelar")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Cambiar",
                allowedDateValidator = {
                    it <= LocalDate.now()
                }
            ) {
                fechaInicial = it
            }
        }



        MaterialDialog(
            dialogState = dateDialogState2,
            buttons = {
                positiveButton(text = "Ok") {
                    /*Toast.makeText(
                        applicationContext,
                        "Clicked ok",
                        Toast.LENGTH_LONG
                    ).show()*/
                }
                negativeButton(text = "Cancelar")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Cambiar",
                allowedDateValidator = {
                    it >= fechaInicial &&
                            it <= LocalDate.now()
                }
            ) {
                fechaFinal = it
            }
        }

        if (listMaxAlcohol.isNotEmpty()) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

            ) {

                itemsIndexed(listMaxAlcohol) { index, item ->

                    Text(text = "Máximo de alcohol en sangre: " + item.nivelAlcohol.toString() + " " + item.unidadAlcohol)
                    Text(
                        text = "Fecha: " + item.day.toString() + "/" +
                                item.month.toString() + "/" +
                                item.year.toString()
                    )
                    if (index > 0) {
                        if (item.nivelAlcohol >= listMaxAlcohol[index - 1].nivelAlcohol) {
                            Text(text = (item.nivelAlcohol - listMaxAlcohol[index - 1].nivelAlcohol).toString() + " " + item.unidadAlcohol + " más que el anterior día registrado")
                        } else {
                            Text(text = (listMaxAlcohol[index - 1].nivelAlcohol - item.nivelAlcohol).toString() + " " + item.unidadAlcohol + " menos que el anterior día registrado")
                        }
                    }
                    Divider(color = Color.Black)

                }

            }
        }
    }


}