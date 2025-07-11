package com.bishal.logmonitorapp.fx;

import javafx.application.Application;
import javafx.stage.Stage;

public class FxApp extends Application {

	@Override
	public void start(Stage stage)
	{

		stage.setOpacity(0);
		stage.setIconified(true); // minimize
		stage.setWidth(1);
		stage.setHeight(1);
		stage.setX(-1000);
		stage.setY(-1000); // move off-screen
		stage.show();      // THIS is the crucial line
	}
}
