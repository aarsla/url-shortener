package com.simpastudio.shortener.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.simpastudio.shortener.domain.Url;
import com.simpastudio.shortener.service.AccountRepository;
import com.simpastudio.shortener.service.UrlRepository;

@Controller
public class RedirectController {

	@Autowired AccountRepository accountRepository;
	@Autowired UrlRepository urlRepository;

	/**
	 * Redirect shortcode to original url
	 */
    @RequestMapping(value="/{shortcode}", method=RequestMethod.GET)
    public ResponseEntity<String> redirect(@PathVariable("shortcode") String shortcode) {

    	HttpHeaders headers = new HttpHeaders();

    	Url existingUrl = urlRepository.findByShortcode(shortcode);

    	if (existingUrl != null) {
    		existingUrl.setVisits(existingUrl.getVisits()+1);
    		urlRepository.save(existingUrl);
    		
    		headers.add("Location", existingUrl.getUrl());
    		
    		switch(existingUrl.getRedirectType()) {
    			case 301:
    	        	return new ResponseEntity<String>(null, headers, HttpStatus.MOVED_PERMANENTLY);
    			default: //302
    	        	return new ResponseEntity<String>(null, headers, HttpStatus.FOUND);
    		}
    		
    	} else { //404
        	return new ResponseEntity<String>(null, headers, HttpStatus.NOT_FOUND);
    	}    	
    }

}
