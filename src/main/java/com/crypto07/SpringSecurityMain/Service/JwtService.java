package com.crypto07.SpringSecurityMain.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final String SECRET = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

    public SecretKey getSignInKey(){

        return Keys.hmacShaKeyFor(SECRET.getBytes());

    }

    public String generateToken(UserDetails userDetails){

        return Jwts.builder()
                .claim("Authorities", populateClaims(userDetails.getAuthorities()))
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(getSignInKey())
                .compact();

    }

    private String populateClaims(Collection<? extends GrantedAuthority> authorities) {

        Set<String> authoritesSet = new HashSet<>();

        for ( GrantedAuthority authority: authorities) {

            // By using the enhanced for loop get the authorites for users

            authoritesSet.add(authority.getAuthority());

        }

        return String.join(",", authoritesSet);

    }


    public Claims extractAllClaims(String jwtToken){

        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();

    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsTFunction){

        Claims claims = extractAllClaims(jwtToken);

        return claimsTFunction.apply(claims);

    }

    public String extractUserName(String jwtToken) {

        // Extract the UserName from Token by using Claims

        return extractClaim(jwtToken, Claims::getSubject);

    }

    public boolean validateToken(String jwtToken, UserDetails userDetails) {

        // Compare the username from jwtToken and userDetails

        boolean userMatched = extractUserName(jwtToken).equals(userDetails.getUsername());

        boolean isTokenNotExpired = !isTokenExpired(jwtToken);

        return userMatched && isTokenNotExpired;

    }

    private boolean isTokenExpired(String jwtToken) {

        return extractExpiration(jwtToken).before(new Date());

    }

    private Date extractExpiration(String jwtToken) {

        return extractClaim(jwtToken, Claims::getExpiration);

    }

}
