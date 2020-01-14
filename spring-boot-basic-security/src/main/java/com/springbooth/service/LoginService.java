package com.springbooth.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springbooth.model.dto.UserDTO;
import com.springbooth.model.entity.Role;
import com.springbooth.model.entity.User;
import com.springbooth.repo.UserRepository;

@Service
@Transactional
public class LoginService {

	@Autowired
	private UserRepository userRepository;
	
	public void signUp(UserDTO userDTO) {
		userRepository.save(copyUserDtoToEntity(userDTO));
	}

	public User copyUserDtoToEntity(UserDTO userDTO) {
		User user=new User();		
		BeanUtils.copyProperties(userDTO, user);	
		user.setPassword(generateBcryptHash12(userDTO.getPassword())); 
		user.setActive(1); 
		setRoles(user);		
		return user;
	}

	private void setRoles(User user) {
		Set<Role> roles = new HashSet<>();
		Role role = new Role();
		role.setRole("ROLE_GUEST");
		role.setUser(user);			
		roles.add(role);		
		user.setRoles(roles);
	}


public static String generateBcryptHash12(String plainPassword) {
String generatedHash = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
return generatedHash;
}

}
