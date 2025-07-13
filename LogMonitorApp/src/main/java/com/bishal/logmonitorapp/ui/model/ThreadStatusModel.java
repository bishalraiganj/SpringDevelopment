package com.bishal.logmonitorapp.ui.model;

public class ThreadStatusModel {
	private String filePath;
	private String status;

	public ThreadStatusModel(String filePath, String status) {
		this.filePath = filePath;
		this.status = status;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getStatus() {
		return status;
	}
}