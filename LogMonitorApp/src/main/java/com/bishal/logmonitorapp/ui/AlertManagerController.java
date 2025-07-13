package com.bishal.logmonitorapp.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AlertManagerController {

	@FXML private TextField thresholdField;
	@FXML private TextField durationField;
	@FXML private Label alertStatusLabel;

	private final RestClient client = new RestClient();

	public void evaluateAlerts() {
		String threshold = thresholdField.getText();
		String duration = durationField.getText();

		String response = client.evaluateAlerts(threshold, duration);
		alertStatusLabel.setText(response);
	}
}
