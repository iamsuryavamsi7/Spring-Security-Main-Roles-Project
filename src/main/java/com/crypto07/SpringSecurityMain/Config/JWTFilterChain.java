package com.crypto07.SpringSecurityMain.Config;

import com.crypto07.SpringSecurityMain.Service.JwtService;
import com.crypto07.SpringSecurityMain.Service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTFilterChain extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Request will come to this filter before going to another filters and controller.

        // I have to check the request is having the Bearer token in "Authorization" header or not

        // If the "Authorization" header is having the Bearer token i have to check the format of it

        final String authHeader = request.getHeader("Authorization");

        final String jwtToken;

        final String userName;

        if ( authHeader == null || !authHeader.startsWith("Bearer ") ) {

            filterChain.doFilter(request, response);

            return;

        }

        jwtToken = authHeader.substring(7);

        userName = jwtService.extractUserName(jwtToken);

        if ( userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = myUserDetailsService.loadUserByUsername(userName);

            if (jwtService.validateToken(jwtToken, userDetails)) {

                // Now we got the request with valid jwtToken but not in authenticated object now we have to convert

                // For converting we have to use the Authenticate class implemented by UsernamePasswordAuthenticationToken

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("Now your request is turned into Authenticated Object");

            } else {

                    System.out.println("\n\n\nJWT Token is invalid or expired\n\n\n");

            }

        } else {

            System.out.println("\n\n\n Sorry User not found (or User is already authenticated \n\n\n");

            throw new UsernameNotFoundException("Sorry Username not found (or) User is already authenticated");

        }

        filterChain.doFilter(request, response);

    }

}
