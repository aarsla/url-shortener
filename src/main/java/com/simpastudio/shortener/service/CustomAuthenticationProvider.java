package com.simpastudio.shortener.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.simpastudio.shortener.domain.Account;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
 
	@Autowired AccountRepository accountRepository;
	
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	if (authentication.isAuthenticated())
            return authentication;
    	
        String accountId = authentication.getName();
        String password = authentication.getCredentials().toString();

        System.out.println("authenticate: "+accountId+" - "+password);
        Account existingUser = accountRepository.findByAccountId(accountId);

        if (existingUser == null) {
        	throw new UsernameNotFoundException("Username Not Found");
        }
        
        if (!existingUser.getPassword().equals(password)) {
            throw new BadCredentialsException("Bad Credentials");
        }

        ArrayList<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        Authentication auth = new UsernamePasswordAuthenticationToken(accountId, password, grantedAuths);
        return auth;
    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}