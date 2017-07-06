package com.simpastudio.shortener.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UrlResponse {

	private String shortcode;
	private String description;
	
	public String getShortcode() {
		return shortcode;
	}
	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public String getDescription() {
		return description;
	
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
