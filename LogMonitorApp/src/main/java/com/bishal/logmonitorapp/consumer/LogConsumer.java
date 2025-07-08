package com.bishal.logmonitorapp.consumer;

import com.bishal.logmonitorapp.model.LogEntry;


public interface LogConsumer {


	void accept(LogEntry logEntry);


	default LogConsumer andThen(LogConsumer after)
	{
		if(after == null) throw  new RuntimeException("Next Consumer is null !");

		return (entry)->{
			this.accept(entry);
			after.accept(entry);
		};
	}

}