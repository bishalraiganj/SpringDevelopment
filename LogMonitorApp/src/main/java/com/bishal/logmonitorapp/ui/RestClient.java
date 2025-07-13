package com.bishal.logmonitorapp.ui;

import com.bishal.logmonitorapp.core.model.LogEntry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class RestClient {
	private final String BASE = "http://localhost:8080/api/logs";
	private final ObjectMapper mapper;

	public RestClient() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule()); // ✔ Fix for LocalDateTime
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ✔ Output ISO-8601 strings
	}

	private List<LogEntry> getList(String urlString) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			try (InputStream is = conn.getInputStream()) {
				return mapper.readValue(is, new TypeReference<>() {});
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<LogEntry> getAllLogs() {
		return getList(BASE + "/getAll");
	}

	public List<LogEntry> filterByPath(String path) {
		String encodedPath = URLEncoder.encode(path, StandardCharsets.UTF_8);
		return getList(BASE + "/filterByPath?path=" + encodedPath);
	}

	public List<LogEntry> filterByLevel(String level) {
		return getList(BASE + "/filterByLevel?level=" + level);
	}

	public List<LogEntry> filterByTime(String start, String end) {
		return getList(BASE + "/filterByTimePeriod?startDateTime=" + start + "&endDateTime=" + end);
	}

	public List<LogEntry> getRecent(String seconds) {
		return getList(BASE + "/getRecent?timeUnit=" + seconds);
	}

	public String evaluateAlerts(String threshold, String duration) {
		return getText(BASE + "/evaluateAlerts?threshold=" + threshold + "&duration=" + duration);
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

	public String startSyntheticWriting(String path) {
		return getText(BASE + "/startTestWritingToFile?path=" + encode(path));
	}

	private String encode(String input) {
		try {
			return URLEncoder.encode(input, StandardCharsets.UTF_8);
		} catch (Exception e) {
			return input;
		}
	}

	public List<LogEntry> filterByTimePeriod(String start, String end) {
		return getList(BASE + "/filterByTimePeriod?startDateTime=" + start + "&endDateTime=" + end);
	}

	public String clearMemory() {
		return getText(BASE + "/clearMemory");
	}
}