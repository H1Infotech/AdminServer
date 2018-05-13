package com.h1infotech.smarthive.common;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Component(value = "restLogoutSuccessHandler")
public class RestLogoutSuccessHandler extends SecurityContextLogoutHandler {
    @Autowired
    private MappingJackson2HttpMessageConverter messageConverter;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            stringRedisTemplate.delete(request.getHeader("token"));
            PrintWriter writer = response.getWriter();
            messageConverter.getObjectMapper().writeValue(response.getWriter(), Response.success("success"));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
