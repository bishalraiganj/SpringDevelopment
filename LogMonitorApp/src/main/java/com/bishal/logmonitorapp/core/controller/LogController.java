package com.bishal.logmonitorapp.core.controller;

import com.bishal.logmonitorapp.core.Testing.TemporaryTesting;
import com.bishal.logmonitorapp.core.model.LogEntry;
import com.bishal.logmonitorapp.core.monitor.ConcurrentLogMonitor;
import com.bishal.logmonitorapp.core.monitor.LogMonitorInitializer;
import com.bishal.logmonitorapp.core.storage.InMemoryLogStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/logs")
public class LogController {

	private final InMemoryLogStore logStore;

	private final LogMonitorInitializer logMonitorInitializer;

	private final ExecutorService cachedThreadPool;

	@Autowired
	public LogController(InMemoryLogStore logStore,LogMonitorInitializer logMonitorInitializer)
	{
		this.logStore = logStore;
		this.logMonitorInitializer = logMonitorInitializer;
		cachedThreadPool = Executors.newCachedThreadPool();
	}


	@GetMapping("/getAll")
	public List<LogEntry> getAll()
	{
		return logStore.getAll();
	}

	@GetMapping("/startTestWritingToFile")
	public String startTestWritingToFile(String path)
	{
		Path filePath = Path.of(path);

		TemporaryTesting temporaryTestingLogWriter = logMonitorInitializer.getTemporaryTestingLogWriter();


		// Currently time out is set to 360 seconds for writing test logs
		Runnable syntheticLogWriterRunnable = ()->{
			temporaryTestingLogWriter.writeToFile(filePath, Duration.ofSeconds(360));
		};

		cachedThreadPool.execute(syntheticLogWriterRunnable);


		System.out.println("Started Synthetic logs writing to : Path: " + "( " + filePath.toAbsolutePath() + ") " + LocalDateTime.now());

		return "Started writing Synthetic Logs to Path: " + "(" +  filePath.toAbsolutePath() + ")";


	}

	@GetMapping("/startMonitoring")
	public String startMonitoring(String path)
	{
		Path filePath = Path.of(path);

		ConcurrentLogMonitor monitor = logMonitorInitializer.getMonitor();
		monitor.startMonitoring(filePath);

		System.out.println("Started Monitoring : Path:  " + "(" + filePath.toAbsolutePath() + ")" + LocalDateTime.now());

		return "StartedMonitoring : " + filePath.toAbsolutePath();

	}


	@GetMapping("/stopMonitoring")
	public String stopMonitoring(@RequestParam String path)
	{
		Path monitorFilePath = Path.of(path);
		logMonitorInitializer.getMonitor().stopMonitoring(monitorFilePath);

		System.out.println("Stopped Monitoring : Path: " + "(" + monitorFilePath.toAbsolutePath() + ")" + LocalDateTime.now());

		return "Monitoring Stopped for path : " + monitorFilePath.toAbsolutePath() + LocalDateTime.now();
	}


	@GetMapping("/clearMemory")
	public String clearMemory()
	{
		logStore.clearMemory();

		return "Memory cleared ! " + LocalDateTime.now();
	}



	@GetMapping("/monitoringThreadsStatus")
	public Map<String,Boolean> getMonitoringThreadStatuses()
	{
		ConcurrentLogMonitor monitor = logMonitorInitializer.getMonitor();

		Map<String,Boolean> statuses = monitor.getRunningThreadsStatuses();
		System.out.println("MonitoringThreadsStatus Fetched : Status : " + statuses + "\n\n\n");
		return statuses;

	}
//	@GetMapping("/hello")
//	public String hello()
//	{
//		return "LogMonitorApp API is running! :-0 hell yeah ";
//	}

}
