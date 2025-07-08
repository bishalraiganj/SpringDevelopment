package com.bishal.logmonitorapp.Testing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TemporaryTesting {


	// Write now the logic in writeToFile writes every 1100 ms or 1.1 seconds later

	public static void writeToFile(Path path , Duration duration)
	{
		long now = System.nanoTime();

		long count = 0;
		Random random = new Random();

		Source[] sources = Source.values();
		Message[] messages = Message.values();
		Level[] levels = Level.values();


		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");



		try(BufferedWriter bw = new BufferedWriter(new FileWriter(path.toFile(),true))) {



			while(System.nanoTime() - now <= duration.toNanos())
		{


			LocalDateTime nowTime = LocalDateTime.now();
			String logLine = nowTime.format(formatter).toString() + " " + levels[random.nextInt(levels.length)].name() + " " + "["+sources[random.nextInt(sources.length)].name()+"]" + " - " + messages[random.nextInt(messages.length)].name() +"\n";

			bw.write(logLine);
			bw.flush();
			count++;

			Thread.sleep(1100);

		}

		} catch(IOException | InterruptedException e)
		{
			throw new RuntimeException(e);
		}

		System.out.println("Total Written logs to file : " + count + " Ran for : " + ((System.nanoTime() - now) / (1000*1000*1000L )) +" seconds | "+" File name : " + path.getFileName() + " Path : " + path.toAbsolutePath().toString() );


	}



}
