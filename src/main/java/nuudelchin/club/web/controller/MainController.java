package nuudelchin.club.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import nuudelchin.club.web.common.ImageUtility;
import nuudelchin.club.web.entity.UserEntity;
import nuudelchin.club.web.service.MainService;

@Controller
public class MainController {
	
	private final MainService mainService;
	
	public MainController(MainService mainService) {
		
		this.mainService = mainService;
	}

	@GetMapping("/")
	public String main(Model model) {
		
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String username = authentication.getName();
		
		UserEntity userEntity = mainService.findByUsername(username);
		
		if(userEntity != null) {
			
			String fullname = userEntity.getFullname();
			String role = userEntity.getRole();
			String email = userEntity.getEmail();
			byte[] pictureBytes = userEntity.getPicture();
			
			String picture = pictureBytes != null 
		            ? "data:image/png;base64," + ImageUtility.encodeToBase64(pictureBytes)
		            : "/images/default-profile.png";
			
			model.addAttribute("fullname", fullname);
			model.addAttribute("role", role);
			model.addAttribute("email", email);
			model.addAttribute("picture", picture);
		}
		
		return "main";
	}
}
