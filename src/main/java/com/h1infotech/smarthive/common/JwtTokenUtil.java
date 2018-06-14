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
import com.h1infotech.smarthive.domain.Admin;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.h1infotech.smarthive.repository.AdminRepository;
import com.h1infotech.smarthive.service.AdminRightService;

import org.springframework.beans.factory.annotation.Autowired;

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
	private AdminRepository adminRepository;
	@Autowired
	AdminRightService adminRightService;

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
    
    public Admin getAdmin(String token) {
    	if(StringUtils.isEmpty(token)) {
    		return null;
    	}
    	final Claims claims = getClaimsFromToken(token);
    	String userName = String.valueOf(claims.get(CLAIM_KEY_USERNAME));
    	if(StringUtils.isEmpty(userName)) {
    		return null;
    	}
    	try {
    		Admin admin = adminRepository.findDistinctFirstByUsername(userName);
    		if(admin!=null && admin.getId()!=null) {
    			admin.setRights(adminRightService.getAdminRights(admin.getId()));
    		}
    		return admin;
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

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
