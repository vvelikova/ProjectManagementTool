package com.vvelikova.ppmtool.security;

import com.vvelikova.ppmtool.domain.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.vvelikova.ppmtool.security.SecurityConstants.EXPIRATION_TIME;
import static com.vvelikova.ppmtool.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {
    // generate the token
    public String generateToken(Authentication authentication){
        //at this point the user is authenticated and we want to get that user
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());

        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        String userId = Long.toString(user.getId()); // we need to cast to a String; the token will contain user info and the token is a String

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", (Long.toString(user.getId())));
        claims.put("username", user.getUsername());
        claims.put("fullName", user.getFullName());


        //the following will generate the token when we have a valid user details
        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims) // info about the user; can do it one by one OR using a Map
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }


    // validate the token
    public boolean validateToken (String token) {
        try{

            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT exception");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty");
        }

        return false;
    }

    // get the user id from the token

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String)claims.get("id");

        return Long.parseLong(id);
    }
}
