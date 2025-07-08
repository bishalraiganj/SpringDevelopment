package com.bishal.logmonitorapp.alert;

import com.bishal.logmonitorapp.model.LogEntry;

import java.util.List;

public interface AlertNotifier {

	void  notify(AlertRule rule, List<LogEntry> matchingLogs);



}
