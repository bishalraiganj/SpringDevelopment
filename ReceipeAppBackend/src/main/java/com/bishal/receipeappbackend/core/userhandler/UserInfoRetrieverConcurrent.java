package com.bishal.receipeappbackend.core.userhandler;

import com.bishal.receipeappbackend.core.dbhandler.DbHandler;
import com.bishal.receipeappbackend.core.model.User;
import com.bishal.receipeappbackend.core.model.UserSession;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class UserInfoRetrieverConcurrent {

	private ExecutorService dbExecutor = Executors.newFixedThreadPool(50);

	public CompletableFuture<UserSession>  getUserInfo(String username, String password)
	{
		CompletableFuture<UserSession> cf = CompletableFuture.supplyAsync(()->{
		return	DbHandler.userSessionQuery(username,password,DbHandler.fetchUserSessionBiFunct).get();
		},dbExecutor);

		return cf;

	}


}
