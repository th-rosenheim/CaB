package de.obermui.cabr2.intern;

public class helper {
	public static String stripChar(String s, Character c) {
		return stripTrailingChar(stripLeadingChar(s, c), c);
	}

	public static String stripTrailingChar(String s, Character c) {
		StringBuilder sb = new StringBuilder(s);
		while (sb.length() > 1 && sb.charAt(sb.length() - 1) == c) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}

	public static String stripLeadingChar(String s, Character c) {
		StringBuilder sb = new StringBuilder(s);
		while (sb.length() > 1 && sb.charAt(0) == c) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}
}
