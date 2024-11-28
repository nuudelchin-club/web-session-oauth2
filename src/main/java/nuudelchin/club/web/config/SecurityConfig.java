package nuudelchin.club.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import nuudelchin.club.web.oauth2.CustomClientRegistrationRepository;
import nuudelchin.club.web.oauth2.CustomOAuth2AuthorizedClientService;
import nuudelchin.club.web.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomClientRegistrationRepository customClientRegistrationRepository;
	private final CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService;
    private final JdbcTemplate jdbcTemplate;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService
    		, CustomClientRegistrationRepository customClientRegistrationRepository
    		, CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService
    		, JdbcTemplate jdbcTemplate) {

        this.customOAuth2UserService = customOAuth2UserService;
        this.customClientRegistrationRepository = customClientRegistrationRepository;
        this.customOAuth2AuthorizedClientService = customOAuth2AuthorizedClientService;
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
    			.clientRegistrationRepository(customClientRegistrationRepository.clientRegistrationRepository())    			
    			.authorizedClientService(customOAuth2AuthorizedClientService.oAuth2AuthorizedClientService(jdbcTemplate, customClientRegistrationRepository.clientRegistrationRepository()))
                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                        .userService(customOAuth2UserService)));

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
