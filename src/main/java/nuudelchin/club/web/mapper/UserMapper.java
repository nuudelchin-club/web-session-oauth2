package nuudelchin.club.web.mapper;

import org.apache.ibatis.annotations.Mapper;

import nuudelchin.club.web.entity.UserEntity;

@Mapper
public interface UserMapper {
	
	UserEntity selectByUsername(String username);
	
	int insert(UserEntity userEntity);
	
	int update(UserEntity userEntity);
}
