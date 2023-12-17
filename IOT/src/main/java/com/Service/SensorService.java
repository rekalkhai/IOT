package com.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Model.Sensor;
import com.Repository.ISensor;

@Service
public class SensorService {
	@Autowired
	private ISensor iSensor;
	public List<Sensor> getAllSensors(int fkssId ) {
		return iSensor.findByFkssId(fkssId);
	}
	public void addSensor(Sensor sensor) {
    	iSensor.save(sensor);
    }
	

}
