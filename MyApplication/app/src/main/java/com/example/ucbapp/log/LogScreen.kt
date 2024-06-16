package com.example.ucbapp.log

import android.bluetooth.BluetoothSocket
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ucbapp.bluetooth.BluetoothController
import com.example.ucbapp.db.FirebaseClient
import com.example.ucbapp.model.LogAlcohol
import com.example.ucbapp.model.LogGas
import com.example.ucbapp.notification.NotificationService
import com.example.ucbapp.sign_in.UserData
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun LogScreen(
    userData: UserData?,
    navController: NavController,
    bluetoothController: BluetoothController?,
    firebaseClient: FirebaseClient,
    service: NotificationService,
    vibrator: Vibrator,
    onLogGas: () -> Unit,
    onLogAll: () -> Unit,
    onEditParam: () -> Unit,
    onFinishLog: () -> Unit,
) {

    var isLogAll by rememberSaveable { mutableStateOf(false) }
    var isLogging by rememberSaveable { mutableStateOf(false) }
    var isAlarma by rememberSaveable { mutableStateOf(false) }

    var data by rememberSaveable { mutableStateOf("") }
    //var flow by rememberSaveable { mutableStateOf("") }
    var textoData by rememberSaveable { mutableStateOf("") }
    var textoAlarma by rememberSaveable { mutableStateOf("") }

    var pickedTime by remember { mutableStateOf(LocalTime.now()) }
    var actualTime by remember { mutableStateOf(LocalTime.now()) }
    var tiempoDisponible by remember { mutableStateOf(0.0) }
    var tiempoParaSobriedad by remember { mutableStateOf(0.0) }
    var m by remember { mutableStateOf(if (pickedTime.hour < 13) "am" else "pm") }

    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm")
                .format(pickedTime)
        }
    }

    val radioOptions = listOf("Si", "No")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (isLogging) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Monitoreo en Curso",
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isAlarma) {
                //PANTALLA ALARMA
                Text(
                    text = textoAlarma,
                    textAlign = TextAlign.Center,
                    fontSize = 26.sp,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    vibrator.cancel()
                    isAlarma = false
                }) {
                    Text(text = "Apagar alarma")

                }
            } else {
                Text(textoData)

            }


        } else {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Parámetros",
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                //onLogGas
                isLogging = true
                isLogAll = false

                CoroutineScope(Dispatchers.Default).launch {
                    try{
                        bluetoothController!!.uploadData(isLogAll).collect { value ->
                            Log.v("abc", "me rio " + value)
                            //modificar data
                        }
                    }
                    catch(e: Exception){
                        Log.v("abc", "exception "+e.message+ "    completo   "+e.toString())
                    }

                }


            }) {
                Text(text = "Solo monitorear exposición a gases nocivos")
            }

            Button(onClick = {
                //onLogAll
                isLogging = true
                isLogAll = true
                CoroutineScope(Dispatchers.Default).launch {
                    try{
                    bluetoothController!!.uploadData(isLogAll).collect { value ->
//modificar data

                        var lol =value.substring(0, value.length - 1)
                        Log.v("abc", "me rio cortado:   " + lol)
                        var cadena = lol.split(",").toTypedArray()

                        firebaseClient.registrarLogGas(
                            LogGas(
                                userData!!.userId,
                                "CH4 (metano)",
                                cadena.get(1).toDouble(),
                                "ppm",
                                LocalDate.now().dayOfMonth,
                                LocalDate.now().monthValue,
                                LocalDate.now().year,
                                LocalTime.now().hour,
                                LocalTime.now().minute,
                                LocalTime.now().second
                            )
                        )

                        firebaseClient.registrarLogGas(
                            LogGas(
                                userData!!.userId,
                                "CO (monóxido de carbono)",
                                cadena.get(2).toDouble(),
                                "ppm",
                                LocalDate.now().dayOfMonth,
                                LocalDate.now().monthValue,
                                LocalDate.now().year,
                                LocalTime.now().hour,
                                LocalTime.now().minute,
                                LocalTime.now().second
                            )
                        )

                        textoData = "CH4 (metano) " + cadena.get(1) + "ppm; CO (monóxido de carbono): " + cadena.get(2)+" ppm"

                        if (isLogAll) {
                            firebaseClient.registrarLogAlcohol(
                                LogAlcohol(
                                    userData.userId,
                                    cadena.get(0).toDouble(),
                                    "mg/L",
                                    LocalDate.now().dayOfMonth,
                                    LocalDate.now().monthValue,
                                    LocalDate.now().year,
                                    LocalTime.now().hour,
                                    LocalTime.now().minute,
                                    LocalTime.now().second
                                )
                            )
                        }

                        val bracUsuario = cadena.get(0).toDouble()
                        if (selectedOption == radioOptions[0]) {
                            //usuario conducira

                            if (bracUsuario > 0.25) {


                                actualTime = LocalTime.now()
                                if (pickedTime > actualTime) {
                                    tiempoDisponible =
                                        (pickedTime.hour.toDouble() + (pickedTime.minute.toDouble() / 60.0)) - (actualTime.hour.toDouble() + (actualTime.minute.toDouble() / 60.0))
                                } else {
                                    tiempoDisponible =
                                        (pickedTime.hour.toDouble() + 24 + (pickedTime.minute.toDouble() / 60.0)) - (actualTime.hour.toDouble() + (actualTime.minute.toDouble() / 60.0))
                                }

                                tiempoParaSobriedad = (2 * (bracUsuario -0.25)) / 0.015

                                if (tiempoParaSobriedad > tiempoDisponible) {
                                    textoData+="Su nivel de alcohol es "+(2*bracUsuario)+"(BAC). Debe dejar de beber durante "+tiempoParaSobriedad+" horas para poder conducir. No podrá conducir a las "+formattedTime+m+" horas. Se aclara que estos datos no son exactos y se le aconseja no conducir a menos que su nivel de alcohol sea 0. (Este mensaje sale si el BrAC del usuario está por encima del límite legal de conducción)"

                                } else {
                                    textoData+="Su nivel de alcohol es "+(2*bracUsuario)+"(BAC). Debe dejar de beber durante "+tiempoParaSobriedad+" horas para poder conducir. Se aclara que estos datos no son exactos y se le aconseja no conducir a menos que su nivel de alcohol sea 0. (Este mensaje sale si el BrAC del usuario está por encima del límite legal de conducción)"
                                }

                            } else {
                                textoData+="Su nivel de alcohol es "+(2*bracUsuario)+"(BAC). Puede conducir. Se encuentra dentro del límite legal de alcohol en sangre. Se aclara que estos datos no son exactos y se le aconseja no conducir a menos que su nivel de alcohol sea 0. (Este mensaje sale si el BrAC del usuario está por debajo del límite legal de conducción)"
                            }
                        } else {
                            //usuario no conducira

                            if (bracUsuario > 0.25){
                                tiempoParaSobriedad = (2 * (bracUsuario -0.25)) / 0.015
                                textoData+="Su nivel de alcohol es "+(2*bracUsuario)+"(BAC). Debe dejar de beber durante "+tiempoParaSobriedad+" horas para estar sobrio. Se aclara que estos datos no son exactos y se le aconseja no conducir a menos que su nivel de alcohol sea 0. (Este mensaje sale si el BrAC del usuario está por encima del límite legal de conducción)"
                            }else{
                                textoData+="Su nivel de alcohol es "+(2*bracUsuario)+"(BAC). Está sobrio. Se aclara que estos datos no son exactos y se le aconseja no conducir a menos que su nivel de alcohol sea 0. (Este mensaje sale si el BrAC del usuario está por debajo del límite legal de conducción)"
                            }
                        }

                    //}

                                if(cadena.get(1).toDouble()>1000.0){
                                    isAlarma=true

                                    if(cadena.get(2).toDouble()>200.0){
                                        textoAlarma="CH4 (metano) elevado... y CO (monóxido de carbono) elevado..."
                                    }else{
                                        textoAlarma="CH4 (metano) elevado..."
                                    }
                                }else{
                                    if(cadena.get(2).toDouble()>200.0){
                                        isAlarma=true
                                        textoAlarma="CO (monóxido de carbono) elevado..."
                                    }else{
                                    }

                                }

                                //si el estado alarma fue activado lanzar vibracion
                                if (isAlarma) {
                                    // Handling vibrations for Android 8.0 (Oreo) and above
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        val effect = VibrationEffect.createOneShot(
                                            1000000,
                                            VibrationEffect.DEFAULT_AMPLITUDE
                                        )
                                        vibrator.vibrate(effect)
                                    } else {
                                        // Handling vibrations for devices below Android 8.0
                                        vibrator.vibrate(1000000)
                                    }
                                }


                        //modificar data
                    }
                    }
                    catch(e: Exception){
                        Log.v("abc", "exception "+e.message+ "    completo   "+e.toString())
                    }

                }




            }) {
                Text(text = "Monitorear gases nocivos y alcohol en sangre")

            }


            val timeDialogState = rememberMaterialDialogState()

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly

            ) {
                Text(text = "Hora de conducción: " + formattedTime + " " + m)

            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                timeDialogState.show()
            }) {
                Text(text = "Cambiar hora de conducción")
            }

            Spacer(modifier = Modifier.height(16.dp))



            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Conduciré: ")



                Column {
                    radioOptions.forEach { text ->
                        Row(
                            Modifier

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

            }




            MaterialDialog(
                dialogState = timeDialogState,
                buttons = {
                    positiveButton(text = "Ok") {
                        /*Toast.makeText(
                            applicationContext,
                            "Clicked ok",
                            Toast.LENGTH_LONG
                        ).show()*/

                    }
                    negativeButton(text = "Cancel")
                }
            ) {
                timepicker(
                    initialTime = LocalTime.now(),
                    title = "Pick a time",
                    /*timeRange = LocalTime.MIDNIGHT..LocalTime.NOON*/
                ) {
                    pickedTime = it
                    m = if (pickedTime.hour < 13) "am" else "pm"
                }
            }
        }


    }

    BackHandler {
        bluetoothController!!.closeSocket()
        navController.popBackStack("profile", false)
    }
}