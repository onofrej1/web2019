package com.furca.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AjaxSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
 private RequestCache requestCache = new HttpSessionRequestCache();
	
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
	  response.getWriter().write(new ObjectMapper().writeValueAsString("abcd"));
      response.setStatus(200);
  }
  
  public void setRequestCache(final RequestCache requestCache) {
      this.requestCache = requestCache;
  }
}
