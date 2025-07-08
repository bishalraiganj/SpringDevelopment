package com.bishal.logmonitorapp.model;

import java.nio.file.Path;
import java.time.LocalDateTime;

public record LogEntry(LocalDateTime timestamp, String logLevel, String source, String message, Path filePath) {



}
