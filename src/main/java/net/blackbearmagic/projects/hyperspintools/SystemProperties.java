package net.blackbearmagic.projects.hyperspintools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class SystemProperties {

	private static Properties properties;
	private static File propertyFile;
	private static File presentDirecoty;
	private static boolean readyToLog = false;

	private SystemProperties() {

	}

	static {
		File me = new File(SystemProperties.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath());
		SystemProperties.presentDirecoty = me.getParentFile();
		String myName = me.getName();
		int lastPeriodPos = myName.lastIndexOf('.');
		String propertyFileName = myName.substring(0, lastPeriodPos)
				+ ".properties";
		SystemProperties.propertyFile = new File(
				SystemProperties.presentDirecoty, propertyFileName);
	}

	public static Properties getProperties() {
		return SystemProperties.properties;
	}

	public static void saveProperties() {
		SystemProperties.logMessage("Saving properties");
		OutputStream out;
		try {
			out = new FileOutputStream(SystemProperties.propertyFile);
			SystemProperties.properties.store(out, "Autosaved properties.");
			out.close();
		} catch (Exception e) {
			SystemProperties.logMessage(e);

		}
	}

	public static void loadDefaultProperties(boolean makeNew) {
		if (makeNew) {
			SystemProperties.properties = new Properties();
		}
		if (SystemProperties.properties.getProperty("output.directory") == null) {
			SystemProperties.properties.put("output.directory",
					SystemProperties.presentDirecoty.getAbsolutePath());
		}

		if (SystemProperties.properties.getProperty("source.directory") == null) {
			SystemProperties.properties.put("source.directory",
					SystemProperties.presentDirecoty.getAbsolutePath());
		}
		if (SystemProperties.properties.getProperty("file.filter") == null) {
			SystemProperties.properties.put("file.filter", ".*");
		}
		if (SystemProperties.properties.getProperty("log.file") == null) {
			SystemProperties.properties.put("log.file", "hstool.log");
		}
		if (SystemProperties.properties.getProperty("log.default.level") == null) {
			SystemProperties.properties.put("log.default.level", "INFO");
		}
		if (SystemProperties.properties
				.getProperty("log.file.retention.MBsize") == null) {
			SystemProperties.properties.put("log.file.retention.MBsize", "5");
		}
		if (SystemProperties.properties.getProperty("log.print.to.system.out") == null) {
			SystemProperties.properties.put("log.print.to.system.out", "false");
		}
		if (SystemProperties.properties.getProperty("selected.action") == null) {
			SystemProperties.properties.put("selected.action",
					Action.CREATE.toString());
		}
		try {
			if (Action.getEnum(SystemProperties.properties
					.getProperty("selected.action")) == null) {
				SystemProperties.properties.put("selected.action",
						Action.CREATE.toString());
			}
		} catch (Exception e) {
			SystemProperties.properties.put("selected.action",
					Action.CREATE.toString());
		}

	}

	public static void loadSettings() throws FileNotFoundException {
		SystemProperties.properties = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(
					SystemProperties.propertyFile.getAbsolutePath());
		} catch (Exception e1) {
			SystemProperties.logMessage(e1);
		}
		if (in != null) {
			try {
				SystemProperties.properties.load(in);
			} catch (IOException e) {
				SystemProperties.logMessage(e);
			}
			try {
				in.close();
			} catch (IOException e) {
				SystemProperties.logMessage(e);
			}
		} else {
			SystemProperties
					.logMessage("No property file found, using defaults.");
		}

		SystemProperties.loadDefaultProperties(false);

	}

	private static boolean printToSystemOut() {
		return Boolean.getBoolean("logSystemPropertyMessages")
				|| Boolean.parseBoolean(SystemProperties.properties
						.getProperty("log.print.to.system.out"));
	}

	private static void logMessage(String message) {
		if (SystemProperties.readyToLog) {
			Log.logMessage(message);
		} else if (SystemProperties.printToSystemOut()) {
			System.out.println(message);
		}
	}

	private static void logMessage(Exception exception) {
		if (SystemProperties.readyToLog) {
			Log.logMessage(exception);
		} else if (SystemProperties.printToSystemOut()) {
			exception.printStackTrace();
		}
	}

	public static void setReadyToLog() {
		SystemProperties.readyToLog = true;
	}

}
