package com.bishal.receipeappbackend.core.controller;


import com.bishal.receipeappbackend.core.authenticator.ParallelAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/fetch")
public class Controller {

	private final ParallelAuthenticator authenticator;


	@Autowired
	public Controller(ParallelAuthenticator pa)
	{
		this.authenticator=pa;
	}


	@GetMapping("/authenticate")
	public CompletableFuture<Boolean> authenticateApi(String username,String password)
	{
		CompletableFuture<>


		return authenticator.authenticate().com
	}
}
