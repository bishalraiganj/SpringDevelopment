package com.bishal.logmonitorapp.core.monitor;


import com.bishal.logmonitorapp.core.Testing.TemporaryTesting;
import com.bishal.logmonitorapp.core.consumer.LogConsumer;
import com.bishal.logmonitorapp.core.storage.InMemoryLogStore;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class LogMonitorInitializer {

	private final InMemoryLogStore logStore;

	public LogMonitorInitializer(InMemoryLogStore logStore)
	{
		this.logStore = logStore;
	}

	@PostConstruct
	public void init()
	{
		System.out.println("Starting background log monitoring... ");

		Path pathToMonitor = Path.of("BishalAppLogs.txt");


		LogConsumer addToMemoryConsumer = (logEntry)->{
			logStore.add(logEntry);
		};


		//My monitor will run at max for 60 seconds
		int monitorTimeLimitSec = 60;
		//sleep time in the following is set for 1400 seconds
		long monitorPollTimeMilli = 1400;

		TemporaryTesting writerToBishalAppLog = new TemporaryTesting();
		ConcurrentLogMonitor monitor = new ConcurrentLogMonitor(monitorTimeLimitSec,monitorPollTimeMilli,addToMemoryConsumer);

		ExecutorService executor = Executors.newCachedThreadPool();

		Runnable writerRunnable = ()->{
			writerToBishalAppLog.writeToFile(Path.of("BishalAppLogs.txt"), Duration.ofSeconds(120));
		};

		executor.execute(writerRunnable);
		monitor.startMonitoring(Path.of("BishalAppLogs.txt"));



	}


}
