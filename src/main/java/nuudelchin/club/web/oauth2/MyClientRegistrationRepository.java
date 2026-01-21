package nuudelchin.club.web.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
public class MyClientRegistrationRepository {

	private final SocialClientRegistration socialClientRegistration;
	
	public MyClientRegistrationRepository(SocialClientRegistration socialClientRegistration) {

        this.socialClientRegistration = socialClientRegistration;
    }
	
	public ClientRegistrationRepository clientRegistrationRepository() {

        return new InMemoryClientRegistrationRepository(
        		socialClientRegistration.naverClientRegistration()
        		, socialClientRegistration.googleClientRegistration()
        		, socialClientRegistration.facebookClientRegistration());
    }
}
