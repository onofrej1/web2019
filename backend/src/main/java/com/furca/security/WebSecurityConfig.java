package com.furca.security;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
    
	
	@Autowired
	private AuthenticationSuccessHandler mySuccessHandler;
	
	@Autowired
	private AjaxSuccessHandler ajaxSuccessHandler;
	
	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	private MyAuthenticationEntryPoint myAuthenticationEntryPoint;
	
	@Autowired
    private UserDetailsService userDetailsService;
	
	private SimpleUrlAuthenticationFailureHandler myFailureHandler = new SimpleUrlAuthenticationFailureHandler();
	
	@Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
	}
	
	/*@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
	    auth.inMemoryAuthentication()
	        .withUser("admin").password(encoder().encode("admi")).roles("ADMIN")
	        .and()
	        .withUser("user").password(encoder().encode("user")).roles("USER");
	}*/
	 
	@Bean
	public PasswordEncoder  encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    List<String> allowOrigins = Arrays.asList("*");
	    configuration.setAllowedOrigins(allowOrigins);
	    configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "OPTIONS"));
	    configuration.setAllowedHeaders(Arrays.asList("*"));	    
	    configuration.setAllowCredentials(true);
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception { 
	    http
	    .cors().configurationSource(corsConfigurationSource())
	    .and()
	    	.csrf().disable()
	    	.exceptionHandling()
	    	.authenticationEntryPoint(myAuthenticationEntryPoint)
	    .and()
	    	.formLogin()
	        .permitAll()
	    	.successHandler(ajaxSuccessHandler)
	    	.failureHandler(myFailureHandler)
	    .and()
	    	.authorizeRequests()	    	
	    	.antMatchers("/apix/**").authenticated()
	    	.antMatchers("/greetingx").authenticated()
	    	.antMatchers("/api/admin/**").hasRole("ADMIN")
	    	.anyRequest().permitAll()
	       	.and().httpBasic()	
	    .and()
	    	.logout();
	}
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }
	
	/*@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/	
}
