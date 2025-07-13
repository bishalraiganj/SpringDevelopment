package com.bishal.logmonitorapp.ui;

import com.bishal.logmonitorapp.core.model.LogEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class SystemActionsController {

	@FXML private TextField durationField;
	@FXML private Label memoryStatusLabel;
	@FXML private TableView<LogEntry> recentTable;
	@FXML private TableColumn<LogEntry, String> timeColumn;
	@FXML private TableColumn<LogEntry, String> levelColumn;
	@FXML private TableColumn<LogEntry, String> sourceColumn;
	@FXML private TableColumn<LogEntry, String> messageColumn;
	@FXML private TableColumn<LogEntry, String> fileColumn;

	private final RestClient client = new RestClient();

	@FXML
	public void initialize() {
		timeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().timestamp().toString()));
		levelColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().logLevel()));
		sourceColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().source()));
		messageColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().message()));
		fileColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().filePath().toString()));
	}

	public void clearMemory() {
		String response = client.clearMemory();
		memoryStatusLabel.setText(response);
		recentTable.getItems().clear();
	}

	public void loadRecentLogs() {
		String duration = durationField.getText();
		List<LogEntry> logs = client.getRecent(duration);
		ObservableList<LogEntry> list = FXCollections.observableArrayList(logs);
		recentTable.setItems(list);
	}
}
