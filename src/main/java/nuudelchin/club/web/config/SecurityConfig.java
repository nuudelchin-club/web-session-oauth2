package nuudelchin.club.web.config;

import nuudelchin.club.web.oauth2.MyClientRegistrationRepository;
import nuudelchin.club.web.oauth2.MyOAuth2AuthorizedClientService;
import nuudelchin.club.web.service.MyOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final MyOAuth2UserService myOAuth2UserService;
	private final MyClientRegistrationRepository myClientRegistrationRepository;
	private final MyOAuth2AuthorizedClientService myOAuth2AuthorizedClientService;
    private final JdbcTemplate jdbcTemplate;

    public SecurityConfig(MyOAuth2UserService myOAuth2UserService
    		, MyClientRegistrationRepository myClientRegistrationRepository
    		, MyOAuth2AuthorizedClientService myOAuth2AuthorizedClientService
    		, JdbcTemplate jdbcTemplate) {

        this.myOAuth2UserService = myOAuth2UserService;
        this.myClientRegistrationRepository = myClientRegistrationRepository;
        this.myOAuth2AuthorizedClientService = myOAuth2AuthorizedClientService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.disable());

        http.formLogin((login) -> login.disable());

        http.httpBasic((basic) -> basic.disable());
        
        http
        	.oauth2Login((oauth2) -> oauth2
    			.loginPage("/login")
    			.clientRegistrationRepository(myClientRegistrationRepository.clientRegistrationRepository())
    			.authorizedClientService(myOAuth2AuthorizedClientService.oAuth2AuthorizedClientService(jdbcTemplate, myClientRegistrationRepository.clientRegistrationRepository()))
                .userInfoEndpoint((config) -> config.userService(myOAuth2UserService)));

        http.authorizeHttpRequests((auth) -> auth
        		.requestMatchers("/", "privacy-policy", "term-of-service", "userdata-deletion", "/oauth2/**", "/login/**").permitAll()
        		.anyRequest().authenticated());
        
        //http.sessionManagement((auth) -> auth
        //        .maximumSessions(1)
        //        .maxSessionsPreventsLogin(true));
		
		http.sessionManagement((auth) -> auth
                .sessionFixation().changeSessionId());
		
		http.logout((auth) -> auth
				.logoutUrl("/logout")
                .logoutSuccessUrl("/"));

        return http.build();
    }
}
