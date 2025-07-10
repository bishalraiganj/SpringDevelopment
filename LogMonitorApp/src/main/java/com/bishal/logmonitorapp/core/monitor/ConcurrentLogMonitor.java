package com.bishal.logmonitorapp.core.monitor;

import com.bishal.logmonitorapp.core.consumer.LogConsumer;
import com.bishal.logmonitorapp.core.model.LogEntry;
import com.bishal.logmonitorapp.core.parser.LogParser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


class ProperBlockingTask extends RecursiveTask<ConcurrentHashMap<LogEntry, Time>>
{
 //timeLimit is in seconds measure so should be passed seconds units like , 8 for 8 seconds , 9 for 9 seconds
	private final int timeLimit ; //This defines how long the compute() runs or abstractly saying how long the thread will run

	private final ConcurrentHashMap<Path, Long> fileOffsets;

	private final Path path;

	//sleepTime describes how long a thread will sleep before polling again , except at the very first poll
	private final Long sleepTime; // No need for atomicLong since we are not concurrently accessing or modifying
	// (Since, each thread will have their own instance of ProperBlockingTask
	// thus own copy/instance of this field

	private final ConcurrentHashMap<Path, AtomicBoolean> runningFlags; // This is a shared resource passed to each instance of properBlockingTask
	// by the concurrentLogMonitor instance
	// where this resource truly originates ,
	// this object is shared for establishing coordination across threads running concurrently

//	private final ArrayBlockingQueue<Optional<LogEntry>> tempQueue;

//	private final ConcurrentHashMap<LogEntry, Time> tempMap;

	private final LogConsumer consumer;

	private final ConcurrentHashMap<String, LocalDateTime> processedFailedLogsMap = new ConcurrentHashMap<>();

	private final AtomicLong logTotalCount = new AtomicLong(0L);

	ProperBlockingTask(int timeLimit,ConcurrentHashMap<Path,Long> fileOffsets, Path path ,Long sleepTime, ConcurrentHashMap<Path, AtomicBoolean> runningFlags ,LogConsumer consumer )
	{
		this.timeLimit = timeLimit;
		this.fileOffsets = fileOffsets;
		this.path = path;
		this.sleepTime = sleepTime;
		this.runningFlags = runningFlags;
//		this.tempMap = tempMap;
		this.consumer = consumer;
	}

	@Override
	public ConcurrentHashMap<LogEntry,Time> compute()
	{


		long sTime = System.currentTimeMillis();


			if (Files.exists(path)) {
				try (RandomAccessFile raf = new RandomAccessFile(path.toString(), "r")) {


//					if (!runningFlags.containsKey(path)) {
//						runningFlags.put(path, new AtomicBoolean(true));
//					}
					while (runningFlags.get(path).get() )
//						&& System.currentTimeMillis() - sTime <= timeLimit * 1000L) {

					{
						if (!fileOffsets.containsKey(path)) {

							fileOffsets.put(path, raf.length());
						}
						raf.seek(fileOffsets.get(path));

						Optional<String> line = Optional.ofNullable(raf.readLine());

						line.ifPresentOrElse(

								(lineValue) -> {

									Optional<LogEntry> maybeLogEntry = LogParser.parseLine(lineValue, path);

									if (maybeLogEntry.isPresent()) { // Only on successful parsing returned optional instance will have a logEntry value
//									tempMap.put(maybeLogEntry.get(),new Time(System.currentTimeMillis()));


										logTotalCount.incrementAndGet();
										consumer.accept(maybeLogEntry.get());
//									try {
//										fileOffsets.put(path, raf.getFilePointer());
//									} catch (IOException e) {
//										throw new RuntimeException(e);
//									}

									}

									try {
										if(maybeLogEntry.isEmpty()) {
											processedFailedLogsMap.merge(lineValue, LocalDateTime.now(), (ol, n) -> LocalDateTime.now());
											logTotalCount.incrementAndGet();
										}
										fileOffsets.put(path, raf.getFilePointer());
									} catch (IOException e) {
										throw new RuntimeException(e);
									}


								}, () -> {

								}
						);

						ForkJoinPool.managedBlock(new SleepBlocker(sleepTime));


					}

					System.out.println(Thread.currentThread().getName() + " finished execution !  in " + ( System.currentTimeMillis() - sTime )/1000 + " seconds  | Monitored : " + logTotalCount.get()  + " Lines " );

				} catch (IOException | InterruptedException e) {
					throw new RuntimeException(e);
				}
			}


		return null;

	}





