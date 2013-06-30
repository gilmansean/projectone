package net.blackbearmagic.projects.hyperspintools;

import java.io.File;
import java.io.FilenameFilter;

public class MyFilter implements FilenameFilter {

	String filter = "";

	public MyFilter(String theFilter) {
		this.filter = theFilter;
	}

	public boolean accept(File dir, String name) {
		boolean matches = false;
		if (name.matches(this.filter)) {
			matches = true;
		}
		return matches;
	}

}
