package com.bishal.receipeappbackend.core.authenticator;

import com.bishal.receipeappbackend.core.dbhandler.DbHandler;
import com.bishal.receipeappbackend.core.model.Recipe;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.*;

@Component
public class ParallelRecipeFetcher {

	private final ForkJoinPool fjp= new ForkJoinPool(12);
	private final ExecutorService dbExecutor= Executors.newFixedThreadPool(200);



	public ForkJoinPool getFjp()
	{
		return fjp;
	}
	public ExecutorService getDbExecutor()
	{
		return dbExecutor;
	}

	public CompletableFuture<Recipe> parallelRecipeFetcher(String title)
	{
		if(title!=null) {
			CompletableFuture<Recipe> cf = CompletableFuture.supplyAsync(() -> {

				return new FetchTask(title.trim(),dbExecutor).compute().get();
			},fjp);
			return cf;
		}
		else{
			System.out.println("Title is null : parallelRecipeFetcher() failed in ParallelRecipeFetcher" );
			return CompletableFuture.completedFuture(null);
		}


	}


}


class FetchTask extends RecursiveTask<Optional<Recipe>> {

	private final ExecutorService dbExecutor;
	private final String title;
	private final long startTime = System.currentTimeMillis();

	public FetchTask(String title,ExecutorService dbExecutor)
	{
		this.title = title;
		this.dbExecutor = dbExecutor;
	}


	@Override
	public Optional<Recipe> compute()
	{

		CompletableFuture<Optional<Recipe>> cf = CompletableFuture.supplyAsync(()->{

			return DbHandler.fetchRecipe(title,DbHandler.fetchRecipeBiFunct);


		},dbExecutor);


		try {
			ForkJoinPool.managedBlock(new Compensation(startTime, cf));
		}catch(InterruptedException e)
		{
			System.out.println("Compensation logic failed in FetchTask " + LocalDateTime.now());
			e.printStackTrace();
		}
		//6s passed or cf is done
		if(!cf.isDone())
		{
			cf.cancel(false);
		}
		return cf.getNow(Optional.empty());

	}

}


class Compensation implements ForkJoinPool.ManagedBlocker{


	private final long startTime ;
	private final CompletableFuture<Optional<Recipe>> cf;

	public Compensation(long startTime,CompletableFuture<Optional<Recipe>> cf)
	{
		this.startTime = startTime;
		this.cf = cf;
	}

	@Override
	public boolean block()
	{

		try{
			Thread.sleep(4);
		}catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		return false;
	}




	@Override
	public boolean isReleasable()
	{
		long elapsedTime = System.currentTimeMillis() - startTime ;

		return cf.isDone() || elapsedTime > 6000;
	}




}
