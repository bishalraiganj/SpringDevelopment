package com.bishal.logmonitorapp.core.controller;

import com.bishal.logmonitorapp.core.model.LogEntry;
import com.bishal.logmonitorapp.core.storage.InMemoryLogStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

	private final InMemoryLogStore logStore;

	@Autowired
	public LogController(InMemoryLogStore logStore)
	{
		this.logStore = logStore;
	}


	@GetMapping
	public List<LogEntry> getAll()
	{
		return logStore.getAll();
	}
//	@GetMapping("/hello")
//	public String hello()
//	{
//		return "LogMonitorApp API is running! :-0 hell yeah ";
//	}

}
