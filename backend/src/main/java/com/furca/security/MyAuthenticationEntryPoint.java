package com.furca.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component("myAuthenticationEntryPoint")
public final class MyAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
 
    @Override
    public void commence(
        final HttpServletRequest request, 
        final HttpServletResponse response, 
        final AuthenticationException authException) throws IOException {
        System.out.print("aaaa");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, 
          "Unauthorized");
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("Baeldung");
        super.afterPropertiesSet();
    }
}
