//Include the library
#include <MQUnifiedsensor.h>

//mq3
/************************Hardware Related Macros************************************/
#define         Board                   ("Arduino MEGA")
#define         Pin3                     (A2)  //Analog input 3 of your arduino
/***********************Software Related Macros************************************/
#define         Type                    ("MQ-3") //MQ3
#define         Voltage_Resolution      (5)
#define         ADC_Bit_Resolution      (10) // For arduino UNO/MEGA/NANO
#define         RatioMQ3CleanAir        (60) //RS / R0 = 60 ppm 
/*****************************Globals***********************************************/
//Declare Sensor
MQUnifiedsensor MQ3(Board, Voltage_Resolution, ADC_Bit_Resolution, Pin3, Type);

//mq5
#define pin5 A7 //Analog input 0 of your arduino
#define type "MQ-5" //MQ5
#define RatioMQ5CleanAir 6.5  //RS / R0 = 6.5 ppm 
//#define calibration_button 13 //Pin to calibrate your sensor

//Declare Sensor
MQUnifiedsensor MQ5(Board, Voltage_Resolution, ADC_Bit_Resolution, pin5, type);

//mq9
#define         Pin9                     (A4)  //Analog input 4 of your arduino
/***********************Software Related Macros************************************/
#define         Type                    ("MQ-9") //MQ9
#define         RatioMQ9CleanAir        (9.6) //RS / R0 = 60 ppm 
/*****************************Globals***********************************************/
//Declare Sensor
MQUnifiedsensor MQ9(Board, Voltage_Resolution, ADC_Bit_Resolution, Pin9, Type);

void setup()
{  
  Serial1.begin(9600);
  Serial.begin(9600);
  delay(1000);

  //mq3
  //Set math model to calculate the PPM concentration and the value of constants
  MQ3.setRegressionMethod(1); //_PPM =  a*ratio^b
  MQ3.setA(4.8387); MQ3.setB(-2.68); // Configure the equation to to calculate Benzene concentration
  /*
    Exponential regression:
  Gas    | a      | b
  LPG    | 44771  | -3.245
  CH4    | 2*10^31| 19.01
  CO     | 521853 | -3.821
  Alcohol| 0.3934 | -1.504
  Benzene| 4.8387 | -2.68
  Hexane | 7585.3 | -2.849
  */

  /*****************************  MQ Init ********************************************/ 
  //Remarks: Configure the pin of arduino as input.
  /************************************************************************************/ 
  MQ3.init(); 
  
  /* 
    //If the RL value is different from 10K please assign your RL value with the following method:
    MQ3.setRL(10);
  */
  /*****************************  MQ CAlibration ********************************************/ 
  // Explanation: 
   // In this routine the sensor will measure the resistance of the sensor supposedly before being pre-heated
  // and on clean air (Calibration conditions), setting up R0 value.
  // We recomend executing this routine only on setup in laboratory conditions.
  // This routine does not need to be executed on each restart, you can load your R0 value from eeprom.
  // Acknowledgements: https://jayconsystems.com/blog/understanding-a-gas-sensor
  Serial.print("Calibrating please wait.");
  float calcR0 = 0;
  for(int i = 1; i<=10; i ++)
  {
    MQ3.update(); // Update data, the arduino will read the voltage from the analog pin
    calcR0 += MQ3.calibrate(RatioMQ3CleanAir);
    Serial.print(".");
  }
  MQ3.setR0(calcR0/10);
  Serial.println("  done!.");
  
  if(isinf(calcR0)) {Serial.println("Warning: Conection issue, R0 is infinite (Open circuit detected) please check your wiring and supply"); while(1);}
  if(calcR0 == 0){Serial.println("Warning: Conection issue found, R0 is zero (Analog pin shorts to ground) please check your wiring and supply"); while(1);}
  /*****************************  MQ CAlibration ********************************************/ 
  MQ3.serialDebug(true);

  //mq5
  //Set math model to calculate the PPM concentration and the value of constants
  MQ5.setRegressionMethod(1); //_PPM =  a*ratio^b
  MQ5.setA(491204); MQ5.setB(-5.826); // Configure the equation to to calculate H2 concentration
  /*
    Exponential regression:
  Gas    | a      | b
  H2     | 1163.8 | -3.874
  LPG    | 80.897 | -2.431
  CH4    | 177.65 | -2.56
  CO     | 491204 | -5.826
  Alcohol| 97124  | -4.918
  */
  
  /*****************************  MQ Init ********************************************/ 
  //Remarks: Configure the pin of arduino as input.
  /************************************************************************************/ 
  MQ5.init();   
  /* 
    //If the RL value is different from 10K please assign your RL value with the following method:
    MQ5.setRL(10);
  */
  /*****************************  MQ CAlibration ********************************************/ 
  // Explanation: 
   // In this routine the sensor will measure the resistance of the sensor supposedly before being pre-heated
  // and on clean air (Calibration conditions), setting up R0 value.
  // We recomend executing this routine only on setup in laboratory conditions.
  // This routine does not need to be executed on each restart, you can load your R0 value from eeprom.
  // Acknowledgements: https://jayconsystems.com/blog/understanding-a-gas-sensor
  Serial.print("Calibrating please wait.");
  calcR0 = 0;
  for(int i = 1; i<=10; i ++)
  {
    MQ5.update(); // Update data, the arduino will read the voltage from the analog pin
    calcR0 += MQ5.calibrate(RatioMQ5CleanAir);
    Serial.print(".");
  }
  MQ5.setR0(calcR0/10);
  Serial.println("  done!.");
  
  if(isinf(calcR0)) {Serial.println("Warning: Conection issue, R0 is infinite (Open circuit detected) please check your wiring and supply"); while(1);}
  if(calcR0 == 0){Serial.println("Warning: Conection issue found, R0 is zero (Analog pin shorts to ground) please check your wiring and supply"); while(1);}
  /*****************************  MQ CAlibration ********************************************/ 
  MQ5.serialDebug(true);
  
  //mq9
  //Set math model to calculate the PPM concentration and the value of constants
  MQ9.setRegressionMethod(1); //_PPM =  a*ratio^b
 
  /*****************************  MQ Init ********************************************/ 
  //Remarks: Configure the pin of arduino as input.
  /************************************************************************************/ 
  MQ9.init(); 
  /* 
    //If the RL value is different from 10K please assign your RL value with the following method:
    MQ9.setRL(10);
  */
  /*****************************  MQ CAlibration ********************************************/ 
  // Explanation: 
   // In this routine the sensor will measure the resistance of the sensor supposedly before being pre-heated
  // and on clean air (Calibration conditions), setting up R0 value.
  // We recomend executing this routine only on setup in laboratory conditions.
  // This routine does not need to be executed on each restart, you can load your R0 value from eeprom.
  // Acknowledgements: https://jayconsystems.com/blog/understanding-a-gas-sensor
  Serial.print("Calibrating please wait.");
  calcR0 = 0;
  for(int i = 1; i<=10; i ++)
  {
    MQ9.update(); // Update data, the arduino will read the voltage from the analog pin
    calcR0 += MQ9.calibrate(RatioMQ9CleanAir);
    Serial.print(".");
  }
  MQ9.setR0(calcR0/10);
  Serial.println("  done!.");
  
  if(isinf(calcR0)) {Serial.println("Warning: Conection issue, R0 is infinite (Open circuit detected) please check your wiring and supply"); while(1);}
  if(calcR0 == 0){Serial.println("Warning: Conection issue found, R0 is zero (Analog pin shorts to ground) please check your wiring and supply"); while(1);}
  /*****************************  MQ CAlibration ********************************************/ 

}

