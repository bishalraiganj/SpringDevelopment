package com.bishal.logmonitorapp.ui;

import com.bishal.logmonitorapp.core.model.LogEntry;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import com.bishal.logmonitorapp.ui.MonitorRestClient;

import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class LogInsightsController implements Initializable {

	@FXML
	private Accordion accordion;

	private final MonitorRestClient restClient = new MonitorRestClient();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<LogEntry> logs = restClient.getAllLogs();
		Map<Path, List<LogEntry>> logsGroupedByFile = logs.stream()
				.collect(Collectors.groupingBy(LogEntry::filePath));

		for (Map.Entry<Path, List<LogEntry>> entry : logsGroupedByFile.entrySet()) {
			Path file = entry.getKey();
			List<LogEntry> fileLogs = entry.getValue();

			Map<String, Long> levelCount = fileLogs.stream()
					.collect(Collectors.groupingBy(LogEntry::logLevel, Collectors.counting()));

			BarChart<String, Number> barChart = createBarChart(levelCount);
			PieChart pieChart = createPieChart(levelCount);

			VBox content = new VBox(15, barChart, pieChart);
			content.setStyle("-fx-padding: 20;");

			TitledPane pane = new TitledPane(file.toString(), content);
			accordion.getPanes().add(pane);
		}
	}

	private BarChart<String, Number> createBarChart(Map<String, Long> levelCount) {
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Log Level");

		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Count");

		BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
		chart.setTitle("Bar Chart");

		XYChart.Series<String, Number> series = new XYChart.Series<>();
		levelCount.forEach((level, count) -> series.getData().add(new XYChart.Data<>(level, count)));

		chart.getData().add(series);
		return chart;
	}

	private PieChart createPieChart(Map<String, Long> levelCount) {
		PieChart chart = new PieChart();
		levelCount.forEach((level, count) ->
				chart.getData().add(new PieChart.Data(level, count))
		);
		chart.setTitle("Pie Chart");
		return chart;
	}
}