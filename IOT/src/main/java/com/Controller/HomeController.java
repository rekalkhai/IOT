package com.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.Model.SensorPub;
import com.Service.SensorPubService;

@Controller
public class HomeController {
	
	@Autowired
    SensorPubService sensorPubService;
	@GetMapping("/home")
    public String getHome(Model model) {
		List<SensorPub> sensorpubs = sensorPubService.getAllSensors();
		int lastIndexPub = sensorpubs.size() - 1;
		SensorPub sensorPub=sensorpubs.get(lastIndexPub);
		model.addAttribute("sensorPub", sensorPub);
    	return "Home";
    }
	
}
