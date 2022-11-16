package com.example.realworld.security;

import com.example.realworld.entity.User;
import com.example.realworld.model.TokenPayload;
import com.example.realworld.repository.UserRepository;
import com.example.realworld.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader(("Authorization"));
        String token = null;
        TokenPayload tokenPayload = null;

        if(requestTokenHeader!= null && requestTokenHeader.startsWith("Token ")){
            token = requestTokenHeader.substring(6).trim();
            try {
                tokenPayload = jwtTokenUtil.getTokenPayLoad(token);

            } catch (IllegalArgumentException e){
                System.out.println("Unable tp get Jwt");
            }
            catch (ExpiredJwtException e){
                System.out.println("Token has expired");
            }
        }
        else {
            System.out.println("JWT Token does not start with 'Token '");
        }

        if(tokenPayload!= null && SecurityContextHolder.getContext().getAuthentication() == null){
            Optional<User> userOptional = userRepository.findById(tokenPayload.getUserId());
            if(userOptional.isPresent()){
                User user = userOptional.get();
                //Check hop le token
                if(jwtTokenUtil.validate(token, user)){
                    List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
                    UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorityList);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);

    }

}

