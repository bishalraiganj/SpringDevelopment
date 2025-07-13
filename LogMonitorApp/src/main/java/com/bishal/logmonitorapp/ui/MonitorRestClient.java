package com.bishal.logmonitorapp.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MonitorRestClient {

	private final String BASE = "http://localhost:8080/api/logs";
	private final ObjectMapper mapper = new ObjectMapper();

	public String startMonitoring(String path) {
		return getText(BASE + "/startMonitoring?path=" + path);
	}

	public String stopMonitoring(String path) {
		return getText(BASE + "/stopMonitoring?path=" + path);
	}

	public Map<String, Boolean> getMonitoringStatuses() {
		try {
			URL url = new URL(BASE + "/monitoringThreadsStatus");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			try (InputStream is = conn.getInputStream()) {
				return mapper.readValue(is, new TypeReference<>() {});
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Map.of();
		}
	}

	private String getText(String urlStr) {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			try (InputStream is = conn.getInputStream()) {
				return new Scanner(is).useDelimiter("\\A").next();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Request failed.";
		}
	}


	public boolean getFileMonitoringStatus(String path) {
		try {
			URL url = new URL(BASE + "/getFileMonitoringStatus?path=" + path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			try (InputStream is = conn.getInputStream()) {
				String response = new Scanner(is).useDelimiter("\\A").next();
				return Boolean.parseBoolean(response.trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}