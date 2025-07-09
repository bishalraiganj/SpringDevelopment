package com.bishal.logmonitorapp.core.controller;

import com.bishal.logmonitorapp.core.model.LogEntry;
import com.bishal.logmonitorapp.core.monitor.LogMonitorInitializer;
import com.bishal.logmonitorapp.core.storage.InMemoryLogStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

	private final InMemoryLogStore logStore;

	private final LogMonitorInitializer logMonitorInitializer;

	@Autowired
	public LogController(InMemoryLogStore logStore,LogMonitorInitializer logMonitorInitializer)
	{
		this.logStore = logStore;
		this.logMonitorInitializer = logMonitorInitializer;
	}


	@GetMapping("/getAll")
	public List<LogEntry> getAll()
	{
		return logStore.getAll();
	}

	@GetMapping("/stopMonitoring")
	public String stopMonitoring(@RequestParam String path)
	{
		Path monitorFilePath = Path.of(path);
		logMonitorInitializer.getMonitor().stopMonitoring(monitorFilePath);

		return "Monitoring Stopped for path : " + monitorFilePath.toAbsolutePath() + LocalDateTime.now();
	}


	@GetMapping("/clearMemory")
	public String clearMemory()
	{
		logStore.clearMemory();

		return "Memory cleared ! " + LocalDateTime.now();
	}





//	@GetMapping("/hello")
//	public String hello()
//	{
//		return "LogMonitorApp API is running! :-0 hell yeah ";
//	}

}
