package com.springbooth.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springbooth.model.dto.UserDTO;
import com.springbooth.model.entity.Role;
import com.springbooth.model.entity.User;
import com.springbooth.repo.RoleRepository;
import com.springbooth.repo.UserRepository;

@Service("userService")
public class UserService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public List<UserDTO> findAllUsers() {
		List<User> users = userRepository.findAll();
		if (users != null) {
			List<UserDTO> userDTOs = new ArrayList<>();
			for (User user : users) {
				UserDTO userDTO = new UserDTO();
				BeanUtils.copyProperties(user, userDTO);
				userDTO.setFirstName(user.getFirstName() + " " + user.getLastName());
				userDTO.setBirthDate(provideCurrentAge(user.getBirthDate()));
				userDTOs.add(userDTO);
			}
			return userDTOs;
		} else {
			return new ArrayList<>();
		}
	}

	public String provideCurrentAge(String date) {
		Period period = providePerodDate(date);
		String age = null;
		String year = null;
		String day = null;
		String month = null;
		if (period.getYears() != 0) {
			year = string(period.getYears()) + " years ";
		}
		if (period.getMonths() != 0) {
			month = string(period.getMonths()) + " months ";
		}
		if (period.getDays() != 0) {
			day = string(period.getDays()) + " days ";
		}
		age = year + month + day;
		age = age.replaceAll("null", "");
		return age;
	}

	public static Period providePerodDate(String date) {
		LocalDate today = LocalDate.now();
		LocalDate birthday = LocalDate.parse(date);
		Period period = Period.between(birthday, today);
		return period;
	}

	public static String string(Integer integer) {
		return Integer.toString(integer);
	}

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void changePasswor(User user, UserDTO userDTO) {
		user.setPassword(bCryptPasswordEncoder.encode(userDTO.getNewPass()));
		userRepository.save(user);
	}

	public boolean isPasswordValid(String databasedPass, String givenPassword) {
		if (BCrypt.checkpw(databasedPass, givenPassword)) {
			return true;
		} else {
			return false;
		}
	}

	public void signUp(UserDTO userDTO) {

	}

	public User saveUser(UserDTO userDTO) {
		User user = new User();
		user.setEmail(userDTO.getEmail());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setAddress(userDTO.getAddress());
		user.setPhone(userDTO.getPhone());
		user.setBirthDate(userDTO.getBirthDate());
		user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
		user.setActive(1);

		Role role = new Role();
		role.setRole(userDTO.getRole());
		role.setUser(user);
		user.setRoles(new HashSet<>(Arrays.asList(role)));
		return userRepository.save(user);
	}

}