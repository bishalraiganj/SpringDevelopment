package com.bishal.receipeappbackend.core.authenticator;

import java.util.concurrent.*;

public class ParallelAuthenticator {



	ForkJoinPool fjp = new ForkJoinPool(12);
	Executor dbExecutor = Executors.newFixedThreadPool(200);

	public Future<Boolean> authenticate(String username,String password)
	{
		Future<Boolean> authStatus=fjp.submit(new AuthenticationTask(username,password,dbExecutor));
		return authStatus;

	}



}



class AuthenticationTask extends RecursiveTask<Boolean> {


	private final String username;
	private final String password;
	private final Executor dbCommonExecutor;

	public AuthenticationTask(String username, String password,Executor dbCommonExecutor)
	{
		this.username= username;
		this.password=password;
		this.dbCommonExecutor = dbCommonExecutor;
	}

	@Override
	public Boolean compute()
	{

		long startTime = System.currentTimeMillis();

		CompletableFuture<Boolean>  future = CompletableFuture.supplyAsync(()-> Authenticator.authenticate(username,password),dbCommonExecutor);


		try {
			ForkJoinPool.managedBlock(new Compensator(startTime,future));
		}catch(InterruptedException e)
		{
			System.out.println("Compensation logic failed ");
			e.printStackTrace();
		}
		return future.getNow(false);


	}
}


class Compensator implements ForkJoinPool.ManagedBlocker {

	private final long startTime ;
	private final CompletableFuture<Boolean> future;

	private long currentTime = 0;
	public Compensator(long startTime,CompletableFuture<Boolean> future)
	{

		this.startTime = startTime;
		this.future = future;
	}


	@Override
	public boolean block()
	{

		try {
			Thread.sleep(100);
		}catch(InterruptedException e)
		{
			throw new RuntimeException(e);
		}

		return false;
	}


	@Override
	public boolean isReleasable()
	{

		 long elapsedTime = System.currentTimeMillis() - startTime;

			return future.isDone() || elapsedTime > 2000;

	}



}


