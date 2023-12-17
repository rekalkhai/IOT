package com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	@Query(value = "SELECT * FROM user WHERE username = ?1 and password = ?2",nativeQuery = true)
    User findByUserPassword(String username,String password);

    @Query(value = "SELECT * FROM user WHERE username = ?1",nativeQuery = true)
    User findByUsername(String username);
}