void loop()
{ 
  //mq3 (alcoholimetro)
  int adc_MQ = analogRead(A2); //Lemos la salida analógica  del MQ
  float voltaje = adc_MQ * (5.0 / 1023.0); //Convertimos la lectura en un valor de voltaje
  float Rs=1000*((5-voltaje)/voltaje);  //Calculamos Rs con un RL de 1k
  double alcohol=0.4091*pow(Rs/5463, -1.497); // calculamos la concentración  de alcohol con la ecuación obtenida.
  
  Serial.print("alcohol:");
  Serial.print(alcohol);
  Serial.println("mg/L");



  //mq3
  MQ3.update(); // Update data, the arduino will read the voltage from the analog pin
  float datoMQ3 = MQ3.readSensor(); // Sensor will read PPM concentration using the model, a and b values set previously or from the setup
  //MQ3.serialDebug(); // Will print the table on the serial port


  


  //mq5
  int inputanalog1 = analogRead(A7);
  //Serial.print("mq5:");
  //Serial.println(inputanalog1);

  MQ5.update(); // Update data, the arduino will read the voltage from the analog pin
  float datoMQ5 = MQ5.readSensor(); // Sensor will read PPM concentration using the model, a and b values set previously or from the setup
  //MQ5.serialDebug(); // Will print the table on the serial port




  //mq9
  //Serial.print("mq9:");
  int inputanalog2 = analogRead(A4);
  //Serial.println(inputanalog2);

  MQ9.update(); // Update data, the arduino will read the voltage from the analog pin
  /*
  Exponential regression:
  GAS     | a      | b
  LPG     | 1000.5 | -2.186
  CH4     | 4269.6 | -2.648
  CO      | 599.65 | -2.244
  */

  MQ9.setA(1000.5); MQ9.setB(-2.186); // Configure the equation to to calculate LPG concentration
  float LPG = MQ9.readSensor(); // Sensor will read PPM concentration using the model, a and b values set previously or from the setup

  MQ9.setA(4269.6); MQ9.setB(-2.648); // Configure the equation to to calculate LPG concentration
  float CH4 = MQ9.readSensor(); // Sensor will read PPM concentration using the model, a and b values set previously or from the setup

  MQ9.setA(599.65); MQ9.setB(-2.244); // Configure the equation to to calculate LPG concentration
  float CO = MQ9.readSensor(); // Sensor will read PPM concentration using the model, a and b values set previously or from the setup

  //Serial.println("** Values from MQ-9 ****");
  //Serial.println("| MQ9 LPG PPM | MQ9 CH4 PPM | MQ9 CO PPM | MQ3 Benzene PPM | MQ5 CO PPM "); 
  //Serial.print("|    "); Serial.print(LPG);
  //Serial.print("    |    "); Serial.print(CO); 
  //Serial.print("    |");Serial.print(datoMQ3);
   
  Serial.println(" MQ9 CH4 PPM | MQ5 CO PPM ");   
  Serial.print(" |    "); Serial.print(CH4);
  Serial.print("    |    "); Serial.print(datoMQ5); 
  Serial.println("    |    ");

  //write bluetooth  
  //String mensaje = String(alcohol)+","+String(inputanalog1)+","+String(inputanalog2)+"z";
  String mensaje = String(alcohol)+","+String(CH4)+","+String(datoMQ5)+"z";
  writeString(mensaje);
  delay(10000);
}





void writeString(String stringData) { // Used to serially push out a String with Serial.write()

  for (int i = 0; i < stringData.length(); i++)
  {
    Serial1.write(stringData[i]);   // Push each char 1 by 1 on each loop pass
  }

}// end writeString


