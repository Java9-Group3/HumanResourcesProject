package com.hrmanagement.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hrmanagement.exception.CompanyManagerException;
import com.hrmanagement.exception.ErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class JwtTokenProvider {
    @Value("${jwt.secretkey}")
    String secretKey;
    @Value("${jwt.audience}")
    String audience;
    @Value("${jwt.issuer}")
    String issuer;

    public Optional<String> createToken(Long id){
        String token = null;
        Date date = new Date(System.currentTimeMillis() + (1000*60*60*24*5));
        try {
            token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .withClaim("id", id)
                    .sign(Algorithm.HMAC512(secretKey));
            return Optional.of(token);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }



    public Optional<Long> getIdFromToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).withAudience(audience).withIssuer(issuer).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT == null){
                throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
            }
            Long id = decodedJWT.getClaim("id").asLong();
            return Optional.of(id); // == Optional<Long>
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
        }
    }
    public Optional<Long> getAuthIdFromToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).withAudience(audience).withIssuer(issuer).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT == null){
                throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
            }
            Long id = decodedJWT.getClaim("authId").asLong();
            return Optional.of(id); // == Optional<Long>
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
        }
    }

    public List<String> getRoleFromToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).withAudience(audience).withIssuer(issuer).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT == null){
                throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
            }
            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
            return roles;
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
        }
    }
    public Optional<Long> getCompanyIdFromToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).withAudience(audience).withIssuer(issuer).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT == null){
                throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
            }
            Long id = decodedJWT.getClaim("companyId").asLong();
            return Optional.of(id); // == Optional<Long>
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new CompanyManagerException(ErrorType.INVALID_TOKEN);
        }
    }

    //Password encode
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}