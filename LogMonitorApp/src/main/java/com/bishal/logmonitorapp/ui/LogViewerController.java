package com.bishal.logmonitorapp.ui;

import com.bishal.logmonitorapp.core.model.LogEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.nio.file.Path;
import java.util.List;

public class LogViewerController {

	@FXML private TableView<LogEntry> logTable;
	@FXML private TextField pathField;
	@FXML private TextField levelField;
	@FXML private TextField startTimeField;
	@FXML private TextField endTimeField;
	@FXML private TextField recentDurationField;
	@FXML private Label statusLabel;

	private final RestClient restClient = new RestClient();

	@FXML
	public void initialize() {
		TableColumn<LogEntry, String> timestampCol = new TableColumn<>("Timestamp");
		timestampCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().timestamp().toString()));

		TableColumn<LogEntry, String> levelCol = new TableColumn<>("Level");
		levelCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().logLevel()));

		TableColumn<LogEntry, String> sourceCol = new TableColumn<>("Source");
		sourceCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().source()));

		TableColumn<LogEntry, String> messageCol = new TableColumn<>("Message");
		messageCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().message()));

		TableColumn<LogEntry, String> filePathCol = new TableColumn<>("File Path");
		filePathCol.setPrefWidth(1000);
		filePathCol.setCellValueFactory(cell -> {
			Path path = cell.getValue().filePath();
			return new SimpleStringProperty(path != null ? path.toString() : "N/A");
		});

		// Tooltip support for long paths
		filePathCol.setCellFactory(col -> new TableCell<>() {
			private final Tooltip tooltip = new Tooltip();
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setTooltip(null);
				} else {
					setText(item);
					tooltip.setText(item);
					setTooltip(tooltip);
				}
			}
		});

		// Clear existing and add all consistently defined columns
		logTable.getColumns().clear();
		logTable.getColumns().addAll(timestampCol, levelCol, sourceCol, messageCol, filePathCol);
	}

	public void loadAllLogs() {
		List<LogEntry> logs = restClient.getAllLogs();
		updateTable(logs);
	}

	public void filterByPath() {
		List<LogEntry> logs = restClient.filterByPath(pathField.getText());
		updateTable(logs);
	}

	public void filterByLevel() {
		List<LogEntry> logs = restClient.filterByLevel(levelField.getText());
		updateTable(logs);
	}

	public void filterByTimePeriod() {
		List<LogEntry> logs = restClient.filterByTime(startTimeField.getText(), endTimeField.getText());
		updateTable(logs);
	}

	public void filterByRecent() {
		List<LogEntry> logs = restClient.getRecent(recentDurationField.getText());
		updateTable(logs);
	}

	private void updateTable(List<LogEntry> logs) {
		if (logs != null) {
			ObservableList<LogEntry> data = FXCollections.observableArrayList(logs);
			logTable.setItems(data);
			statusLabel.setText("Found " + logs.size() + " logs");
		} else {
			statusLabel.setText("Error loading logs");
		}
	}
}