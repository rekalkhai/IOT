package com.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Model.Sensor;

@Repository
public interface ISensor extends JpaRepository<Sensor,Integer> {

	@Query(value ="SELECT * FROM sensor s WHERE s.fkss_id = ?1",nativeQuery = true)
    List<Sensor> findByFkssId( int fkssId);
	
}
