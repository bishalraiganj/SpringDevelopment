package com.bishal.logmonitorapp.core.alert;

import com.bishal.logmonitorapp.core.alert.AlertRule;
import com.bishal.logmonitorapp.core.model.LogEntry;

import java.time.Duration;
import java.util.List;

public class SpikeInSevereRule implements AlertRule {


	private int threshold;

	private  Duration window;



	public SpikeInSevereRule(int threshold , Duration window)
	{
		this.threshold = threshold;
		this.window = window;
	}



	@Override
	public boolean evaluate(List<LogEntry> logs)
	{

		long count = logs.parallelStream()
				.filter((entry)->{

					return entry.logLevel().trim().equalsIgnoreCase("SEVERE");


				})
				.count();
		System.out.println(" \n Count : " +count);
		return count >= threshold;


	}

	@Override
	public int getThreshold()
	{
		return threshold;
	}

	@Override
	public void setThreshold(int threshold)
	{
		this.threshold = threshold;
	}

	@Override
	public void setWindow(Duration window)
	{
		this.window = window;
	}
	@Override
	public String getLogLevel()
	{

		return "SEVERE";
	}

	@Override
	public Duration getWindow()
	{
		return window;
	}


	@Override
	public String getDescription()
	{
		return "SpikeInSevereRule : If the number of SEVERE Logs cross a certain threshold set by the rule during a window set by the rule also , " +
				"Say 10 severe logs monitored in the last 5 seconds :-) ";
	}

	@Override
	public String toString()
	{
		return "\n SpikeInSevereRule [threshold="+threshold + "| Window= " + window + "] \n";
	}


	@Override
	public boolean equals(Object o)
	{
		if (this.getClass().getSimpleName().equals(o.getClass().getSimpleName())) {

			SpikeInSevereRule other = (SpikeInSevereRule) o;
			if(this.threshold != other.getThreshold())
			{
				other.threshold = this.getThreshold();
				System.out.println("Threshold value changed from  " + threshold +  " to " + other.getThreshold() + "Due to existing rule , threshold modified in existing rule");
			}
			if(this.window != other.getWindow())
			{
				other.window = this.getWindow();
				System.out.println("Window value changed from " + window + " to " + other.getWindow() + "Due to existing rule,  window modified in existing rule");
			}
			return true;
		}
		else
			return false;
	}


	@Override
	public int hashCode()
	{
		return this.getClass().getSimpleName().hashCode();
	}


}
