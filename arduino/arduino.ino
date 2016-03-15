#include <SoftwareSerial.h>
#include <Wire.h>
#include "DHT.h"
#define DHTPIN 6 
#define DHTTYPE DHT22  
// Connect a 10K resistor from pin 2 (data) to pin 1 (power) of the sensor

DHT dht(DHTPIN, DHTTYPE);

SoftwareSerial Wifi = SoftwareSerial(6,8); //RX, TX
SoftwareSerial dust = SoftwareSerial(4,5); // RX, TX
// Dust data setup
byte send_data[] = {0x11, 0x01, 0x01, 0xED};
byte receive_data[16];
float Ro = 10000.0; 

float get_CO (float ratio){
  float ppm = 0.0;
  ppm = 37143 * pow (ratio, -3.178);
return ppm;
}

void setup(){
	dht.begin();
	Serial.begin(9600);
	dust.begin(9600);
  Wifi.begin(9600);
}

void loop(){
  double dust_value; 
	//Get Dust Sensor Value
	for(byte i = 0; i < 4; i++) 
		dust.write(send_data[i]); // send data
	for(byte i = 0; i < 16; i++) 
		receive_data[i] = dust.read();
	
	double ugm, pcsl;
	ugm = receive_data[3] * 255.0 * 255.0 * 255.0 + receive_data[4] * 255.0 * 255.0 + receive_data[5] * 255.0 + receive_data[6];
	pcsl = ugm * 3528.0 / 100000.0;
	int dh,dl,c;
	dh = (int)pcsl;
	dl = pcsl * 100 - dh * 100;
	dust_value = dh + (dl % 100);
	//Get Humi and Temp Sensor Value
	
	float humi_value = dht.readHumidity();
	// Read temperature as Celsius (the default)
	float temp_value = dht.readTemperature();
	// Compute heat index in Celsius (isFahreheit = false)
	
	if (isnan(humi_value) || isnan(temp_value)) {
		Serial.println("Failed to read from DHT sensor!");
		return;
	}
	
	//Get Gas Sensor Value
	double Vrl = analogRead(A0)*(5.00/1024.0);
  float Rs = 20000 * ( 5.00 - Vrl) / Vrl ;   // Ohm
  double ratio =  Rs/Ro;
  float gas_value = get_CO(ratio);

  Serial.print("Temp : ");
  Serial.println(temp_value);
  Serial.print("Humi : ");
  Serial.println(humi_value);
  Serial.print("Gas : ");
  Serial.println(gas_value);
  Serial.print("Dust : ");
  Serial.println(dust_value);
  Serial.println();
  delay(200);
}

