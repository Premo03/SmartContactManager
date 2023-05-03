package com.start.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.start.dao.UserRepository;
import com.start.entity.User;
import com.start.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

//import ch.qos.logback.core.model.Model;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title","Home - Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title","About - Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("title","Register - Smart Contact Manager");
		model.addAttribute("user",new User());
		return "signup";
	}


//This for registering user
	@RequestMapping(value ="/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result1,
			@RequestParam(value = "agreement", defaultValue = "false")boolean agreement,
			Model model,HttpSession session)
	{
		try 
		{
			if(!agreement)
			{
				System.out.println("You are not agreed our terms and conditions");
				throw new Exception("You are not agreed our terms and conditions");
			}
			
			if(result1.hasErrors())
			{
				System.out.println("ERROR "+result1.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			
				user.setRole("ROLE_USER");	
				user.setEnabled(true);
				user.setImageUrl("default.png");
	
				System.out.println("Agreement "+agreement);
				System.out.println("USER "+user);
	
				User result = this.userRepository.save(user);
	
				model.addAttribute("user",new User());
				
				session.setAttribute("message", new Message("Successfully Registered!!","alert-success"));
				return "signup";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong!!"+ e.getMessage(),"alert-danger"));
			return "signup";
		}
		
	}
}	
