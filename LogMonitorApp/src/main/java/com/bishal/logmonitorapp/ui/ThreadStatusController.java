package com.bishal.logmonitorapp.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.bishal.logmonitorapp.ui.model.ThreadStatusModel;

public class ThreadStatusController implements Initializable {

	@FXML
	private TableView<ThreadStatusModel> threadStatusTable;

	@FXML
	private TableColumn<ThreadStatusModel, String> filePathColumn;

	@FXML
	private TableColumn<ThreadStatusModel, String> statusColumn;

	private final MonitorRestClient restClient = new MonitorRestClient();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		filePathColumn.setCellValueFactory(new PropertyValueFactory<>("filePath"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

		Map<String, Boolean> threadStatuses = restClient.getMonitoringStatuses();
		List<ThreadStatusModel> modelList = new ArrayList<>();
		for (Entry<String, Boolean> entry : threadStatuses.entrySet()) {
			modelList.add(new ThreadStatusModel(entry.getKey(), entry.getValue() ? "Running" : "Stopped"));
		}
		ObservableList<ThreadStatusModel> observableList = FXCollections.observableArrayList(modelList);
		threadStatusTable.setItems(observableList);
	}
}