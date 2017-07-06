package com.simpastudio.shortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.simpastudio.shortener.service.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired CustomAuthenticationProvider customAuthProvider;
	
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    		http.csrf().disable();
            http.authorizeRequests()
                 .antMatchers("/register", "/statistic")
                 .authenticated()
                 .and().httpBasic();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder)
            throws Exception {
               authManagerBuilder.authenticationProvider(customAuthProvider);
    }
}