	private static class SleepBlocker implements ForkJoinPool.ManagedBlocker {
		private final long sleepTime;

		private final long startTime;

		/* Since ,
	we need to make the thread wait at least few seconds for modification to the
	log file before polling we will check how much time has elapsed from the
	starting of execution of this task , if enough time elapsed then no need to block
	which is why we have this startTime field to calculate the elapsed Time
	*/

		SleepBlocker(long sleepTime)
		{
			this.sleepTime = sleepTime;
			this.startTime = System.currentTimeMillis();
		}

		@Override
		public boolean block() throws InterruptedException
		{
			Thread.sleep(sleepTime);
			return true;
		}

		@Override
		public boolean isReleasable()
		{
			return System.currentTimeMillis() - startTime >= sleepTime; // If after invoking forkJoinPool.manageBlocked() in compute elapsed time is greater or equal to sleep time
			// then no need to invoke the block() here since (logic : since 3 seconds already passed so no need to sleep through block() before polling
			// isReleasable() is like a condition before invoking block() if isReleasable() returns false that means block() is invoked and forkJoinPool tries to compensate
			// by spawning (creating ) a new thread
			// isReleasable() tells the forkJoinPool whether the block() is required or not , if isReleasable() returns true that means ignore invoking block() and compensation  and release
			// the flow of execution in compute after the blocking logic ( specifically the ForkJoinPool.manageBlocked(manageBlocker instance) )
			// and if the isReleasable() returns false , then compensation is tried by forkJoinPool and then block() logic is executed if execution completes block() also returns true
			// indicating forkJoinpool to continue execution in compute after block logic else if block() failed or returned false forkJoinPool keeps trying block() logic execution until true is returned or handled otherwise

		}
	}
}





public class ConcurrentLogMonitor {


	private final int timeLimit;


	private final ConcurrentHashMap<Path,Long> fileOffsets = new ConcurrentHashMap<>();


	private final ConcurrentHashMap<String ,AtomicBoolean  > runningThreadsStatus = new ConcurrentHashMap<>();

	private final ConcurrentHashMap<Path, AtomicBoolean> runningFlags = new ConcurrentHashMap<>();

//	private final ConcurrentHashMap<LogEntry,Time> tempMap = new ConcurrentHashMap<>();

	private final long sleepTime ;

	private final LogConsumer consumer;



	public ConcurrentLogMonitor(int timeLimit, long sleepTime, LogConsumer consumer)
	{
		this.timeLimit = timeLimit;
		this.sleepTime = sleepTime;
		this.consumer = consumer;
	}




	ForkJoinPool executor = new ForkJoinPool(12);


	public void startMonitoring(Path path)
	{

		runningFlags.putIfAbsent(path,new AtomicBoolean(true));
		if(!Files.exists(path) || !Files.isRegularFile(path))
		{
//			try {
//				Files.createFile(path);
//			}catch(IOException e)
//			{
//				throw new RuntimeException(e);
//			}

			throw new RuntimeException(" Cannot monitor nonexistent or invalid log file: " + path);
		}


		executor.submit(new ProperBlockingTask(timeLimit,fileOffsets,path,sleepTime,runningFlags,consumer));





	}

	public void stopMonitoring(Path path)
	{
		AtomicBoolean runningFlag = runningFlags.get(path);
		if(runningFlag == null)
		{
			System.out.println("No running flag found for path: " + path);
		}
		else {

			runningFlags.get(path).set(false);

		}

	}




}
