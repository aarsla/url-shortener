package com.simpastudio.shortener.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.simpastudio.shortener.domain.Account;
import com.simpastudio.shortener.domain.Url;
import com.simpastudio.shortener.service.AccountRepository;
import com.simpastudio.shortener.service.UrlRepository;
import com.simpastudio.shortener.util.Base62;
import com.simpastudio.shortener.api.AccountResponse;
import com.simpastudio.shortener.api.UrlResponse;

@Controller
public class ConfigurationController {

	@Autowired
	AccountRepository accountRepository;
	@Autowired
	UrlRepository urlRepository;

	/**
	 * Registers new account
	 * 
	 */
	@RequestMapping(value = "/account", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AccountResponse> account(
			@RequestBody @Valid Account account) {

		Account existingAccount = accountRepository.findByAccountId(account
				.getAccountId());

		HttpStatus status;
		AccountResponse accountResponse = new AccountResponse();

		if (existingAccount != null) {
			status = HttpStatus.CONFLICT;

			accountResponse.setStatus(false);
			accountResponse.setDescription("Account already exists");
		} else {
			status = HttpStatus.CREATED;
			account.setPassword();
			accountRepository.save(account);

			accountResponse.setStatus(true);
			accountResponse.setDescription("Your account is now active");
		}

		// accountResponse.setAccountId(account.getAccountId());
		accountResponse.setPassword(account.getPassword());

		return new ResponseEntity<AccountResponse>(accountResponse, status);
	}

	/**
	 * Register new url
	 * 
	 * @throws MalformedURLException
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UrlResponse> registerUrl(@RequestBody Url url)
			throws MalformedURLException {

		HttpStatus status;
		UrlResponse urlResponse = new UrlResponse();

		// Validate sent URL
		try {
			@SuppressWarnings("unused")
			URL realURL = new URL(url.getUrl());
		} catch (MalformedURLException e) {
			status = HttpStatus.EXPECTATION_FAILED;
			urlResponse.setDescription("Malformed URL");

			System.out.println("URL check exception: "
					+ e.getLocalizedMessage());
			return new ResponseEntity<UrlResponse>(urlResponse, status);
		}

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String accountId = auth.getName();

		Account urlOwner = accountRepository.findByAccountId(accountId);
		Url existingUrl = urlRepository.findByUrl(url.getUrl().toString());

		if (existingUrl != null) {
			status = HttpStatus.FOUND;
			urlResponse.setShortcode(existingUrl.getShortcode());
			urlResponse.setDescription("ShortUrl already exists");
		} else {
			url.setAccount(urlOwner);

			String shortcode = getValidShortcode();
			url.setShortcode(shortcode);

			if (url.getRedirectType() != 301) {
				url.setRedirectType(302);
			}

			urlRepository.save(url);

			urlResponse.setShortcode(url.getShortcode());
			status = HttpStatus.CREATED;
		}

		return new ResponseEntity<UrlResponse>(urlResponse, status);
	}

	/**
	 * Get statistics for account
	 */
	@RequestMapping(value = "/statistic/{accountId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Integer>> getAccountStats(
			@PathVariable("accountId") String accountId) {

		HttpStatus status;

		/*
		 * //Probably everyone can see each other's stats based on api design
		 * 
		 * Authentication auth =
		 * SecurityContextHolder.getContext().getAuthentication(); String
		 * authUser = auth.getName();
		 * 
		 * if(authUser != accountId) { status = HttpStatus.FORBIDDEN; return new
		 * ResponseEntity<Map<String,Integer>>(status); }
		 * 
		 * // otherwise we could simply fetch authenticate user's urls Account
		 * urlOwner = accountRepository.findByAccountId(authUser);
		 */

		Account urlOwner = accountRepository.findByAccountId(accountId);

		Map<String, Integer> urls = new HashMap<String, Integer>();

		for (final Url url : urlOwner.getUrls()) {
			urls.put(url.getUrl().toString(), new Integer(url.getVisits()));
		}

		status = HttpStatus.OK;
		return new ResponseEntity<Map<String, Integer>>(urls, status);
	}

	/**
	 * Find unused shortcode
	 */
	private String getValidShortcode() {
		int random = (int) (Math.random() * 999999999 + 1);
		String shortcode = Base62.fromBase10(random);

		Url existingUrl = urlRepository.findByShortcode(shortcode);
		if (existingUrl != null) {
			return getValidShortcode();
		}

		return shortcode;
	}

}
