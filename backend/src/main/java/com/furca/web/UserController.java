package com.furca.web;

import com.furca.model.User;
import com.furca.security.JwtTokenUtil;
import com.furca.service.UserService;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<String> login(@RequestBody User loginUser)  {
		Authentication authentication = null;
		final User user = userService.findByUsername(loginUser.getUsername());
		
		authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		final String token = jwtTokenUtil.generateToken(user);

		return ResponseEntity.ok(token);
	}

	/*
	 * @ExceptionHandler({AuthenticationException.class}) public
	 * ResponseEntity<String> handleAuthenticationException(AuthenticationException
	 * e) { System.out.println(e.getMessage()); return
	 * ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad credentials"); }
	 */

	/*
	 * @RequestMapping(value = "/check-username", method = RequestMethod.POST)
	 * public ResponseEntity<String> checkUsername(@RequestParam(value="username")
	 * String username) { System.out.println(username); return
	 * ResponseEntity.ok("aaa"); }
	 */

	@PostMapping("/registration")
	public ResponseEntity<String> registration(@Valid @RequestBody User user) {
		userService.save(user);

		return ResponseEntity.ok("success");
	}

}
