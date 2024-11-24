package nuudelchin.club.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class TermOfService {

	@GetMapping("/term-of-service")
	public String termOfService() {
			
		return "Term of service";
	}
}
