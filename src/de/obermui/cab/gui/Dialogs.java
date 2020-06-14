package de.obermui.cab.gui;

import java.awt.*;

import javax.swing.*;

public class Dialogs {

	public static void infoBox(JFrame frame, String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(frame, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

	public static String saveAs(JFrame frame, String format) {
		FileDialog fDialog = new FileDialog(frame, "Speichern unter ...", FileDialog.SAVE);
		fDialog.setFilenameFilter((dir, filename) -> filename.endsWith(format));
		fDialog.setFile("Untitled" + format);
		fDialog.setVisible(true);
		String file = fDialog.getFile();
		if (!file.endsWith(format)) {
			file += format;
		}
		return file;
	}
}
