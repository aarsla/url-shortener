package com.simpastudio.shortener.service;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.simpastudio.shortener.domain.Url;

@Repository
public interface UrlRepository extends CrudRepository<Url, Long> {

	Url findByUrl(String url);

	Url findByShortcode(String shortcode);

	List<Url> findAll();

}