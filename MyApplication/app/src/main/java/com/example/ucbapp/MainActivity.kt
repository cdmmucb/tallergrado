package com.example.ucbapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.model.LineChartData
import com.example.ucbapp.bluetooth.BluetoothController
import com.example.ucbapp.change_password.ChangePasswordScreen
import com.example.ucbapp.db.FirebaseClient
import com.example.ucbapp.delete_account.DeleteAccountScreen
import com.example.ucbapp.edit_data.EditDataScreen

import com.example.ucbapp.history_alcohol.HistoryAlcoholScreen
import com.example.ucbapp.history_gas.HistoryGasScreen
import com.example.ucbapp.info_alcohol.InfoAlcoholScreen
import com.example.ucbapp.info_gas.InfoGasScreen
import com.example.ucbapp.log.LogScreen
import com.example.ucbapp.model.Cliente
import com.example.ucbapp.model.LogAlcohol
import com.example.ucbapp.model.LogAlcoholAgregado
import com.example.ucbapp.notification.NotificationService
import com.example.ucbapp.profile.ProfileScreen
import com.example.ucbapp.report_alcohol.ReportAlcoholScreen
import com.example.ucbapp.settings.SettingsScreen
import com.example.ucbapp.sign_in.GoogleAuthUiClient
import com.example.ucbapp.sign_in.SignInScreen
import com.example.ucbapp.sign_in.SignInViewModel
import com.example.ucbapp.ui.theme.UcbappTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MainActivity : ComponentActivity() {


    var isBluetooth: Boolean = false


    //private val bluetoothController = BluetoothController(applicationContext)
    private val bluetoothController by lazy {
        BluetoothController(
            context = applicationContext,
            //oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }


    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val firebaseClient by lazy {
        FirebaseClient(
            context = applicationContext,
            //oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    var list = mutableStateListOf<LogAlcohol>()
    var listMaxAlcohol = mutableStateListOf<LogAlcoholAgregado>()
    var pointsData = mutableStateListOf<Point>()
    var listaPrueba = mutableStateListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(googleAuthUiClient.getSignedInUser()!=null){
        CoroutineScope(Dispatchers.IO).launch {
            list.clear()
            listMaxAlcohol.clear()
            val aux = firebaseClient.getLogAlcohol(googleAuthUiClient.getSignedInUser()!!.userId)
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


            pointsData.clear()
            listaPrueba.clear()
            pointsData.add(Point(0f,0f))
            listaPrueba.add("0")

            listMaxAlcohol.forEachIndexed { i, element ->
                pointsData.add(Point(i+1.toFloat(),element.nivelAlcohol.toFloat()))
                listaPrueba.add(listMaxAlcohol[i].day.toString() + "/" +
                        listMaxAlcohol[i].month.toString()+ "/" +
                        listMaxAlcohol[i].year.toString())
                Log.v("abc","punto "+i+"="+pointsData[i])
                Log.v("abc","label"+i+"="+listaPrueba[i])
            }
            Log.v("abc","terminado")
            Log.v("abc","tamanio pointsdata = "+pointsData.size)
            Log.v("abc","tamanio listaprueba = "+listaPrueba.size)
        }}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationService.CHANNEL_ID,
                "Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Ask user to use alcoholimeter"

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)


        }

        val service = NotificationService(applicationContext)
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        setContent {
            UcbappTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "sign_in") {
                        composable("sign_in") {


                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(key1 = Unit) {
                                if (googleAuthUiClient.getSignedInUser() != null) {
                                    navController.navigate("profile")
                                }


                            }

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }

                                }
                            )

                            LaunchedEffect(key1 = state.isSignInSuccessful) {

                                if (state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("profile")
                                    viewModel.resetState()
                                }


                            }

                            SignInScreen(state = state,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )


                        }
                        composable("profile") {
                            ProfileScreen(userData = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        navController.popBackStack()
                                    }
                                },
                                onLog = {
                                    lifecycleScope.launch {

                                        if (bluetoothController.isBluetoothEnabled()) {

                                            // Log.v("termina ", "1");
                                            //bluetoothController.getPairedDevices()

                                            isBluetooth = bluetoothController.connectArduino()
                                            if (isBluetooth == false) {
                                                Toast.makeText(
                                                    applicationContext,
                                                    "No se pudo conectar con el widget",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            } else {
                                                Toast.makeText(
                                                    applicationContext,
                                                    "log",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                navController.navigate("log")
                                            }


                                        } else {
                                            Toast.makeText(
                                                applicationContext,
                                                "Para continuar debe activar bluetooth",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }


                                    }
                                },
                                onHistoryAlcohol = {
                                    lifecycleScope.launch {
                                        navController.navigate("history_alcohol")
                                    }
                                },
                                onHistoryGas = {
                                    lifecycleScope.launch {
                                        navController.navigate("history_gas")
                                    }
                                },
                                onReportAlcohol = {
                                    lifecycleScope.launch {
                                        navController.navigate("report_alcohol")
                                    }
                                },
                                onInfoGas = {
                                    lifecycleScope.launch {
                                        navController.navigate("info_gas")
                                    }
                                },
                                onInfoAlcohol = {
                                    lifecycleScope.launch {
                                        navController.navigate("info_alcohol")
                                    }
                                },
                                onSettings = {
                                    lifecycleScope.launch {
                                        navController.navigate("settings")
                                    }
                                }
                            )

                        }

                        composable("log") {
                            LogScreen(userData = googleAuthUiClient.getSignedInUser(),
                                navController,
                                bluetoothController,
                                firebaseClient,
                                service,
                                vibrator,
                                onLogGas = {
                                    lifecycleScope.launch {

                                        Toast.makeText(
                                            applicationContext,
                                            "Logging only gas",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        //navController.popBackStack()
                                    }
                                },
                                onLogAll = {
                                    lifecycleScope.launch {

                                        Toast.makeText(
                                            applicationContext,
                                            "Logging all",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        //navController.popBackStack()
                                    }
                                },
                                onEditParam = {
                                    lifecycleScope.launch {

                                        Toast.makeText(
                                            applicationContext,
                                            "editar parametros",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        //navController.popBackStack()
                                    }
                                },
                                onFinishLog = {
                                    lifecycleScope.launch {

                                        /* Toast.makeText(
                                             applicationContext,
                                             "terminar sesion",
                                             Toast.LENGTH_LONG
                                         ).show()*/
                                        // bluetoothController!!.closeSocket()
                                        // navController.popBackStack()
                                    }
                                }
                            )
                        }

                        composable("history_alcohol") {
                            HistoryAlcoholScreen(userData = googleAuthUiClient.getSignedInUser(),
                                firebaseClient,
                                onHistoryAlcohol = {
                                    lifecycleScope.launch {

                                        Toast.makeText(
                                            applicationContext,
                                            "historial alcohol",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        //navController.popBackStack()
                                    }
                                }
                            )
                        }

                        composable("history_gas") {
                            HistoryGasScreen(userData = googleAuthUiClient.getSignedInUser(),
                                firebaseClient,
                                onHistoryGas = {
                                    lifecycleScope.launch {

                                        Toast.makeText(
                                            applicationContext,
                                            "historial gas",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        //navController.popBackStack()
                                    }
                                }
                            )
                        }

                        composable("info_alcohol") {
                            InfoAlcoholScreen(
                                userData = googleAuthUiClient.getSignedInUser()
                            )
                        }

                        composable("info_gas") {
                            InfoGasScreen(
                                userData = googleAuthUiClient.getSignedInUser()
                            )
                        }

                        composable("report_alcohol") {
                            ReportAlcoholScreen(
                                pointsData,
                                listaPrueba,
                                userData = googleAuthUiClient.getSignedInUser(),
                                firebaseClient,
                                onReportAlcohol = {
                                    lifecycleScope.launch {

                                        /*Toast.makeText(
                                            applicationContext,
                                            "report alcohol",
                                            Toast.LENGTH_LONG
                                        ).show()*/

                                        //navController.popBackStack()

                                    }
                                }

                            )
                        }

                        composable("settings") {
                            SettingsScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                onChangePassword = {
                                    lifecycleScope.launch {
                                        navController.navigate("change_password")
                                    }
                                },
                                onEditData = {
                                    lifecycleScope.launch {
                                        navController.navigate("edit_data")
                                    }
                                },
                                onDeleteAccount = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.deleteUser()
                                        googleAuthUiClient.signOut()
                                        //navController.navigate("delete_account")
                                        navController.popBackStack("sign_in", false)
                                        Toast.makeText(
                                            applicationContext,
                                            "Cuenta eliminada",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                },
                            )
                        }

                        composable("change_password") {
                            ChangePasswordScreen(userData = googleAuthUiClient.getSignedInUser(),
                                onChangePassword = {
                                    lifecycleScope.launch {
                                        Toast.makeText(
                                            applicationContext,
                                            "change password",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        //navController.popBackStack()
                                    }
                                },

                                onCancelar = {
                                    lifecycleScope.launch {

                                        navController.popBackStack()
                                    }
                                }
                            )
                        }

                        composable("delete_account") {
                            DeleteAccountScreen(userData = googleAuthUiClient.getSignedInUser(),
                                onDeleteAccount = {
                                    lifecycleScope.launch {

                                        Toast.makeText(
                                            applicationContext,
                                            "borrar cuenta",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        //navController.popBackStack()
                                    }
                                },
                                onCancelar = {
                                    lifecycleScope.launch {
                                        navController.popBackStack()
                                    }
                                }
                            )
                        }

                        composable("edit_data") {
                            EditDataScreen(userData = googleAuthUiClient.getSignedInUser(),
                                firebaseClient,
                                applicationContext,
                                navController,
                                /*onEditData = {
                                    lifecycleScope.launch {

                                        Toast.makeText(
                                            applicationContext,
                                            "editar datos personales",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.popBackStack("profile",false)
                                        //navController.popBackStack()
                                    }
                                },*/
                                onCancelar = {
                                    lifecycleScope.launch {
                                        navController.popBackStack()
                                    }
                                }
                            )
                        }

                    }


                }
            }
        }

    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}


