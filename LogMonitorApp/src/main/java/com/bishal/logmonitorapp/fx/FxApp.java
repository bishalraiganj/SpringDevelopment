package com.bishal.logmonitorapp.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class FxApp extends Application {

//	@Override
//	public void start(Stage stage)
//	{
//
//		stage.setOpacity(0);
//		stage.setIconified(true); // minimize
//		stage.setWidth(1);
//		stage.setHeight(1);
//		stage.setX(-1000);
//		stage.setY(-1000); // move off-screen
//		stage.show();      // THIS is the crucial line
//	}


	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainDashboard.fxml"));
			Scene scene = new Scene(root, 1000, 700);
			primaryStage.setTitle("🧠 LogMonitor Dashboard");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
