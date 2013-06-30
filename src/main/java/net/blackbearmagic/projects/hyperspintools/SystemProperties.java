package net.blackbearmagic.projects.hyperspintools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class SystemProperties {

	private static Properties properties;
	private static String propertyFile = "HyperSpinDirectoryCreate.properties";

	private SystemProperties() {

	}

	public static Properties getProperties() {
		return SystemProperties.properties;
	}

	public static void saveProperties() {
		if (SystemProperties.printToSystemOut()) {
			System.out.println("Saving properties");
		}
		File f = new File(SystemProperties.propertyFile);
		OutputStream out;
		try {
			out = new FileOutputStream(f);
			SystemProperties.properties.store(out, "Autosaved properties.");
			out.close();
		} catch (Exception e) {
			if (SystemProperties.printToSystemOut()) {
				e.printStackTrace();
			}
		}
	}

	public static void loadDefaultProperties(boolean makeNew) {
		if (makeNew) {
			SystemProperties.properties = new Properties();
		}
		if (SystemProperties.properties.getProperty("output.directory") == null) {
			SystemProperties.properties.put("output.directory", "./");
		}

		if (SystemProperties.properties.getProperty("source.directory") == null) {
			SystemProperties.properties.put("source.directory", "./");
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
				.getProperty("log.file.retention.number") == null) {
			SystemProperties.properties.put("log.file.retention.number", "5");
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
			URL fileURL = SystemProperties
					.findFile(SystemProperties.propertyFile);
			if (fileURL != null) {
				in = new FileInputStream(
						new File(SystemProperties.findFile(
								SystemProperties.propertyFile).toURI())
								.getAbsolutePath());
			} else {
				throw new FileNotFoundException(
						"Could not load property file: "
								+ SystemProperties.propertyFile);
			}
		} catch (Exception e1) {
			if (SystemProperties.printToSystemOut()) {
				e1.printStackTrace();
			}
		}
		if (in != null) {
			try {
				SystemProperties.properties.load(in);
			} catch (IOException e) {
				if (SystemProperties.printToSystemOut()) {
					e.printStackTrace();
				}
			}
			try {
				in.close();
			} catch (IOException e) {
				if (SystemProperties.printToSystemOut()) {
					e.printStackTrace();
				}
			}
		} else {
			if (SystemProperties.printToSystemOut()) {
				System.out.println("No property file found, using defaults.");
			}
		}

		SystemProperties.loadDefaultProperties(false);

	}

	public static URL findFile(String file) {
		URL pathToFile = null;
		File theFile = new File(file);
		try {
			if (theFile.exists()) {
				pathToFile = theFile.toURI().toURL();
			} else {
				throw new MalformedURLException(
						"Does not exist, this is not our path");
			}
		} catch (MalformedURLException e) {
			// Exception means there was a problem locating or accessing the
			// file. Means it does not count as found for us.
			pathToFile = null;
		}
		if (pathToFile == null) {
			pathToFile = SystemProperties.class.getClassLoader().getResource(
					file);
		}
		if (pathToFile == null) {
			pathToFile = SystemProperties.class.getClassLoader().getResource(
					theFile.getName());
		}
		if (pathToFile == null) {
			pathToFile = Thread.currentThread().getContextClassLoader()
					.getResource(file);
		}
		if (pathToFile == null) {
			pathToFile = Thread.currentThread().getContextClassLoader()
					.getResource(theFile.getName());
		}
		if (pathToFile == null) {
			if (SystemProperties.printToSystemOut()) {
				System.out.println("Could not find the file: " + file);
			}

		}

		return pathToFile;
	}

	private static boolean printToSystemOut() {
		return Boolean.getBoolean("logSystemPropertyMessages")
				|| Boolean.parseBoolean(SystemProperties.properties
						.getProperty("log.print.to.system.out"));
	}

}
