package nuudelchin.club.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class PrivacyPolicy {

	@GetMapping("/privacy-policy")
	public String privacyPolicy() {
			
		return "Privacy policy";
	}
}
