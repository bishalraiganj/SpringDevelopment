package com.bishal.logmonitorapp.storage;

import com.bishal.logmonitorapp.model.LogEntry;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class InMemoryLogStore {

	private final ConcurrentLinkedQueue<LogEntry> logEntries
			= new ConcurrentLinkedQueue<>();

	private final ConcurrentHashMap<Path, ConcurrentLinkedQueue<LogEntry>> groupedByPathMap
			= new ConcurrentHashMap<>();

	private final ConcurrentHashMap<String,ConcurrentLinkedQueue<LogEntry>>  groupedByLevelMap
			= new ConcurrentHashMap<>();

	private final AtomicLong totalCount =
			new AtomicLong(0);

	private final AtomicReference<LocalDateTime> lastUpdated
			= new AtomicReference<>(LocalDateTime.now());


	public void add(LogEntry entry)
	{

		Path path = entry.filePath();
		String level = entry.logLevel();
		logEntries.offer(entry);



//		groupedByPathMap.computeIfAbsent(path,(p)->{
//
//			ConcurrentLinkedQueue<LogEntry> queue =  new ConcurrentLinkedQueue<>();
//			queue.offer(entry);
//			return queue;
//		});

		groupedByPathMap.compute(path,(p,v)->{
			if (v == null)
			{
			ConcurrentLinkedQueue<LogEntry> 	queue = new ConcurrentLinkedQueue<>();
			queue.offer(entry);
			return queue;
			}
			v.offer(entry);
			return v;
		});



//			groupedByLevelMap.computeIfAbsent(level, (l) -> {
//
//				ConcurrentLinkedQueue<LogEntry> queue = new ConcurrentLinkedQueue<>();
//				queue.offer(entry);
//				return queue;
//			});


			groupedByLevelMap.compute(level, (l, v) -> {
			if(v == null)
			{
				ConcurrentLinkedQueue<LogEntry> queue = new ConcurrentLinkedQueue<>();
				queue.offer(entry);
				return queue;
			}
				v.offer(entry);
				return v;
			});





		totalCount.getAndIncrement();
		lastUpdated.set(entry.timestamp());
	}

	public List<LogEntry> getAll()
	{
		return new ArrayList<>(logEntries);
	}

	public long totalCount()
	{
		return totalCount.get();
	}

	public HashMap<Path,ConcurrentLinkedQueue<LogEntry>> getPathMap()
	{
		return new HashMap<>(groupedByPathMap);
	}

	public HashMap<String,ConcurrentLinkedQueue<LogEntry>> getLevelMap()
	{
		return new HashMap<>(groupedByLevelMap);
	}

	public List<LogEntry> getByLevel(String level)
	{


		List<LogEntry> filteredList = logEntries.parallelStream()
				.filter((entry)->{
					return entry.logLevel().equalsIgnoreCase(level.trim());
				})
				.collect(()->new ArrayList<>(),(ArrayList<LogEntry> list,LogEntry entry)->list.add(entry),(a,b)->{
					a.addAll(b);
				});

		return filteredList;

	}

	public List<LogEntry> getByFile(Path path)
	{
		List<LogEntry> filteredList = logEntries.parallelStream()
				.filter((entry)->{
					return entry.filePath().equals(path);
				})
				.collect(()->new ArrayList<>(),(ArrayList<LogEntry> list,LogEntry entry)->
						list.add(entry),(a,b)->{
					a.addAll(b);
				});
		return filteredList;

	}

	public List<LogEntry> getRecent(Duration duration)
	{

//		if(duration.toMillis() <= 30 * 1000)
//		{
//			long endTime = System.currentTimeMillis();
//			long startTime = endTime - duration.toMillis();
//
//			List<LogEntry> filteredLogs = logEntries.parallelStream()
//					.filter((entry)->{
//						return entry.timestamp().
//					})
//
//
//
//
//
//		}



		if(logEntries.isEmpty())
		{
			return Collections.emptyList();
		}


		LocalDateTime  endTime = LocalDateTime.now();

		LocalDateTime startTime = endTime.minus(duration);


		System.out.println("Start time: " + startTime + " End time: " + endTime);
		List<LogEntry> filteredList = logEntries.parallelStream()
				.filter((entry)->{
					return !entry.timestamp().isBefore(startTime) && entry.timestamp().isBefore(endTime);
				})
				.collect(()->new ArrayList<>(),(ArrayList<LogEntry> list, LogEntry entry)->list.add(entry),
						(a,b)->{
					a.addAll(b);
						});

		return filteredList;

	}

	public LocalDateTime getLastUpdated()
	{
		return lastUpdated.get();
	}


}
