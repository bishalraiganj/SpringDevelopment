package com.bishal.logmonitorapp.parser;

import com.bishal.logmonitorapp.model.LogEntry;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

	// The pattern matches or finds in Log's of formats "YYYY-MM-DD HH:MM:SS LEVEL [Adhikary.X.Testing.Source] - Adhikary.X.Testing.Message" UNTESTED , (Padded white space characters between parts is supported now)
	private static final Pattern  pattern = Pattern.compile("(\s*([1-9][0-9]{3})-([01][0-9])-([0123][0-9]))\s+(([012][0-9]):([0-6][0-9]):([0-6][0-9]))\s+([A-Za-z]{3,40})\s+(\\[[a-zA-Z0-9]{3,300}\\])\s+-\s+(([a-zA-Z0-9]{1,40}){1}(\s+[a-zA-Z0-9]{1,40})*)");

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");




	//  This method parses the matched or found String  data's each component then uses them to create the LogEntry records and returns them wrapped around Optional's

	public static Optional<LogEntry> parseLine(String rawLine, Path filePath)
	{
		Matcher matcher = pattern.matcher(rawLine);

		if(matcher.matches())
		{
			int year  = Integer.parseInt(matcher.group(2));
			int month = Integer.parseInt(matcher.group(3));

			int day = Integer.parseInt(matcher.group(4));

			int hour = Integer.parseInt(matcher.group(6));

			int minute = Integer.parseInt(matcher.group(7));

			int second = Integer.parseInt(matcher.group(8));

			if( day > 31 || month > 12 || hour > 24 || minute > 59 || second > 59 )
			{
				return Optional.empty();
			}

			LocalDateTime dateTime = LocalDateTime.of(
					year,
					month,
					day,
					hour,
					minute,
					second
			);

			String logLevel = matcher.group(9);

			String logSource = matcher.group(10);

			String logMessage = matcher.group(11);


			LogEntry logEntry = new LogEntry(dateTime,logLevel,logSource,logMessage,filePath);

			return Optional.of(logEntry);



		}

		matcher.reset(rawLine);

		 if(matcher.find())
		{
			int year  = Integer.parseInt(matcher.group(2));
			int month = Integer.parseInt(matcher.group(3));

			int day = Integer.parseInt(matcher.group(4));

			int hour = Integer.parseInt(matcher.group(6));

			int minute = Integer.parseInt(matcher.group(7));

			int second = Integer.parseInt(matcher.group(8));

			if( day > 31 || month > 12 || hour > 24 || minute > 59 || second > 59 )
			{
				return Optional.empty();
			}

			LocalDateTime dateTime = LocalDateTime.of(
					year,
					month,
					day,
					hour,
					minute,
					second
			);

			String logLevel = matcher.group(9);

			String logSource = matcher.group(10);

			String logMessage = matcher.group(11);


			LogEntry logEntry = new LogEntry(dateTime,logLevel,logSource,logMessage,filePath);

			return Optional.of(logEntry);


		}


		return Optional.empty();


	}

}
