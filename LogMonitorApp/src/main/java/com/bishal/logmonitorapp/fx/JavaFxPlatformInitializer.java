package com.bishal.logmonitorapp.fx;

import jakarta.annotation.PostConstruct;
import javafx.application.Application;
import org.springframework.stereotype.Component;

@Component
public class JavaFxPlatformInitializer {


	private static boolean started = false;
	@PostConstruct
	public void initJavaFxPlatform()
	{
		if(!started)
		{
			new Thread(()-> Application.launch(FxApp.class)).start();
			started = true;
		}
	}
}
