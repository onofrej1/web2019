package com.furca.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AjaxSuccessHandler ajaxSuccessHandler;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	// Custom JWT based security filter
    @Autowired
    JwtAuthorizationTokenFilter authorizationTokenFilter;
	
	@Autowired
    private UserDetailsService userDetailsService;
	
	private SimpleUrlAuthenticationFailureHandler myFailureHandler = new SimpleUrlAuthenticationFailureHandler();
	
	@Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
	}
	 
	@Bean
	public PasswordEncoder  encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	
	@Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            // we don't need CSRF because our token is invulnerable
            .csrf().disable()

            .exceptionHandling()
            	.authenticationEntryPoint(jwtAuthenticationEntryPoint)
            	.and()

            // don't create session
            .sessionManagement()
            	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            	.and()

            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
            // Un-secure H2 Database
            .antMatchers("/h2-console/**/**").permitAll()
            .antMatchers("/login").permitAll()
            .antMatchers("/auth/**").permitAll()
            .antMatchers("/api/**").hasAnyRole("USER")
            .anyRequest().authenticated();

       httpSecurity
            .addFilterBefore(authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity
            .headers()
            .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
            .cacheControl();
	}
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }
}
