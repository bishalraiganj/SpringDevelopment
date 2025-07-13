package com.bishal.logmonitorapp.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Map;

public class MonitorControlController {

	@FXML private TextField pathField;
	@FXML private Label statusLabel;
	@FXML private TableView<Map.Entry<String, Boolean>> monitorStatusTable;
	@FXML private TableColumn<Map.Entry<String, Boolean>, String> filePathColumn;
	@FXML private TableColumn<Map.Entry<String, Boolean>, String> statusColumn;

	private final MonitorRestClient client = new MonitorRestClient();

	@FXML
	public void initialize() {
		filePathColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKey()));
		statusColumn.setCellValueFactory(cellData -> {
			boolean status = cellData.getValue().getValue();
			return new javafx.beans.property.SimpleStringProperty(status ? "🟢 Running" : "🔴 Stopped");
		});

		loadMonitorStatuses();
	}

	public void startMonitoring() {
		String response = client.startMonitoring(pathField.getText());
		statusLabel.setText(response);
		loadMonitorStatuses();
	}

	public void stopMonitoring() {
		String response = client.stopMonitoring(pathField.getText());
		statusLabel.setText(response);
		loadMonitorStatuses();
	}

	private void loadMonitorStatuses() {
		Map<String, Boolean> map = client.getMonitoringStatuses();
		ObservableList<Map.Entry<String, Boolean>> list = FXCollections.observableArrayList(map.entrySet());
		monitorStatusTable.setItems(list);
	}
}