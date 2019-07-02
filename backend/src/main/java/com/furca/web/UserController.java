package com.furca.web;

import com.furca.model.User;
import com.furca.security.JwtTokenUtil;
import com.furca.service.SecurityService;
import com.furca.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody User loginUser) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final User user = userService.findByUsername(loginUser.getUsername());
        final String token = jwtTokenUtil.generateToken(user);
        
        return ResponseEntity.ok(token);
    }
	
	
	/*@RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody User login) throws ServletException {

        String jwtToken = "";

        if (login.getUsername() == null || login.getPassword() == null) {
            throw new ServletException("Please fill in username and password");
        }

        String username = login.getUsername();
        String password = login.getPassword();

        User user = userService.findByUsername(username);

        if (user == null) {
            throw new ServletException("User email not found.");
        }


        if (!bCryptPasswordEncoder.matches(user.getPassword(), password)) {
            //throw new ServletException("Invalid login. Please check your name and password.");
        }
        System.out.println(user.getUsername());
        jwtToken = Jwts.builder()
        		.setSubject(username)
        		.claim("roles", "user")
        		.setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey")
                .compact();

        return jwtToken;
    }*/

	// @Autowired
	// private UserValidator userValidator;

	@PostMapping("/registration")
	public ResponseEntity registration(@Valid @RequestBody User user) {
		// userValidator.validate(userForm, bindingResult);
		
		//if (result.hasErrors()) {
			// System.out.print(result.getFieldError("firstName").getDefaultMessage());
			//return ResponseEntity.ok("fail");
		//}

		//userService.save(user);

		// securityService.autologin(userForm.getUsername(),
		// userForm.getPasswordConfirm());

		return ResponseEntity.ok("success");
	}

	/*
	 * @GetMapping({"/", "/welcome"}) public String welcome(Model model) { return
	 * "welcome"; }
	 */
}
