  #include <SoftwareSerial.h>
#include <Wire.h>
#include "DHT.h"

#define DEBUG true
#define DHTPIN 6 
#define DHTTYPE DHT22 

DHT dht(DHTPIN, DHTTYPE);

SoftwareSerial wifi = SoftwareSerial(7,8); //RX, TX
SoftwareSerial dust = SoftwareSerial(4,5); //RX, TX
// Dust data setup
byte send_data[] = {0x11, 0x01, 0x01, 0xED};
byte receive_data[16];
float Ro = 10000.0; 

String sendData(String command, const int timeout, boolean debug){
	String response = "";
	wifi.print(command);
	long int time = millis();
	
	while((time + timeout) > millis()){
		while(wifi.available()){
			char c = wifi.read();
			response += c;
		}
	}
	if(debug)
    Serial.print("test");
		Serial.print(response);
	return response;
}

double get_CO (float ratio){
  double ppm = 0.0;
  ppm = 37143 * pow (ratio, -3.178);
return ppm;
}

void setup(){
	dht.begin();
	Serial.begin(9600);
	dust.begin(9600);
  wifi.begin(9600);
	sendData("AT+RST\r\n",2000,DEBUG);
	sendData("AT+CWMODE=2\r\n",1000,DEBUG);
	sendData("AT+CIFSR\r\n",1000,DEBUG);
	sendData("AT+CIPMUX=1\r\n",1000,DEBUG);
	sendData("AT+CIPSERVER=1,80\r\n",1000,DEBUG);
	
}

void loop(){
  	//Get Dust Sensor Value
   
  	for(int i = 0; i < 4; i++) 
      dust.write(send_data[i]);
    for(int i = 0; i < 16; i++){
      receive_data[i] = 0x00;
      receive_data[i] = dust.read();
    }
   double ugm = ((receive_data[3] * 255.0 * 255.0 * 255.0) + (receive_data[4] * 255.0 * 255.0) + (receive_data[5] * 255.0) + receive_data[6]);
   double dust_value = ugm * 3528.0 / 100000.0; 
   int dh, dl;
   dh = (int)dust_value;
   dl = dust_value * 100 - dh * 100;
   dust_value = (dh + (dl * 0.01));
   //Get Humi and Temp Sensor Value
	double humi_value = (double)dht.readHumidity();
	// Read temperature as Celsius (the default)
	double temp_value = (double)dht.readTemperature();
	// Compute heat index in Celsius (isFahreheit = false)
	
	if (isnan(humi_value) || isnan(temp_value)) {
		Serial.println("Failed to read from DHT sensor!");
		return;
	}
	
	//Get Gas Sensor Value
	double Vrl = analogRead(A0)*(5.00/1024.0);
	float Rs = 20000 * ( 5.00 - Vrl) / Vrl ;   // Ohm
	double ratio =  Rs/Ro;
	double gas_value = get_CO(ratio);
	switch(Serial.read()){
	  case 't' : Serial.println(temp_value); break;
	  case 'h' : Serial.println(humi_value); break;
	  case 'g' : Serial.println(gas_value); break;
	  case 'd' : Serial.println(dust_value); break;
    case 'a' :
    Serial.print("Temp : ");
  Serial.println(temp_value);
  Serial.print("Humi : ");
  Serial.println(humi_value);
  Serial.print("Gas : ");
  Serial.println(gas_value);
  Serial.print("Dust : ");
  Serial.println(dust_value);
  break;
  default : break;
	}
	if(wifi.available()){
    while(wifi.available()){
      char c = wifi.read();
      Serial.write(c);
    }
  	if(wifi.find("+IPD,")){
  		int connectionId = wifi.read()-48;
  		String webpage = "<a type=\"Temp\">";
      webpage.concat(temp_value);
      webpage.concat("</a><a type=\"Humi\">");
      webpage.concat(humi_value);
      webpage.concat("</a><a type=\"Gas\">");
      webpage.concat(gas_value);
      webpage.concat("</a><a type=\"Dust\">");
      webpage.concat(dust_value);
      webpage.concat("</a>"); 
  		String cipSend = "AT+CIPSEND=";
  		cipSend += connectionId;
  		cipSend += ",";
  		cipSend += webpage.length();
  		cipSend += "\r\n";
  		
  		sendData(cipSend,1000,DEBUG);
  		sendData(webpage,1000,DEBUG);
  		
  		String closeCommand = "AT+CIPCLOSE=";
  		closeCommand += connectionId;
  		closeCommand += "\r\n";
  		
  		sendData(closeCommand,3000,DEBUG);
		}
	}
 delay(200);
}
