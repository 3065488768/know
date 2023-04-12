package com.example.know.security.filter;

import com.example.know.security.MyUserDetailsService;
import com.example.know.security.utils.JwtProperties;
import com.example.know.security.utils.JwtProvider;
import com.example.know.security.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtProvider jwtProvider;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;


    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(jwtProperties.getRequestHeader());
        if (StringUtils.hasLength(authHeader) && authHeader.startsWith(this.jwtProperties.getTokenPrefix())) {
            String authToken = authHeader.substring(this.jwtProperties.getTokenPrefix().length());
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            logger.info("checking username:" + username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.info("authenticated user:" + username);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
