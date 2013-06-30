package net.blackbearmagic.projects.hyperspintools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Help {

	public static String getHelpText() {
		String helpText = "";
		InputStream input = Help.class
				.getResourceAsStream("/net/blackbearmagic/projects/hyperspintools/HelpText.txt");
		Scanner s1 = new Scanner(input);
		Scanner s = s1.useDelimiter("\\A");
		helpText = s.hasNext() ? s.next() : "";
		s.close();
		s1.close();
		try {
			input.close();
		} catch (IOException e) {
			Log.logMessage(e);
		}
		return helpText;
	}
}
