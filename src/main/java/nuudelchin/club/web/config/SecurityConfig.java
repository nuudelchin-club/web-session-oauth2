package nuudelchin.club.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import nuudelchin.club.web.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {

        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.disable());

        http.formLogin((login) -> login.disable());

        http.httpBasic((basic) -> basic.disable());
        
        http
        	.oauth2Login((oauth2) -> oauth2
    			.loginPage("/login")
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
