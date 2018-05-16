package com.h1infotech.smarthive.common;

import java.util.Map;
import java.util.Date;
import org.slf4j.Logger;
import java.util.HashMap;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Component;
import com.h1infotech.smarthive.domain.BeeFarmer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import com.h1infotech.smarthive.repository.BeeFarmerRepository;

@Component
public class JwtTokenUtil {
	
	private Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
	
    private static final String CLAIM_KEY_USERNAME = "a";
    
    private static final String CLAIM_KEY_CREATED = "b";
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    @Value("${jwt.secret}")
    private String secret;
    
	@Autowired
	private BeeFarmerRepository beeFarmerRepository;

    public Date getCreatedDateFromToken(String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            return new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            return null;
        }
    }

    public Date getExpirationDateFromToken(String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    public String getUsernameFromToken(String token) {
        final Claims claims = getClaimsFromToken(token);
        return String.valueOf(claims.get(CLAIM_KEY_USERNAME));
    }
    
    public BeeFarmer getBeeFarmerFromToken(String token) {
    	if(StringUtils.isEmpty(token)) {
    		return null;
    	}
    	final Claims claims = getClaimsFromToken(token);
    	String userName = String.valueOf(claims.get(CLAIM_KEY_USERNAME));
    	if(StringUtils.isEmpty(userName)) {
    		return null;
    	}
    	try {
    		return beeFarmerRepository.findDistinctFirstByName(userName);
    	}catch(Exception e) {
    		logger.error("Get Bee Farmer from Database Error", e);
    		return null;
    	}
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    public String refreshToken(String token) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, getUsernameFromToken(token));
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    public Boolean validateToken(String token) {
        final String username = getUsernameFromToken(token);
        return username != null && !isTokenExpired(token);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
