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

	@FXML
	public void openLogViewer() {
		loadFXML("LogViewer.fxml");
	}

	@FXML
	public void openMonitorControl() {
		loadFXML("MonitorControl.fxml");
	}

	@FXML
	public void openAlerts() {
		loadFXML("AlertManager.fxml");
	}

	@FXML
	public void openSyntheticWriter() {
		loadFXML("SyntheticWriter.fxml");
	}

	@FXML
	public void openSystemActions() {
		loadFXML("SystemActions.fxml");
	}

	@FXML
	public void openStats() {
		loadFXML("RealTimeStats.fxml");
	}

	@FXML
	public void openThreadStatus() {
		loadFXML("ThreadStatusView.fxml");
	}

	@FXML
	public void openLogInsights() {
		loadFXML("LogInsights.fxml");
	}

	@FXML
	private void openAboutPage() {
		loadFXML("About.fxml");
	}
}