package net.blackbearmagic.projects.hyperspintools;

import java.io.File;

public class ExecuteAction {

	private ExecuteAction() {

	}

	public static boolean isActionSelected(Action action) {
		boolean selected = false;
		if (Action.getEnum(SystemProperties.getProperties().getProperty(
				"selected.action")) == action) {
			selected = true;
		} else {
			selected = false;
		}
		return selected;
	}

	public static String run() throws Exception {
		String returnStaticts = "";
		int directoriesCreated = 0;
		int filesMoved = 0;
		File sourceDirectory = new File((String) SystemProperties
				.getProperties().get("source.directory"));
		String filter = SystemProperties.getProperties().getProperty(
				"file.filter");
		String[] fileList = sourceDirectory.list(new FileAndNameFilter(filter));
		for (String fileName : fileList) {
			File directory = null;
			int lastPeriodPos = fileName.lastIndexOf('.');
			if (lastPeriodPos <= 0) {
				directory = new File(SystemProperties.getProperties()
						.getProperty("output.directory"), fileName);
			} else {
				directory = new File(SystemProperties.getProperties()
						.getProperty("output.directory"), fileName.substring(0,
						lastPeriodPos));
			}
			if (ExecuteAction.isActionSelected(Action.CREATE)
					|| ExecuteAction.isActionSelected(Action.CREATE_MOVE)) {
				if (!directory.exists()) {
					if (directory.mkdirs()) {
						Log.logMessage("Directory created:"
								+ directory.getAbsolutePath());
						directoriesCreated++;
					} else {
						Log.logMessage("Directory PROBLEM:"
								+ directory.getAbsolutePath());
					}
				}
			}
			if (ExecuteAction.isActionSelected(Action.MOVE)
					|| ExecuteAction.isActionSelected(Action.CREATE_MOVE)) {
				if (directory.exists()) {
					File existingFile = new File(SystemProperties
							.getProperties().getProperty("source.directory"),
							fileName);
					File newFile = new File(directory, fileName);
					if (existingFile.renameTo(newFile)) {
						Log.logMessage("File moved:"
								+ newFile.getAbsolutePath());
						filesMoved++;
					} else {
						Log.logMessage("File Move PROBLEM:"
								+ newFile.getAbsolutePath());
					}
				}
			}
		}
		returnStaticts += "Number of new directories created:"
				+ directoriesCreated + "\n";
		returnStaticts += "Number of files moved:" + filesMoved + "\n";
		return returnStaticts;

	}
}
