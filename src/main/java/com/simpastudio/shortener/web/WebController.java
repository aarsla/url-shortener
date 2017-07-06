package com.simpastudio.shortener.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.simpastudio.shortener.service.AccountRepository;
import com.simpastudio.shortener.service.UrlRepository;

@Controller
public class WebController {
    
	@Autowired AccountRepository accountRepository;
	@Autowired UrlRepository urlRepository;
	
	/**
	 * Home page
	 */
	@RequestMapping("/")
    public String index(Model model) {
		model.addAttribute("accounts", accountRepository.findAll());
		model.addAttribute("urls", urlRepository.findAll());
		model.addAttribute("accountRepo", accountRepository);
    	return "index";		
    }
	
	/**
	 * Help page
	 */
	@RequestMapping("/help")
    public String help() {
    	return "help";
    }
}
