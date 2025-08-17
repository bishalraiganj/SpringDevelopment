package com.bishal.receipeappbackend.core.authenticator;

import com.bishal.receipeappbackend.core.dbhandler.DbHandler;
import com.bishal.receipeappbackend.core.model.RegRequestDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.*;

@Component
public class ParallelRegistrator {

	private ForkJoinPool fjp = new ForkJoinPool(12);
	private ExecutorService dbExecutor = Executors.newFixedThreadPool(200);

	public CompletableFuture<Boolean> registerUser(RegRequestDTO regRequest)
	{
		CompletableFuture<Boolean> cf = CompletableFuture.supplyAsync(()->{
		return	new RegisterTask(regRequest,dbExecutor).compute();
		},fjp);

		return cf;
	}

//	public static void main(String... args)
//	{
//		ParallelRegistrator pr = new ParallelRegistrator();
//
//		RegRequestDTO rr = new RegRequestDTO("Donrt","don@123","Don Legend","don@gmail.com");
//	try {
//		Thread.sleep(5000);
//	}catch(InterruptedException e)
//	{
//		e.printStackTrace();
//	}
//		System.out.println(pr.registerUser(rr));
//
//
//	}
}

class RegisterTask extends RecursiveTask<Boolean> {


	private final  RegRequestDTO regRequest;
	private final ExecutorService dbExecutor;
	public RegisterTask(RegRequestDTO regRequest,ExecutorService dbExecutor)
	{
		this.regRequest = regRequest;
		this.dbExecutor = dbExecutor;
	}

	@Override
	public Boolean compute() {

		long startTime = System.currentTimeMillis();
		String username = regRequest.getUsername().trim();
		String password = regRequest.getPassword().trim();
		String name = regRequest.getName().trim();
		String email = regRequest.getEmail().trim();

		if ( !Authenticator.authenticate(username, password))
		{
			CompletableFuture<Boolean> cf = CompletableFuture.supplyAsync(() -> {
				return DbHandler.registerUser(username,password,name,email,DbHandler.registerUserFunction);
			},dbExecutor);
			try {
				ForkJoinPool.managedBlock(new Compensator(cf, startTime));
			//6 seconds passed or cf(db task) is done only then control comes here,
				if(!cf.isDone()) {
					cf.cancel(true);
					return false;
				}
				return cf.getNow(false);
//				if(cf.get()==)
//				{
//					System.out.println("RegRequest Failed for : username( %s ) password( %s ) ".formatted(username,password));
//					return false;
//				}

			}catch(InterruptedException e)
			{
				e.printStackTrace();
				return false;
			}

		}
		else
		{
			System.out.println("User : %s exists in DB ".formatted(username));
			return false;
		}
	}

}

class Compensator implements ForkJoinPool.ManagedBlocker{

	private final CompletableFuture<Boolean> cf;
	private final long startTime;
	public Compensator(CompletableFuture<Boolean> cf,long startTime)
	{
		this.cf = cf;
		this.startTime = startTime;
	}


	@Override
	public boolean block()
	{
		try {
			Thread.sleep(200); // when control comes here ,
			// it means db task is not done yet ,so make the thread sleep for 100 ms
			// ,this way cpu will not be stressed run this thread and other threads can run that needs to run
			// , since this thread's db task is not done yet and 2sec is not elapsed either
		}catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		return false;

	}

	@Override
	public boolean isReleasable()
	{
		long elapsedTime = System.currentTimeMillis() - startTime;
		return cf.isDone() || elapsedTime > 6000;

	}


}
