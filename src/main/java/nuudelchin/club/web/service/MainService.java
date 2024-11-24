package nuudelchin.club.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nuudelchin.club.web.entity.UserEntity;
import nuudelchin.club.web.repository.UserRepository;

@Service
public class MainService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity findByUsername(String username) {

    	return userRepository.findByUsername(username);
    }
}
