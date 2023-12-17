package com.Sub;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.Model.Sensor;
import com.Model.SensorPub;
import com.Model.User;
import com.Service.SensorPubService;
import com.Service.SensorService;
import com.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Configuration
public class MqttConfig {

    private final static String BROKER_URL = "ssl://6b52018721324382aa4dae58e514f765.s1.eu.hivemq.cloud:8883";
    private final static String CLIENT_ID = "spring-mqtt-client";
    private final static String TOPIC = "esp8266/dht11";

    @Autowired
    SensorPubService sensorPubService;
    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{BROKER_URL});
        options.setCleanSession(true);
        options.setUserName("rekalkhai");
        options.setPassword("khai172305".toCharArray());
        return options;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions());
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer mqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(CLIENT_ID, mqttClientFactory(), TOPIC);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler mqttMessageHandler() {
        return message -> {
            String payload = message.getPayload().toString();
            System.out.println("Received message: " + payload);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = null;
			try {
				jsonNode = objectMapper.readTree(payload);
			} catch (JsonMappingException e) {
				
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
            int temperature = jsonNode.get("temperature").asInt();
            int bpm = jsonNode.get("bpm").asInt();
           
            SensorPub pub=new SensorPub();
            pub.setBpm(bpm);
            pub.setTemp(temperature);
            sensorPubService.addSensorPub(pub);
            System.out.println("Temperature: " + temperature);
            System.out.println("BPM: " + bpm);
            
        };
    }
}