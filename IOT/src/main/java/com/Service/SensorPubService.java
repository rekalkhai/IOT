package com.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Model.Sensor;
import com.Model.SensorPub;
import com.Repository.ISensor;
import com.Repository.ISensorPub;

@Service
public class SensorPubService {

	@Autowired 
	private ISensorPub iSensorPub;
	public void addSensorPub(SensorPub sensorPub) {
    	iSensorPub.save(sensorPub);
    }
	public List<SensorPub> getAllSensors() {
		return iSensorPub.findAll();
	}
}
