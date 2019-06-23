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

            // Un-secure H2 Database
            .antMatchers("/h2-console/**/**").permitAll()
            .antMatchers("/login").permitAll()
            .antMatchers("/logout").permitAll()
            .antMatchers("/auth/**").permitAll()
            .anyRequest().authenticated();

       httpSecurity
            .addFilterBefore(authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity
            .headers()
            .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
            .cacheControl();
}
	
	/*@Override
	protected void configure(HttpSecurity http) throws Exception { 
	    http
	    .cors().configurationSource(corsConfigurationSource())
	    .and()
	    	.csrf().disable()
	    	.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
	    .and()
	    	.formLogin()
	        .permitAll()
	    	//.successHandler(ajaxSuccessHandler)
	    	//.failureHandler(myFailureHandler)
	    .and()
	    	.authorizeRequests()	 
	    	.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()  	
	    	.antMatchers("/apix/**").authenticated()
	    	.antMatchers("/api/admin/**").hasRole("ADMIN")
	    	.anyRequest().permitAll()
	       	.and().httpBasic()	
	    .and()
	    	.logout();
	    
	    http
	     .addFilterBefore(authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}*/
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }
}
