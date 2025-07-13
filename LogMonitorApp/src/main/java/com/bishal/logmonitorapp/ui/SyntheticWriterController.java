package com.bishal.logmonitorapp.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SyntheticWriterController {

	@FXML private TextField filePathField;
	@FXML private Label writerStatusLabel;

	private final RestClient restClient = new RestClient();

	public void startSyntheticWriting() {
		String filePath = filePathField.getText();
		String response = restClient.startSyntheticWriting(filePath);
		writerStatusLabel.setText(response);
	}
}
