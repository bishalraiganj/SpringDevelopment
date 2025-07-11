package com.bishal.logmonitorapp.core.alert;

import com.bishal.logmonitorapp.core.alert.AlertNotifier;
import com.bishal.logmonitorapp.core.alert.AlertRule;
import com.bishal.logmonitorapp.core.model.LogEntry;
import com.bishal.logmonitorapp.core.model.LogEntry;
import com.bishal.logmonitorapp.core.storage.InMemoryLogStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
@Component
public class AlertEngine {


	private final InMemoryLogStore store;

	//Maps each rule to it's dedicated list of notifiers
	private final ConcurrentHashMap<AlertRule, ConcurrentLinkedQueue<AlertNotifier>> ruleNotifierMap
			= new ConcurrentHashMap<>();


	@Autowired
	public AlertEngine(InMemoryLogStore store)
	{
		this.store = store;
	}

	//Register's rule with a single notifier

	public Map<AlertRule,ConcurrentLinkedQueue<AlertNotifier>> getAlertNotifierMap()
	{
		return new LinkedHashMap<>(ruleNotifierMap);
	}



	public void registerRule(AlertRule rule, AlertNotifier notifier)
	{
		ruleNotifierMap.compute(rule,(existingRule,existingValue)->{
			if(existingValue == null)
			{
				ConcurrentLinkedQueue<AlertNotifier> queue
						= new ConcurrentLinkedQueue<>();
				queue.offer(notifier);
				return queue;
			}

			if(!existingValue.contains(notifier)) {
				existingValue.offer(notifier);
			}

			return existingValue;
		});

	}


	//Register's multiple rules through a List

	public void registerRule(AlertRule rule, List<AlertNotifier> notifiers)
	{
		ruleNotifierMap.compute(rule,(existingRule, existingValue)->{
			if(existingValue == null)
			{
				ConcurrentLinkedQueue<AlertNotifier> queue = new ConcurrentLinkedQueue<>();
				queue.addAll(notifiers);
				return queue;
			}
			existingValue.addAll(notifiers);
			return existingValue;
		});
	}

	//Evaluate all registerd rules and notifies via their mapped/associated notifiers queue in ruleNotifierMap

	public void evaluateAlerts()
	{

		List<LogEntry> logs = store.getAll();

		for(Map.Entry<AlertRule,ConcurrentLinkedQueue<AlertNotifier>> entry : ruleNotifierMap.entrySet())
		{

			AlertRule rule = entry.getKey();
			ConcurrentLinkedQueue<AlertNotifier> notifiers = entry.getValue();
			String level = entry.getKey().getLogLevel();

			Duration duration = entry.getKey().getWindow();

			List<LogEntry> list = store.getRecent(Duration.ofSeconds(duration.getSeconds()))
					.parallelStream()
					.filter((logEntry)->{
					return	logEntry.logLevel().trim().equalsIgnoreCase(level);
					})
					.collect(()->new ArrayList<>(),(ArrayList<LogEntry> l,LogEntry b)->
					{
						l.add(b);
					},(c,d)->{
						c.addAll(d);
					});

			boolean status = rule.evaluate(list);
			System.out.println("Boolean Status: " + status + " List sever size: " + list.size());

			if(status)
			{
				for(AlertNotifier notifier : notifiers)
				{

					notifier.notify(rule,list);
					System.out.println(" \n\n  Notified By : " + notifier.toString() + "\n\n");

				}

			}

		}





	}





}
