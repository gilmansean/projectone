package net.blackbearmagic.projects.hyperspintools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	private static LogLevel defaultLogLevel = LogLevel.DEBUG;
	@SuppressWarnings("unused")
	private static Long logRetentionSize = new Long(5000000);
	private static String logFileName = "hstool.log";
	private static File logFile;
	private static Boolean printToSystemOut = new Boolean(false);

	private Log() {

	}

	static {
		try {
			Log.defaultLogLevel = LogLevel.valueOf(SystemProperties
					.getProperties().getProperty("log.default.level"));
		} catch (Exception e) {
			Log.defaultLogLevel = LogLevel.DEBUG;
		}
		Log.printToSystemOut = Boolean.parseBoolean(SystemProperties
				.getProperties().getProperty("log.print.to.system.out"));
		try {
			Integer retentionSize = Integer.parseInt(SystemProperties
					.getProperties().getProperty("log.file.retention.MBsize"));
			Log.logRetentionSize = (long) (retentionSize * 1000000);
		} catch (Exception e) {
			Log.logRetentionSize = new Long(5000000);
		}
		Log.logFileName = SystemProperties.getProperties().getProperty(
				"log.file");
		File presentDirecoty = new File(Log.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath()).getParentFile();
		Log.logFile = new File(presentDirecoty.getAbsolutePath(),
				Log.logFileName);
		Log.trimOldLogFiles();
		SystemProperties.setReadyToLog();
	}

	public static void trimOldLogFiles() {
		try {

			if (Log.logFile.exists()) {

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
			out.println(Log.getFormattedTime() + text);
			out.close();
		} catch (Exception e) {
			if (Log.printToSystemOut) {
				e.printStackTrace();
			}
		}
	}

	private static String getFormattedTime() {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		String formattedTime = dateTimeFormat.format(new Date());
		return "[" + formattedTime + "] ";
	}

	private static void appendExceptionToLogFile(Exception exception) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(Log.logFile, true)));
			out.println(Log.getFormattedTime() + "Exception=>");
			exception.printStackTrace(out);
			out.close();
		} catch (Exception e) {
			if (Log.printToSystemOut) {
				e.printStackTrace();
			}
		}
	}
}
