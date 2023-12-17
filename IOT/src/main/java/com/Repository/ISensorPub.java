package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Model.SensorPub;

@Repository
public interface ISensorPub extends JpaRepository<SensorPub,Integer> {
	
}
