package com.bishal.receipeappbackend.core.authenticator;

import com.bishal.receipeappbackend.core.model.RegRequestDTO;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ParallelRegistrator {

	private ForkJoinPool fjp = new ForkJoinPool(12);
	private ExecutorService dbExecutor = Executors.newFixedThreadPool(200);

	public Optional<Boolean> registerUser(RegRequestDTO regRequestDTO)
	{
		return Optional.empty();
	}

}
