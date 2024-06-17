package com.example.ucbapp.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.util.UUID


@SuppressLint("MissingPermission")
class BluetoothController(private val context: Context) : Thread() {

    val bluetoothManager: BluetoothManager = context.getSystemService(BluetoothManager::class.java)
    val bluetoothAdapter: BluetoothAdapter = bluetoothManager.getAdapter()
    var bluetoothSocket: BluetoothSocket? = null

    fun isBluetoothEnabled(): Boolean {
        if (bluetoothAdapter?.isEnabled == false) {
            /*val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)*/
            return false
        } else {
            return true
        }
    }

    /* fun connectArduino(){
         try{
         val device = bluetoothAdapter.getRemoteDevice("98:D3:81:FD:5C:C5")
         val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
             device.createRfcommSocketToServiceRecord(UUID.randomUUID())
         }
             Log.v("device: ", device.toString());
             Log.v("mmsocket: ", mmSocket.toString());
         bluetoothAdapter?.cancelDiscovery()

             mmSocket?.let { socket ->
                 // Connect to the remote device through the socket. This call blocks
                 // until it succeeds or throws an exception.

                socket.connect()

                 // The connection attempt succeeded. Perform work associated with
                 // the connection in a separate thread.

             }

         mmSocket?.let { manageMyConnectedSocket(it) }
         //mmSocket?.let { stopUsingResources(it) }

         }

         catch (e:Exception){
             Log.v("error:", e.message.toString())
         }
     }*/


    //mmSocket?.let { stopUsingResources(it) }

    fun connectArduino(): Boolean {
        try {
            val device = bluetoothAdapter.getRemoteDevice("98:D3:81:FD:5C:C5")
            bluetoothSocket =
                device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))

            Log.v("device: ", device.toString());
            Log.v("socket: ", bluetoothSocket.toString());
            //bluetoothAdapter?.cancelDiscovery()

            bluetoothSocket?.let { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.

                socket.connect()
                ;
                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.


            }

            return true

        } catch (e: Exception) {
            Log.v("Error:", e.message.toString())
            return false
        }

    }

    fun listenForIncomingMessages()/*: Flow<String>*/ {
        /*return flow {
            if(!bluetoothSocket!!.isConnected) {
                return@flow
            }*/
        var string = ""
        val buffer = ByteArray(1024)
        while (true) {
            val byteCount = try {
                bluetoothSocket!!.inputStream.read(buffer)
            } catch (e: IOException) {
                //throw TransferFailedException()
                Log.v("Error: ", e.message.toString());
            }

            //emit(
            string = buffer.decodeToString(
                endIndex = byteCount
            ).toString()
            Log.v("Datos recibidos: ", string);
            // )
        }
    }/*.flowOn(Dispatchers.IO)*/
    // }

    fun uploadData(isLogAll: Boolean/*setData: Unit*/): Flow<String> = flow {


        var mmInStream = bluetoothSocket!!.inputStream


        var numBytes: Int // bytes returned from read()
        val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream
        // Keep listening to the InputStream until an exception occurs.
        var aux: String = ""
        var mensaje: String = ""

        while (true) {

            // Read from the InputStream.
            numBytes = try {

                mmInStream.read(mmBuffer)


            } catch (e: Exception) {
                Log.v("Fail ", "Input stream was disconnected", e)
                break

            }


            aux = mmBuffer.decodeToString(
                endIndex = numBytes
            ).toString()

            aux = aux.replace(" ", "")



            mensaje += aux


            if (mensaje.last() == 'z') {


                emit(mensaje)

                mensaje = ""
            } else {

            }


        }

    }.flowOn(Dispatchers.Default)

    fun closeSocket() {
        try {
            bluetoothSocket!!.close()
        } catch (e: IOException) {
            Log.v("Fallo", "Could not close the client socket ", e)
        }
    }

    private fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

}
