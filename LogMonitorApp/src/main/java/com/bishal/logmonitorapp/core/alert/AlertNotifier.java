package com.bishal.logmonitorapp.core.alert;

import com.bishal.logmonitorapp.core.alert.AlertRule;
import com.bishal.logmonitorapp.core.model.LogEntry;

import java.util.List;

public interface AlertNotifier {

	void  notify(AlertRule rule, List<LogEntry> matchingLogs);



}
