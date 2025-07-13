package com.bishal.logmonitorapp.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainDashboardController {

	@FXML
	private StackPane contentPane;

	private void loadFXML(String fxmlFile) {
		try {
			Parent view = FXMLLoader.load(getClass().getResource("/fxml/" + fxmlFile));
			contentPane.getChildren().setAll(view);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void openLogViewer() {
		loadFXML("LogViewer.fxml");
	}

	public void openMonitorControl() {
		loadFXML("MonitorControl.fxml");
	}

	public void openAlerts() {
		loadFXML("AlertManager.fxml");
	}

	public void openSyntheticWriter() {
		loadFXML("SyntheticWriter.fxml");
	}

	public void openSystemActions() {
		loadFXML("SystemActions.fxml");
	}

	public void openStats() {
		loadFXML("RealTimeStats.fxml");
	}
}