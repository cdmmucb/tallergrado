package com.example.ucbapp.info_gas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
fun InfoGasScreen(
    userData: UserData?
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Información sobre gases nocivos",
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Los expertos en detección de gases han definido como gas tóxico aquel que es capaz de causar daño a tejidos vivos, discapacidad del sistema nervioso central, enfermedades graves o incluso muerte cuando es ingerido, inhalado o absorbido por la piel u ojos. Técnicamente, los gases se consideran tóxicos si su concentración letal media es mayor a 200 partes por millón (ppm). (GDS Corp, 2020)\n" +
                        "\n" +
                        "Los 5 gases tóxicos más comunes son:\n" +
                        "\n" +
                        "-\tSulfuro de hidrógeno: este gas se suele identificar por su olor a huevo podrido. El sulfuro de hidrogeno (H2S) se encuentra en procesos de manufactura y sustancias químicas. Los pesticidas, plásticos, farmacéuticos, vertederos e incluso cervecerías emiten sulfuro de hidrógeno como subproducto. Los niveles de toxicidad del sulfuro de hidrógeno son altamente peligrosos. Especialmente, si no son desechados inadecuadamente. La inhalación de concentraciones altas de H2S puede resultar en irritación, inconsciencia, pérdida de memoria y en casos severos muerte instantánea.\n" +
                        "OSHA recomienda un límite de exposición de 10 minutos a 10 ppm para trabajadores. Además, la exposición a 100 ppm de H2S ha probado ser letal de manera inmediata.\n" +
                        "\n" +
                        "-\tMonóxido de carbono: se suele encontrar en procesos industriales como fuente de energía y agente reductor. El monóxido de carbono (CO) no tiene color, no huele y no tiene sabor. Cuando se queman materiales de manera inapropiada, las emisiones de monóxido de carbono son altamente nocivas. Sobre todo, en áreas repletas de gente donde no se monitorea la exposición. Los efectos son nausea, intranquilidad, euforia y la exposición puede llegar a ser letal.\n" +
                        "OSHA recomienda 50 ppm para trabajadores durante un turno laboral de 8 horas y los trabajadores marítimos deben prestar más atención si la concentración de CO es mayor a 100 ppm. Los niveles de concentración mayores a 200 ppm son considerados altamente tóxicos.\n" +
                        "\n" +
                        "-\tÓxidos de nitrógeno: Se tienen 7 gases en la categoría óxidos de nitrógeno. El óxido nítrico y el dióxido de nitrógeno son las 2 formas más comunes. Se encuentran en entornos industriales y del consumidor. Los óxidos de nitrógeno son el contribuidor principal de contaminación de aire y reducción de calidad de aire. El óxido nítrico suele ser emitido por vehículos, procesos de agricultura o como subproducto de combustibles fósiles. El dióxido de nitrógeno se usa en la producción de combustibles de cohetes y explosivos. Según el CDC, los efectos varían desde la irritación de ojos, piel y tracto respiratorio a circunstancias letales.\n" +
                        "El límite permisible de óxido nítrico es 25 ppm en 8 horas de sesión laboral. 100 ppm es inmediatamente letal. El dióxido de nitrógeno tiene un límite permisible de 5 ppm y 20 ppm de concentración es letal.\n" +
                        "\n" +
                        "-\tOzono: El ozono es especialmente toxico para plantas y humanos, Este gas está hecho de tres átomos de oxígeno (O3) y suele verse como niebla. Puede ocurrir naturalmente en la estratosfera. Las reacciones químicas de escapes de automóviles o vapores de gasolina contribuyen a concentraciones largas de ozono en el nivel del suelo. Problemas de salud como reducción en la función pulmonar, infección respiratoria, sobreexposición a rayos ultravioleta (quemaduras) o el cáncer de piel en humanos la hacen una sustancia tóxica que debe ser monitoreada continuamente.\n" +
                        "De acuerdo a OSHA, los niveles de ozono no deberían exceder 0.10 ppm en 8 horas de exposición diarias.\n" +
                        "\n" +
                        "-\tSolventes: Los solventes son altamente inflamables. Las propiedades de estos gases son altamente tóxicas. Los solventes orgánicos son sustancias a base de carbono capaces de disolver o dispersar una o más sustancias. Los solventes encontrados en el keroseno, la gasolina, los quitapinturas y los desengrasantes son altamente inflamables y en concentraciones altas afectan el sistema nervioso central. Otros efectos son mareos, incapacidad de concentración, confusión, dolores de cabeza, coma y muerte si es que se tiene una exposición prolongada.\n" +
                        "Debido a la gran cantidad de solventes que se usan a diario, OSHA tiene lineamientos individuales para cada solvente que se ha identificado.\n"
            )

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¿Como se puede saber si una persona está inhalando un gas nocivo?\n" +
                        "\n" +
                        "Existen muchos factores que pueden señalar exposición a gases nocivos como ser:\n" +
                        "-\tLa persona se siente enferma por intervalos largos de tiempo. Por ejemplo: dolores de cabeza\n" +
                        "-\tLa persona vive cerca de un área industrial\n" +
                        "-\tLa persona vive cerca de un sitio de construcción\n" +
                        "-\tLa persona presenta síntomas repentinos como irritación de ojos, nariz y garganta\n" +
                        "-\tLa persona presenta síntomas anormales como falta de aire o sangre en esputo\n" +
                        "-\tNausea y mareos\n" +
                        "-\tAlgunos gases huelen\n" +
                        "\n" +
                        "Muchos gases no huelen ni tienen color. Por esto, si existe la presencia de puntos anteriores es aconsejable monitorear los niveles de gases tóxicos.\n"
            )

            Text(text = "Fuente: National Fire Protection Association")
        }

    }


}