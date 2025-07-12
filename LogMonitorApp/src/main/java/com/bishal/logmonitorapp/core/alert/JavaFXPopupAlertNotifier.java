package com.bishal.logmonitorapp.core.alert;

import com.bishal.logmonitorapp.core.model.LogEntry;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.time.LocalDateTime;
import java.util.List;

public class JavaFXPopupAlertNotifier implements AlertNotifier {


	@Override
	public void notify(AlertRule rule, List<LogEntry> logEntries)
	{

		System.out.println("[Before runLater] Is FX Thread? " + Platform.isFxApplicationThread());
		Platform.runLater(()->{
			System.out.println("[after runLater] Is FX Thread? " + Platform.isFxApplicationThread());
			try {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Alert ! " + rule.toString());
				alert.setHeaderText(rule.getWindow() + "");
				alert.setContentText("SEVERE logs: " + logEntries.size());
				alert.show();
				System.out.println("JavaFxNotifier shown : " + " with rule " + rule.toString() + LocalDateTime.now());

			}catch(Exception e)
			{
				e.printStackTrace();
			}
		});
	}

	@Override
	public boolean equals(Object o)
	{

//		return o instanceof JavaFXPopupAlertNotifier;
		if( this.getClass().getSimpleName().equals(o.getClass().getSimpleName()) )
		{
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

	@Override
	public String toString()
	{
		return "JavaFXPopupAlertNotifier";
	}
}
