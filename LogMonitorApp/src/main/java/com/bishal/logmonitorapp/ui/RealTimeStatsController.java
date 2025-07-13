package com.bishal.logmonitorapp.ui;

import com.bishal.logmonitorapp.core.model.LogEntry;
import javafx.beans.property.SimpleStringProperty;
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
		timeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().timestamp().toString()));
		levelColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().logLevel()));
		sourceColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().source()));
		messageColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().message()));

		// Display file path with tooltip
		fileColumn.setCellValueFactory(cell -> {
			String path = cell.getValue().filePath() != null ? cell.getValue().filePath().toString() : "N/A";
			return new SimpleStringProperty(path);
		});

		fileColumn.setCellFactory(col -> new TableCell<>() {
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