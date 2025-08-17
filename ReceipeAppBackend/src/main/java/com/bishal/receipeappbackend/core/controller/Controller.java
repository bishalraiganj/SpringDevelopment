package com.bishal.receipeappbackend.core.controller;


import com.bishal.receipeappbackend.core.authenticator.ParallelAuthenticator;
import com.bishal.receipeappbackend.core.authenticator.ParallelRegistrator;
import com.bishal.receipeappbackend.core.dbhandler.DbHandler;
import com.bishal.receipeappbackend.core.model.AuthRequestDTO;
import com.bishal.receipeappbackend.core.model.RegRequestDTO;
import com.bishal.receipeappbackend.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api/fetch")
public class Controller {

	private final ParallelAuthenticator authenticator;
	private final ParallelRegistrator pr;


	@Autowired
	public Controller(ParallelAuthenticator pa,ParallelRegistrator pr)
	{
		this.authenticator=pa;
		this.pr=pr;
	}


	@PostMapping("/authenticate")
	public CompletableFuture<Boolean> authenticateApi(@RequestBody AuthRequestDTO request)
	{
		return authenticator.authenticate(request.getUsername(),request.getPassword());
	}

	@PostMapping("/getUserInfo")
	public Optional<User> getUserInfo(@RequestBody AuthRequestDTO request)
	{

		return DbHandler.runQuery(request.getUsername(),request.getPassword(),DbHandler.fetchUserFunction);

	}

	@PostMapping("/regUser")
	public CompletableFuture<Boolean> regUser(@RequestBody RegRequestDTO regRequest)
	{
		return  pr.registerUser(regRequest);
	}
}
