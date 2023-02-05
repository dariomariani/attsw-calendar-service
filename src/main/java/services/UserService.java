package services;

import java.util.List;

import models.User;

public class UserService {
	private List<User> userRepository;
	
	
	public UserService(List<User> userRepository) {
		this.userRepository = userRepository;
	}
	
	public List<User> findAll(){
		return this.userRepository;
	}

	public void createUser(User newUser) {
		if (userRepository.contains(newUser)) throw new IllegalArgumentException(String.format("A User with the Username {0} already exists.", newUser.getUserName()));
		this.userRepository.add(newUser);
	}

}
