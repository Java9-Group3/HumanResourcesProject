package com.hrmG3.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hrmG3.repository.entity.enums.EStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenProvider {
    @Value("123")
    String secretKey;
    @Value("asd")
    String audience;
    @Value("qwe")
    String issuer;

    public Optional<String> createMailToken(Long id, EStatus status){
        String token = null;
        Date date = new Date(System.currentTimeMillis() + (1000*60*60*24));
        try {
            token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .withClaim("id", id)
                    .withClaim("status", status.toString() )
                    .sign(Algorithm.HMAC512(secretKey));
            return Optional.of(token);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }



}
