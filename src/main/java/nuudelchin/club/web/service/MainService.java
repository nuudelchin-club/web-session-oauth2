package nuudelchin.club.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nuudelchin.club.web.entity.UserEntity;
import nuudelchin.club.web.mapper.UserMapper;

@Service
public class MainService {

    @Autowired
    private UserMapper userMapper;

    public UserEntity selectByUsername(String username) {

    	return userMapper.selectByUsername(username);
    }
}
