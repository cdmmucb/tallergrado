package com.example.ucbapp.model

data class Cliente(
    var id: String = "",
    var peso: Double = 0.0,
    var unidadPeso: String = "",
    var altura: Double = 0.0,
    var unidadAltura: String = "",
    var sexo: String =  "",
    var day: Int = 0,
    var month: Int = 0,
    var year: Int = 0
)
