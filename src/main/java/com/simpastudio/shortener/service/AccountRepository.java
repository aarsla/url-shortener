package com.simpastudio.shortener.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.simpastudio.shortener.domain.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

	Account findByAccountId(String accountId);
	List<Account> findAll();

	@Query("select sum(visits) from Url u where u.account = :account")
	int getTotalVisits(@Param("account") Account account);
}