package de.obermui.cab.intern;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class http {

	public String simpleGet(String url) {
		String content = null;
		URLConnection connection = null;
		try {
			connection = new URL(url).openConnection();
			Scanner scanner = new Scanner(connection.getInputStream());
			scanner.useDelimiter("\\Z");
			content = scanner.next();
			scanner.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return content;
	}
}
