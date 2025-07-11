package com.bishal.logmonitorapp.core.alert;

import com.bishal.logmonitorapp.core.model.LogEntry;

import java.time.Duration;
import java.util.List;


/* Represents a generic alerting rule that can be applied to logs */
public interface AlertRule {


	/* Evaluates the rule based on the provided logs */

	boolean evaluate(List<LogEntry> logs);

	/*
	  Gets the level this rule cares about .
	* used for filtering logs before rule evaluation
	* */

	String getLogLevel();

	/* Gets the window for which the logs should be evaluated */

	int getThreshold();


	Duration getWindow();

	/* A human-readable explanation of the rule */

	String getDescription();


	void setThreshold(int threshold);

	void setWindow(Duration window);
}
