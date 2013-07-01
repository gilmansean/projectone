package net.blackbearmagic.projects.hyperspintools;

import java.io.File;
import java.io.FilenameFilter;

public class FileAndNameFilter implements FilenameFilter {

	String filter = "";

	public FileAndNameFilter(String theFilter) {
		this.filter = theFilter;
	}

	public boolean accept(File dir, String name) {
		boolean matches = false;
		if (name.matches(this.filter)) {
			if(dir.isDirectory()&&new File(dir,name).isFile()){
				matches = true;
			}
		}
		return matches;
	}

}
