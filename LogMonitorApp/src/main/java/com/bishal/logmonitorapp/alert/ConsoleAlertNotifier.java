package com.bishal.logmonitorapp.alert;

import com.bishal.logmonitorapp.model.LogEntry;

import java.util.List;

public class ConsoleAlertNotifier implements AlertNotifier {


	@Override
	public void notify(AlertRule rule, List<LogEntry> matchingLogs)
	{
		System.out.println("!".repeat(100));
		System.out.println("\n\n Console Alert Triggered ! ");
		System.out.println("Rule: " + rule);
		System.out.println("Log count: " + matchingLogs.size());
		System.out.println("Window: " + rule.getWindow().toSeconds() + " seconds");
		System.out.println("\n\n Matching Log Entries : ");
		matchingLogs.forEach(logEntry->{
			System.out.println(logEntry);
		});
		System.out.println("\n\n"+"!".repeat(100)+"\n\n");

	}
}
