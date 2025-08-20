package com.bishal.receipeappbackend.core.controller;


import com.bishal.receipeappbackend.core.authenticator.ParallelAuthenticator;
import com.bishal.receipeappbackend.core.authenticator.ParallelRecipeFetcher;
import com.bishal.receipeappbackend.core.authenticator.ParallelRegistrator;
import com.bishal.receipeappbackend.core.dbhandler.DbHandler;
import com.bishal.receipeappbackend.core.model.*;
import com.bishal.receipeappbackend.core.userhandler.UserInfoRetrieverConcurrent;
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
	private final UserInfoRetrieverConcurrent ur;
	private final ParallelRecipeFetcher prf;

	@Autowired
	public Controller(ParallelAuthenticator pa,ParallelRegistrator pr,ParallelRecipeFetcher prf,UserInfoRetrieverConcurrent ur)
	{

		this.authenticator=pa;
		this.pr=pr;
		this.prf=prf;
		this.ur=ur;
	}



	@PostMapping("/authenticate")
	public CompletableFuture<Boolean> authenticateApi(@RequestBody AuthRequestDTO request)
	{
		return authenticator.authenticate(request.getUsername(),request.getPassword());
	}

	@PostMapping("/getUserSession")
	public CompletableFuture<UserSession> getUserSession(@RequestBody AuthRequestDTO request)
	{

		return ur.getUserInfo(request.getUsername(),request.getPassword());

	}

	@PostMapping("/regUser")
	public CompletableFuture<Boolean> regUser(@RequestBody RegRequestDTO regRequest)
	{
		return  pr.registerUser(regRequest);
	}


	@PostMapping("/fetchRecipe")
	public CompletableFuture<Recipe> fetchRecipe(@RequestBody RecipeRequestDTO recipeRequest)
	{
		return prf.parallelRecipeFetcher(recipeRequest.title());
	}


}
