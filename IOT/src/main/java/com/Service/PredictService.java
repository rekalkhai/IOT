package com.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Model.Predict;
import com.Model.Sensor;
import com.Repository.IPredict;

@Service
public class PredictService {

	@Autowired
	private IPredict iPredict;
	public void addPredict(Predict predict) {
    	iPredict.save(predict);
    }
	public List<Predict> getAllPredicts(int fkprId ) {
		return iPredict.findByFkprId(fkprId);
	}
}
