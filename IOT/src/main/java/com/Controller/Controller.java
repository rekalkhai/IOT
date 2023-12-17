package com.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.DTO.SensorDTO;
import com.Model.Predict;
import com.Model.Sensor;
import com.Model.SensorPub;
import com.Model.User;
import com.Service.PredictService;
import com.Service.SensorPubService;
import com.Service.SensorService;
import com.Service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin("*")
@RequestMapping("/mqtt")
public class Controller {

	@Autowired
    SensorService sensorService;
	@Autowired
    SensorPubService sensorPubService;
	@Autowired
	UserService userService;
	@Autowired
	PredictService predictService;
	@GetMapping("/home")
    public ResponseEntity<SensorDTO> getSensors(HttpSession session){
		User user = (User) ((HttpSession) session).getAttribute("NAME");
		List<SensorPub> sensorpubs = sensorPubService.getAllSensors();
		int lastIndexPub = sensorpubs.size() - 1;
		SensorPub sensorPub=sensorpubs.get(lastIndexPub);
		Sensor sensor1=new Sensor();
		sensor1.setBpm(sensorPub.getBpm());
		sensor1.setTemp(sensorPub.getTemp());
		sensor1.setUser(userService.getUserByID(user.getUserId()));
		sensorService.addSensor(sensor1);
		
		List<Sensor> sensors = sensorService.getAllSensors(user.getUserId());
		int lastIndex = sensors.size() - 1;
		Sensor sensor=sensors.get(lastIndex);
		int startIndex = Math.max(sensors.size() - 5, 0);
		List<Sensor> sensor10=sensors.subList(startIndex, sensors.size());
		
		List<Predict> predicts = predictService.getAllPredicts(user.getUserId());
		int dix = Math.max(predicts.size() - 5, 0);
		List<Predict> predicts5=predicts.subList(dix, predicts.size());
		SensorDTO dto=new SensorDTO();
		dto.setLatestSensor(sensor);
		dto.setSensors(sensor10);
		dto.setPredicts(predicts5);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
	@PostMapping("/saveData")
    public String saveData(@RequestBody String content,HttpSession session) {
		User user = (User) ((HttpSession) session).getAttribute("NAME");
        Predict predict = new Predict();
        predict.setResult(content);
        predict.setUser(userService.getUserByID(user.getUserId()));
        predictService.addPredict(predict);
        return "Data saved successfully!";
    }
	
}
