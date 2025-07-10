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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public String startTestWritingToFile(@RequestParam String path)
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
	public String startMonitoring(@RequestParam String path)
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


	@GetMapping("/filterByPath")
	public List<LogEntry> filterByPath(@RequestParam String path)
	{
		Path filePath = Path.of(path);
		return logStore.filterByPath(filePath);
	}

	@GetMapping("/filterByLevel")
	public List<LogEntry> filterByLevel(@RequestParam String level)
	{
		return logStore.getByLevel(level);
	}

	@GetMapping("/filterByTimePeriod")
	public List<LogEntry> filterByTimePeriod(@RequestParam String startDateTime,@RequestParam String endDateTime)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		InMemoryLogStore memoryLogStore = this.logStore;
		try {
			LocalDateTime start = LocalDateTime.parse(startDateTime);
			LocalDateTime end = LocalDateTime.parse(endDateTime);

			List<LogEntry> finalList = memoryLogStore.filterByTimePeriod(start,end);

			System.out.println("Parsing Successful : " + "Filtration requested : ("+ start +" To " + end + ")" + " Found  Logs: " + finalList.size() + LocalDateTime.now() );
			return finalList;

		}catch(DateTimeParseException e)
		{

			LocalDateTime start ;
			LocalDateTime end ;
			// Numeric Back References
			// 1 - Year
			//2 - Month
			//5 - Day
			//9 - Hour
			//12 - Minutes
			//15 - Seconds
			Pattern  pattern =  Pattern.compile("\\s*([1-9][0-9]{3})-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2][0-9])|(3[0-1]))\\s*T{0,1}\\s*(([0-1][0-9])|(2[0-3])):((0[0-9])|([1-5][0-9])):((0[0-9])|([1-5][0-9]))\\s*");

			Matcher matcherStartDate = pattern.matcher(startDateTime);

			Matcher matcherEndDate = pattern.matcher(endDateTime);

			if(matcherStartDate.find() && matcherEndDate.find())
			{

				int startYear = Integer.parseInt(matcherStartDate.group(1));
				int endYear = Integer.parseInt(matcherEndDate.group(1));
				int startMonth = Integer.parseInt(matcherStartDate.group(2));
				int endMonth = Integer.parseInt(matcherEndDate.group(2));
				int startDay = Integer.parseInt(matcherStartDate.group(5));
				int endDay = Integer.parseInt(matcherEndDate.group(5));
				int startHour = Integer.parseInt(matcherStartDate.group(9));
				int endHour = Integer.parseInt(matcherEndDate.group(9));
				int startMinute = Integer.parseInt(matcherStartDate.group(12));
				int endMinute = Integer.parseInt(matcherEndDate.group(12));
				int startSecond = Integer.parseInt(matcherStartDate.group(15));
				int endSecond = Integer.parseInt(matcherEndDate.group(15));


				start = LocalDateTime.of(startYear,startMonth,startDay,startHour,startMinute,startSecond);
				end = LocalDateTime.of(endYear,endMonth,endDay,endHour,endMinute,endSecond);


				List<LogEntry> finalList = memoryLogStore.filterByTimePeriod(start,end);

				if(finalList != null )
				{
					System.out.println("Filtration requested : ("+ start +" To " + end + ")" + " Found  Logs: " + finalList.size() + LocalDateTime.now());
				}
				return finalList;


			}


		}


		System.out.println("Wrong DateTime Format :-!  , null returned instead of List object ! " + LocalDateTime.now());

		return null;

	}





	@GetMapping("/getFileMonitoringStatus")
	public boolean getFileMonitoringStatus(@RequestParam String path)
	{
		Path filePath = Path.of(path);
		ConcurrentLogMonitor monitor = logMonitorInitializer.getMonitor();

		return monitor.getFileMonitoringStatus(filePath);
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
