package com.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.Model.User;
import com.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
    UserService userService;
	@GetMapping("/Login")
    public String getLogin(Model model) {
    	model.addAttribute("user",new User());
    	return "login";
    }
    @GetMapping("/Register")
    public String getRegister(Model model) {
    	model.addAttribute("user", new User());
    	return "register";
    }
    @PostMapping("/submit_login")
    public String postLogin(Model model,@ModelAttribute("user") User user,
    		HttpSession session) {
    	User tmp=userService.getUser(user.getUsername(),user.getPassword() );
    	if(tmp!=null) {
    		session.setAttribute("NAME", tmp);
    		return "Home";
    	}
    	else {
    		model.addAttribute("mess1", "Wrong username or password");
    		return "Login";
    	}
    }
    @PostMapping("/submit_register")
    public String getRegister(Model model, @ModelAttribute("user") User user) {
    	if(userService.getUserByUsername(user.getUsername())==null) {
    		userService.addUser(user);
    		return "Login";
    	}
    	else {
    		model.addAttribute("mess2", "Username account already exists");
    		return "Register";
    	}
    }
    @GetMapping("/Logout")
    public String getLogout(HttpSession session,Model model) {
    	session.removeAttribute("NAME");
    	return "redirect:/home";
    }
}
