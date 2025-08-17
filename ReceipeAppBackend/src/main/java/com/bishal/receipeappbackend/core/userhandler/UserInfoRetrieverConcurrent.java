package com.bishal.receipeappbackend.core.userhandler;

import com.bishal.receipeappbackend.core.dbhandler.DbHandler;
import com.bishal.receipeappbackend.core.model.User;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class UserInfoRetrieverConcurrent {

	private ExecutorService dbExecutor = Executors.newFixedThreadPool(50);

	public CompletableFuture<User>  getUserInfo(String username,String password)
	{
		CompletableFuture<User> cf = CompletableFuture.supplyAsync(()->{
		return	DbHandler.runQuery(username,password,DbHandler.fetchUserFunction).get();
		},dbExecutor);

		return cf;

	}


}
