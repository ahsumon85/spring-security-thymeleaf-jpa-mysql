package com.springbooth.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.springbooth.model.dto.UserDTO;
import com.springbooth.model.entity.Role;
import com.springbooth.model.entity.User;
import com.springbooth.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("userDTO", new UserDTO());
		modelAndView.setViewName("registration");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid UserDTO userDTO) {
		ModelAndView modelAndView = new ModelAndView();
		User user = userService.findUserByEmail(userDTO.getEmail());
		if (user == null) {
			userService.saveUser(userDTO);
			modelAndView.addObject("user", new UserDTO());
			modelAndView.setViewName("login");
		} else {
			modelAndView.addObject("successMessage", "There is already registered user...!");
			modelAndView.setViewName("registration");
		}
		return modelAndView;
	}

	@RequestMapping(value = "user/info", method = RequestMethod.GET)
	public ModelAndView userProfile() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(auth.getName());
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("user", user);
		modelAndView.addObject("userName", user.getFirstName() + " " + user.getLastName());
		modelAndView.setViewName("user-profile");
		return modelAndView;
	}

	@RequestMapping(value = "/change/password", method = RequestMethod.GET)
	public ModelAndView changePassword() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("userDTO", new UserDTO());
		modelAndView.setViewName("password-update");
		return modelAndView;
	}

	@RequestMapping(value = "/new/password", method = RequestMethod.POST)
	public ModelAndView newPassword(UserDTO userDTO) {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (userDTO.getNewPass().equals(userDTO.getConfirmPass())) {
			User user = userService.findUserByEmail(auth.getName());
			Boolean status = userService.isPasswordValid(userDTO.getPassword(), user.getPassword());
			if (status == true) {

				userService.changePasswor(user, userDTO);
				modelAndView.setViewName("login");
			} else {
				modelAndView.addObject("wrongPass", "Current possword was wrong..!");
				modelAndView.setViewName("password-update");
			}

		} else {
			modelAndView.addObject("passMatched", "Password doesn't matched..!");
			modelAndView.setViewName("password-update");
		}
		return modelAndView;
	}

	@RequestMapping(value = "/admin/home", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		String name = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		System.out.println(name);
		modelAndView.addObject("userName",
				"Welcome " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		modelAndView.setViewName("admin/home");
		return modelAndView;
	}

}
