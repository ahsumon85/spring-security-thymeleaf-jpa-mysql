package com.springbooth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springbooth.model.dto.UserDTO;
import com.springbooth.service.UserService;

@Controller
public class UsersController {

	@Autowired
	private UserService userService;

	// view all users list.............
	@RequestMapping(path = "/usersUrl")
	public String home(Model model) {
		try {
			model.addAttribute("userDTO", new UserDTO());
			model.addAttribute("users", userService.findAllUsers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "users-list";
	}

}
