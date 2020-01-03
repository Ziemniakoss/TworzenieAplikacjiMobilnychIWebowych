package com.ziemniak.webserv;

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
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        //TODO
        final String authHeader = req.getHeader("Authorization");
        String username = null;
        String jwt = null;
        if(authHeader != null){
            UserDetails user = userDetailsService.loadUserByUsername("foo");
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(req,res);
    }
}
