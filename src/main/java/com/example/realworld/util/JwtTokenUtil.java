package com.example.realworld.util;

import antlr.Token;
import com.example.realworld.entity.User;
import com.example.realworld.model.TokenPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private final String secret = "NEO_22";


    public String generateToken(User user, long expiredDate) {
        Map<String, Object> claims = new HashMap<>();
        TokenPayload tokenPayload = TokenPayload.builder().userId(user.getId()).email(user.getEmail()).build();
        claims.put("payload", tokenPayload);
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredDate * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    //get infor user api
    public TokenPayload getTokenPayLoad(String token) {
        return getClaimsFromToken(token, (Claims claims) -> {
            Map<String, Object> mapResult = (Map<String, Object>) claims.get("payload");
            return TokenPayload.builder().userId((int)mapResult.get("userId")).email((String) mapResult.get("email")).build();
        });
    }

    private<T> T getClaimsFromToken(String token, Function<Claims, T> claimResolver){
        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claimResolver.apply(claims);
    }

    public boolean validate(String token, User user) {
        TokenPayload tokenPayload = getTokenPayLoad(token);
        return tokenPayload.getUserId() == user.getId() && tokenPayload.getEmail().equalsIgnoreCase(user.getEmail())&& !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiredDate = getClaimsFromToken(token, Claims :: getExpiration);
        return expiredDate.before(new Date());
    }
}
