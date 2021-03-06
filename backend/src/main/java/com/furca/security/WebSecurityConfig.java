package com.furca.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	// Custom JWT based security filter
    @Autowired
    JwtAuthorizationTokenFilter authorizationTokenFilter;
	
	@Autowired
    private UserDetailsService userDetailsService;
		
	@Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
	}
	 
	@Bean
	public PasswordEncoder  encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
	    StrictHttpFirewall firewall = new StrictHttpFirewall();
	    //firewall.setAllowUrlEncodedSlash(true);
	    firewall.setAllowSemicolon(true);
	    return firewall;
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
            .antMatchers("/registration").permitAll()
            .antMatchers("/calc").permitAll()
            .antMatchers("/check-username").permitAll()
            .antMatchers("/upload").permitAll()
            .antMatchers("/images/**").permitAll()
            .antMatchers("/results/**").permitAll()
            
            
            .antMatchers(HttpMethod.GET, "/api/pages").permitAll()
            .antMatchers(HttpMethod.GET, "/api/articles").permitAll()
            .antMatchers(HttpMethod.GET, "/api/categories").permitAll()
            .antMatchers(HttpMethod.GET, "/api/tags").permitAll()
            .antMatchers(HttpMethod.GET, "/api/menuItems").permitAll()
            .antMatchers(HttpMethod.GET, "/api/**").permitAll()
            .antMatchers("/files/**").hasAnyRole("ADMIN");
            
            //.antMatchers("/auth/**").permitAll()
            //.antMatchers(HttpMethod.GET, "/api/**").hasAnyRole("EDITOR");
            //.antMatchers("/api/**").hasAnyRole("ADMIN")
            //.anyRequest().authenticated();

       httpSecurity
            .addFilterBefore(authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity
            .headers()
            .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
            .cacheControl();
	}
	
	/*@Override
    public void configure(WebSecurity web) throws Exception {
      web
        .ignoring()
           .antMatchers("/resources/**");
    }*/
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }
}
