package com.example.ucbapp.info_alcohol

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ucbapp.sign_in.UserData

@Composable
fun InfoAlcoholScreen(
    userData: UserData?
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            text = "Información sobre consumo de alcohol",
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "El consumo excesivo de alcohol, que incluye las borracheras, es una actividad de alto riesgo.\n" +
                        "\n" +
                        "La definición de consumo excesivo se basa en el sexo de la persona. Para las mujeres, más de tres bebidas en un día cualquiera o más de siete bebidas por semana se considera consumo excesivo. Para los hombres, consumo excesivo significa más de cuatro bebidas en un día cualquiera o más de catorce por semana.\n" +
                        "\n" +
                        "Las borracheras son comportamientos que aumentan los niveles de alcohol en sangre a 0,08 %. Por lo general, eso se refiere a cuatro o más bebidas dentro de un período de dos horas para las mujeres y cinco o más bebidas dentro de un período de dos horas para los hombres.\n" +
                        "\n" +
                        "El consumo excesivo de alcohol puede aumentar el riesgo de tener problemas de salud graves, como por ejemplo:\n" +
                        "\n" +
                        "Determinados tipos de cáncer, como cáncer colorrectal, cáncer de mama y cáncer de boca, garganta, esófago e hígado\n" +
                        "Enfermedad hepática.\n" +
                        "Enfermedad cardiovascular, como presión arterial alta y accidente cerebrovascular\n" +
                        "El consumo excesivo de alcohol también se ha asociado con lesiones intencionales, como suicidio, al igual que accidentales y la muerte.\n" +
                        "\n" +
                        "Durante el embarazo, la bebida puede causar daño cerebral y otros problemas en el feto. El consumo excesivo también puede dar lugar a síntomas de abstinencia alcohólica."
            )

            Text(text = "Fuente: Mayo Clinic")

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¿Qué es beber con moderación?\n" +
                        "Las Guías alimentarias para los estadounidenses definen beber con moderación como el consumo de 1 trago o menos al día en las mujeres y 2 tragos o menos al día en los hombres. Además, las Guías alimentarias no recomiendan que personas que no beben alcohol comiencen a beber por cualquier razón. Beber menos es mejor para la salud que beber más.4\n" +
                        "\n" +
                        "Sin embargo, algunas personas no deberían consumir ninguna cantidad de alcohol, como las siguientes:\n" +
                        "\n" +
                        "Las personas menores de 21 años.\n" +
                        "Las mujeres embarazadas o que podrían estar embarazadas.\n" +
                        "Las personas que están manejando, planeando manejar o participando en otras actividades que requieran destreza, coordinación y estar alerta.\n" +
                        "Las personas que están tomado medicamentos recetados o sin receta médica que pueden causar reacciones adversas si se mezclan con el alcohol.\n" +
                        "Las personas que sufren afecciones que pueden empeorar si se consume alcohol.\n" +
                        "Las personas que se están recuperando del alcoholismo o que no pueden controlar la cantidad que beben.\n" +
                        "Si sigue la información de las Guías alimentarias para los estadounidenses, usted puede reducir el riesgo de sufrir daños o de lastimar a otras personas."
            )

            Text(text = "Fuente: CDC")
        }

    }


}