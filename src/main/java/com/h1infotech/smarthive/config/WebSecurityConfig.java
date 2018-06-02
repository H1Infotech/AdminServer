package com.h1infotech.smarthive.config;

import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.h1infotech.smarthive.common.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.h1infotech.smarthive.common.RestLogoutSuccessHandler;
import com.h1infotech.smarthive.common.AuthenticationTokenFilter;
import com.h1infotech.smarthive.common.RestAuthenticationEntryPoint;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
	@Autowired
    private UserDetailsServiceImpl userDetailsService;
    
	@Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    
	@Autowired
    private RestLogoutSuccessHandler restLogoutSuccessHandler;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new AuthenticationTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = "messageConverter")
    public MappingJackson2HttpMessageConverter messageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean(name = "stringRedisTemplate")
    public StringRedisTemplate getStringRedisTemplate(RedisConnectionFactory  redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
                .logout().logoutUrl("/logout").permitAll()
                .addLogoutHandler(restLogoutSuccessHandler)
                .invalidateHttpSession(true).clearAuthentication(true).and()
                .formLogin().disable()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.POST).permitAll().antMatchers("/adminLogin**").permitAll()
                .antMatchers(HttpMethod.POST).permitAll().antMatchers("/register**").permitAll()
                .antMatchers(HttpMethod.POST).permitAll().antMatchers("/adminUpdatePassword**").permitAll()
                .antMatchers(HttpMethod.POST).permitAll().antMatchers("/adminSMSService**").permitAll()
                .anyRequest().authenticated();
        //  httpSecurity.headers().cacheControl();
        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }
}
