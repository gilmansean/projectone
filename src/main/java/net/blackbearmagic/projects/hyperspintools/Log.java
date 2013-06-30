package net.blackbearmagic.projects.hyperspintools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Log {
	private static LogLevel defaultLogLevel = LogLevel.DEBUG;
	private static Integer logRetention = new Integer(5);
	private static String logFileName = "hstool.log";
	private static File logFile;
	private static Boolean printToSystemOut = new Boolean(false);

	private Log() {
		try {
			Log.defaultLogLevel = LogLevel.valueOf(SystemProperties
					.getProperties().getProperty("log.default.level"));
		} catch (Exception e) {
			Log.defaultLogLevel = LogLevel.DEBUG;
		}
		Log.printToSystemOut = Boolean.parseBoolean(SystemProperties
				.getProperties().getProperty("log.print.to.system.out"));
		try {
			Log.logRetention = Integer.parseInt(SystemProperties
					.getProperties().getProperty("log.file.retention.number"));
		} catch (NumberFormatException e) {
			Log.logRetention = new Integer(5);
		}
		Log.logFileName = SystemProperties.getProperties().getProperty(
				"log.file");
		Log.logFile = new File(Log.logFileName + Thread.currentThread().getId());
		this.trimOldLogFiles();
	}

	public void trimOldLogFiles() {
		try {
			File sourceDirectory = Log.logFile.getParentFile();
			String[] fileList = sourceDirectory.list(new MyFilter(
					Log.logFileName));
			if ((fileList != null) && (fileList.length > Log.logRetention)) {
				int fileTrims = fileList.length - Log.logRetention;
				while (fileTrims < fileList.length) {
					File deleteMe = new File(sourceDirectory,
							fileList[fileTrims + 1]);
					deleteMe.delete();
				}
			}
		} catch (Exception e) {
			if (Log.printToSystemOut) {
				e.printStackTrace();
			}
		}
	}

	public static void logMessage(String message) {
		Log.logMessage(Log.defaultLogLevel, message, null);
	}

	public static void logMessage(String message, Exception exception) {
		Log.logMessage(Log.defaultLogLevel, message, exception);
	}

	public static void logMessage(Exception exception) {
		Log.logMessage(Log.defaultLogLevel, null, exception);
	}

	public static void logMessage(LogLevel level, String message,
			Exception exception) {
		if (Log.canLog(level)) {
			if (message != null) {
				Log.appendToLogFile(message);
			}
			if (exception != null) {
				Log.appendExceptionToLogFile(exception);
			}
			if (Log.printToSystemOut) {
				if (message != null) {
					System.out.println(message);
				}
				if (exception != null) {
					exception.printStackTrace();
				}
			}
		}
	}

	private static boolean canLog(LogLevel level) {
		return Log.defaultLogLevel.compareTo(level) <= 0;
	}

	private static void appendToLogFile(String text) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(Log.logFile, true)));
			out.println(text);
			out.close();
		} catch (Exception e) {
			if (Log.printToSystemOut) {
				e.printStackTrace();
			}
		}
	}

	private static void appendExceptionToLogFile(Exception exception) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(Log.logFile, true)));
			exception.printStackTrace(out);
			out.close();
		} catch (Exception e) {
			if (Log.printToSystemOut) {
				e.printStackTrace();
			}
		}
	}
}
