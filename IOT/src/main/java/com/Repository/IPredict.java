package com.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Model.Predict;
import com.Model.Sensor;

@Repository
public interface IPredict extends JpaRepository<Predict,Integer> {
	@Query(value ="SELECT * FROM predict p WHERE p.fkpr_id = ?1",nativeQuery = true)
    List<Predict> findByFkprId( int fkprId);
}