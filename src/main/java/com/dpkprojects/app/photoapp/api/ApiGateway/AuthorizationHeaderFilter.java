package com.dpkprojects.app.photoapp.api.ApiGateway;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Autowired
    Environment env;
    public AuthorizationHeaderFilter() {
        super(Config.class);
    }
    @Override
    /**
     * this method will contain the main logic that will check if
     * authorization header is present or not and
     * will validate jwt token
     */
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "no authorization header present", HttpStatus.UNAUTHORIZED);
            }
            //getting authorization headers and jwt token
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwtToken = authorizationHeader.replace("Bearer", "");
            //validating jwt token
            if (!isJwtTokenValid(jwtToken)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }
            //it will delegate the flow to next filter in chain
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String noAuthorizationHeaderPresent, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public static class Config {
        //put configuration properties here to configure the behaviour of our custom filter
    }

    private boolean isJwtTokenValid(String jwtToken) {
        boolean valid = false;
        String subject = null;
        String tokenKey = env.getProperty("token.secret_key");
        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenKey.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        JwtParser jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();
        try {
            Claims claims = jwtParser.parseSignedClaims(jwtToken).getPayload();
            subject = (String) claims.get("sub");

        } catch (Exception ex) {
            valid = false;
        }
        if (subject != null || !subject.isEmpty()) {
            valid = true;
        }
        return valid;
    }
}
