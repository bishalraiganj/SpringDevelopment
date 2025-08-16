package com.bishal.receipeappbackend.core.authenticator;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelAuthenticator {





}



class AuthenticationTask extends RecursiveTask<Boolean> {


	private final String username;
	private final String password;

	public AuthenticationTask(String username, String password)
	{
		this.username= username;
		this.password=password;
	}

	@Override
	public Boolean compute()
	{

		long startTime = System.currentTimeMillis();

		return Authenticator.authenticate(username,password);

	}
}


class compensator extends ForkJoinPool.ManagedBlocker
{




	@Override
	public boolean isReleasable()
	{



	}



}


