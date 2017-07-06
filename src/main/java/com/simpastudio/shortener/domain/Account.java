package com.simpastudio.shortener.domain;

import java.io.Serializable;
import java.util.Random;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable = false)
	@NotEmpty(message="accountId name must not be empty")
	private String accountId;
	@JsonIgnore
    private String password;

	@JsonIgnore
	@OneToMany(mappedBy="account")
    private Set<Url> urls;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword() {
		this.password = randomString(8);
	} 
	
	public void setPassword(String password) {
		this.password = password;
	} 
	
	public Set<Url> getUrls() {
		return urls;
	}

	public void setUrls(Set<Url> urls) {
		this.urls = urls;
	}
	
	private String randomString(int len) 
	{
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		Random rnd = new Random();
		
 	    StringBuilder sb = new StringBuilder( len );
 	    for( int i = 0; i < len; i++ ) 
 	    	sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	    return sb.toString();
	}

}
