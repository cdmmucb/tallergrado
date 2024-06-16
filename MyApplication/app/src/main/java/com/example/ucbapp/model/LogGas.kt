package com.example.ucbapp.model

import androidx.annotation.Keep

@Keep
data class LogGas(
    var id: String = "",
    var nombreGas: String = "",
    var nivelGas: Double = 0.0,
    var unidadGas: String = "",
    var day: Int = 0,
    var month: Int = 0,
    var year: Int = 0,
    var hour: Int = 0,
    var minute: Int = 0,
    var second: Int = 0,
)
