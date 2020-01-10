package com.ziemniak.webserv;

import com.ziemniak.webserv.repositories.BlacklistedJwtRepository;
import com.ziemniak.webserv.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
	@Autowired
	private MyUserDetailsService userDetailsService;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private BlacklistedJwtRepository blacklistedJwtRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = req.getHeader("Authorization");
		String username = null;
		String jwt = null;
		if (authHeader != null) {
			jwt = authHeader.substring(7);
			if (!blacklistedJwtRepository.isBlacklisted(jwt)) {
				username = jwtUtils.getUsername(jwt);
				UserDetails user = userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(token);
			}
		}
		filterChain.doFilter(req, res);
	}
}
