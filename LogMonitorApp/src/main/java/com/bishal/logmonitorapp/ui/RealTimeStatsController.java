package com.bishal.logmonitorapp.ui;

import com.bishal.logmonitorapp.core.model.LogEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class RealTimeStatsController {

	@FXML private TextField pathField;
	@FXML private TextField levelField;
	@FXML private TextField startField;
	@FXML private TextField endField;
	@FXML private TableView<LogEntry> resultTable;
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

	public void filterByPath() {
		String path = pathField.getText();
		List<LogEntry> logs = client.filterByPath(path);
		updateTable(logs);
	}

	public void filterByLevel() {
		String level = levelField.getText();
		List<LogEntry> logs = client.filterByLevel(level);
		updateTable(logs);
	}

	public void filterByTimeRange() {
		String start = startField.getText();
		String end = endField.getText();
		List<LogEntry> logs = client.filterByTimePeriod(start, end);
		updateTable(logs);
	}

	private void updateTable(List<LogEntry> logs) {
		ObservableList<LogEntry> list = FXCollections.observableArrayList(logs);
		resultTable.setItems(list);
	}
}
