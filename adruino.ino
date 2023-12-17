#include<ESP8266WiFi.h>
#include "DHTesp.h"
#include <ArduinoJson.h>
#include <PubSubClient.h>
#include <WiFiClientSecure.h>

const char* ssid = "Nhan Duyen";      
const char* password = "20212021";   

const char* mqtt_server = "6b52018721324382aa4dae58e514f765.s1.eu.hivemq.cloud";
const int mqtt_port = 8883;
const char* mqtt_username = "rekalkhai"; 
const char* mqtt_password = "khai172305";
//--------------------------------------------------

#define DHTpin 5
DHTesp dht;

int PulseSensorPurplePin = 0;
int LED = 4;

WiFiClientSecure espClient;
PubSubClient client(espClient);

unsigned long lastMsg = 0;
#define MSG_BUFFER_SIZE (50)
char msg[MSG_BUFFER_SIZE];

void setup_wifi() {
  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  randomSeed(micros());
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}
//------------Connect to MQTT Broker-----------------------------
void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    String clientID =  "ESPClient-";
    clientID += String(random(0xffff),HEX);
    if (client.connect(clientID.c_str(), mqtt_username, mqtt_password)) {
      Serial.println("connected");
      client.subscribe("esp8266/client");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}
//-----Call back Method for Receiving MQTT massage---------
void callback(char* topic, byte* payload, unsigned int length) {
  String incommingMessage = "";
  for(int i=0; i<length;i++) incommingMessage += (char)payload[i];
  Serial.println("Massage arived ["+String(topic)+"]"+incommingMessage);
}
//-----Method for Publishing MQTT Messages---------
void publishMessage(const char* topic, String payload, boolean retained){
  if(client.publish(topic,payload.c_str(),true))
    Serial.println("Message published ["+String(topic)+"]: "+payload);
}


void setup() {
  Serial.begin(9600);
  while(!Serial) delay(1);

  dht.setup(DHTpin,DHTesp::DHT11);
  pinMode(LED, OUTPUT);
  setup_wifi();
  espClient.setInsecure();
  client.setServer(mqtt_server, mqtt_port);
  client.setCallback(callback);
}
void loop() {
  if (!client.connected()) {
    reconnect();
  }
  
  unsigned long currentMillis = millis();
  unsigned long previous = 0;
  const unsigned long sensor = 5000;
  unsigned long lastBeatTime = 0;
  unsigned long lastSampleTime = 0;
  int BPM = 0;
  bool Pulse = false;
  int Signal = analogRead(PulseSensorPurplePin);
  int Threshold = 550;
  if (Signal > Threshold) {
    if (Pulse == false) {
      Pulse = true;
      BPM = 29 / ((millis() - lastBeatTime) / 1000.0);
      lastBeatTime = millis();
    }
  }
  if (Signal < Threshold && Pulse == true) {
    Pulse = false;
  }
  if (BPM > 0) {
    digitalWrite(LED, HIGH);
    delay(100);
    digitalWrite(LED, LOW);
  }
  if (currentMillis - previous >= sensor) {
    previous = currentMillis;  
    float t = dht.getTemperature()+33.5;
    DynamicJsonDocument doc(1024);
    doc["temperature"]=t;
    doc["bpm"]=BPM;
    char mqtt_message[128];
    serializeJson(doc,mqtt_message);
    publishMessage("esp8266/dht11", mqtt_message, true);
  }
  delay(3000);
}