package com.example.ucbapp.model

import androidx.annotation.Keep

@Keep
data class LogAlcoholAgregado(
    //var id: String = "",
    var nivelAlcohol: Double = 0.0,
    var unidadAlcohol: String = "",
    var day: Int = 0,
    var month: Int = 0,
    var year: Int = 0,
)