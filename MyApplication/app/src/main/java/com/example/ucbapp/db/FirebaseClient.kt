package com.example.ucbapp.db

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.example.ucbapp.model.Cliente
import com.example.ucbapp.model.LogAlcohol
import com.example.ucbapp.model.LogGas
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate

class FirebaseClient(private val context: Context) {
    val db = Firebase.firestore

    suspend fun registrarUsuario(
        cliente: Cliente,
        navController: NavController
    ) /*= CoroutineScope(Dispatchers.IO).launch*/ {
        try {
            db.collection("clientes").add(cliente).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Registro exitoso.", Toast.LENGTH_LONG).show()
                navController.popBackStack("profile", false)
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: " + e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    /* private fun existeUsuario(cliente: Cliente, newClienteMap: Map<String, Any>) =
         CoroutineScope(Dispatchers.IO).launch {
             val clienteQuery = db.collection("clientes")
                 .whereEqualTo("id", cliente.id)
                 .get()
                 .await()

             val sb = StringBuilder()
             if (clienteQuery.documents.isNotEmpty()) {
                 for (document in clienteQuery) {
                     try {
                         //personCollectionRef.document(document.id).update("age", newAge).await()
                         val clienteAux = document.toObject<Cliente>()
                         sb.append("$clienteAux\n")


                     } catch (e: Exception) {
                         withContext(Dispatchers.Main) {
                             Toast.makeText(context, "fail registro: "+e.message, Toast.LENGTH_LONG).show()
                         }
                     }
                 }
                 withContext(Dispatchers.Main) {
                     return@withContext sb.toString()
                 }
             } else {
                 withContext(Dispatchers.Main) {
                     Toast.makeText(context, "No persons matched the query.", Toast.LENGTH_LONG)
                         .show()
                 }
             }
         }*/

    suspend fun modificarCuenta(
        cliente: Cliente/*, newClienteMap: Map<String, Any>*/,
        navController: NavController
    )/* =
        CoroutineScope(Dispatchers.IO).launch*/ {
        Log.v(
            "dentro de modificar cuenta: ", cliente.altura.toString()
                    + ";"
                    + cliente.peso.toString()
                    + ";"
                    + cliente.unidadPeso.toString()
                    + ";"
                    + cliente.unidadAltura.toString()
        );
        val clienteQuery = db.collection("clientes")
            .whereEqualTo("id", cliente.id)
            .get()
            .await()
        if (clienteQuery.documents.isNotEmpty()) {
            for (document in clienteQuery) {
                try {
                    //personCollectionRef.document(document.id).update("age", newAge).await()
                    /*db.collection("clientes").document(document.id).set(
                        newClienteMap,
                        SetOptions.merge()
                    ).await()*/

                    db.collection("clientes").document(document.id).update(
                        "peso", cliente.peso,
                        "altura", cliente.altura,
                        "unidadPeso", cliente.unidadPeso,
                        "unidadAltura", cliente.unidadAltura
                    ).await()

                    Toast.makeText(context, "Actualización exitosa.", Toast.LENGTH_LONG).show()

                    navController.popBackStack("profile", false)

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "Error de actualización: " + e.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                        Log.v("Error:", e.message.toString());
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "No se encontró el usuario",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }

    fun registrarLogAlcohol(logAlcohol: LogAlcohol) = CoroutineScope(Dispatchers.IO).launch {
        try {
            db.collection("log_alcohol").add(logAlcohol).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun registrarLogGas(logGas: LogGas) = CoroutineScope(Dispatchers.IO).launch {
        try {
            db.collection("log_gas").add(logGas).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    suspend fun getLogAlcohol(id: String): MutableList<LogAlcohol>?  /*=CoroutineScope(Dispatchers.IO).launch*/ {
        try {
            val querySnapshot = db.collection("log_alcohol")
                .whereEqualTo("id", id)
                .get().await()
            var list = mutableListOf<LogAlcohol>()
            for (document in querySnapshot.documents) {
                val logAlcohol = document.toObject<LogAlcohol>()
                list.add(logAlcohol!!)
            }

            //withContext(Dispatchers.Main) {
            // return@withContext list
            return list
            // }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                Log.v("error de log", e.toString())
                Log.v("error mensaje", e.message.toString())
            }
            return null
        }
    }

    suspend fun getLogGas(id: String): MutableList<LogGas>?  /*=CoroutineScope(Dispatchers.IO).launch*/ {
        try {
            val querySnapshot = db.collection("log_gas").whereEqualTo("id", id)
                .get().await()
            var list = mutableListOf<LogGas>()
            for (document in querySnapshot.documents) {
                val logGas = document.toObject<LogGas>()
                list.add(logGas!!)
            }

            //withContext(Dispatchers.Main) {
            // return@withContext list
            return list
            // }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.v("Error: ", e.message.toString());
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
            return null
        }
    }

    suspend fun getCliente(id: String): Cliente? /*= CoroutineScope(Dispatchers.IO).launch */ {
        try {
            val querySnapshot = db.collection("clientes")
                .whereEqualTo("id", id)
                .get().await()

            if (querySnapshot.documents.isNotEmpty()) {

                for (document in querySnapshot.documents) {
                    val cliente = document.toObject<Cliente>()
                    return cliente
                }
            }

            // withContext(Dispatchers.Main) {
            //return@withContext sb.toString()

            //}
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()

            }
            return null
        }
        return null

    }
}