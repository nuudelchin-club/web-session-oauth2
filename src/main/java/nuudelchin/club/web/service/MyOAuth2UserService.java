package nuudelchin.club.web.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import nuudelchin.club.web.common.DownloadUtility;
import nuudelchin.club.web.dto.MyOAuth2User;
import nuudelchin.club.web.dto.FacebookResponse;
import nuudelchin.club.web.dto.GoogleReponse;
import nuudelchin.club.web.dto.NaverResponse;
import nuudelchin.club.web.dto.OAuth2Response;
import nuudelchin.club.web.entity.UserEntity;
import nuudelchin.club.web.mapper.UserMapper;

@Service
public class MyOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger log = LoggerFactory.getLogger(MyOAuth2UserService.class);

	private final UserMapper userMapper;

    public MyOAuth2UserService(UserMapper userMapper) {

        this.userMapper = userMapper;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info(oAuth2User.getAttributes().toString());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        
        if (registrationId.equals("facebook")) {

            log.info("facebook" + oAuth2User.getAttributes());
            oAuth2Response = new FacebookResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {

            log.info("google" + oAuth2User.getAttributes());
            oAuth2Response = new GoogleReponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("naver")) {

            log.info("naver" + oAuth2User.getAttributes());
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("twitter")) {
        	
        	log.info("twitter" + oAuth2User.getAttributes());
            return null;
        }
        else if (registrationId.equals("kakao")) {

            log.info("kakao" + oAuth2User.getAttributes());
            return null;
        }
        else {

            return null;
        }
        
        String username = oAuth2Response.getUsername(); 
        String fullname = oAuth2Response.getName();
        String email = oAuth2Response.getEmail();
        String pictureUrl = oAuth2Response.getPictureURL();        
        String role = "ROLE_USER";
        
        byte[] pictureBytes = null;      	
    	try {
    		if(pictureUrl != null) {
    			pictureBytes = DownloadUtility.downloadImage(pictureUrl);	
    		}			
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        UserEntity userEntity = userMapper.selectByUsername(username);
        
        if(userEntity == null) {
        	
        	userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setFullname(fullname);
            userEntity.setEmail(email);
            userEntity.setPicture(pictureBytes);
            userEntity.setRole(role);

            userMapper.insert(userEntity);
        }
        else {
        	
        	userEntity.setUsername(username);
        	userEntity.setFullname(fullname);
        	userEntity.setEmail(email);
            userEntity.setPicture(pictureBytes);

            userMapper.update(userEntity);
        }
        
        return new MyOAuth2User(oAuth2Response, role);
    }
}
