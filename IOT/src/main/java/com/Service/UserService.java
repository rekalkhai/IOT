package com.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Model.User;
import com.Repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;
	public User getUser(String username,String password) {
        try {
            return repo.findByUserPassword(username,password);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public User getUserByID(int id){
        try {
            return repo.findById(id).get();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public User getUserByUsername(String username) {
        try {
            return repo.findByUsername(username);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public void addUser(User user) { repo.save(user); }
    public void updateUser(User user) {
        try {
            repo.save(user);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
