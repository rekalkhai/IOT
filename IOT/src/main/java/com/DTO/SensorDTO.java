package com.DTO;

import java.util.List;

import com.Model.Predict;
import com.Model.Sensor;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorDTO {

	private List<Predict> predicts;
	public List<Predict> getPredicts() {
		return predicts;
	}

	public void setPredicts(List<Predict> predicts) {
		this.predicts = predicts;
	}
	private List<Sensor> sensors;
    private Sensor latestSensor;
	public List<Sensor> getSensors() {
		return sensors;
	}
	
	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}
	public Sensor getLatestSensor() {
		return latestSensor;
	}
	public void setLatestSensor(Sensor latestSensor) {
		this.latestSensor = latestSensor;
	}
    
}
