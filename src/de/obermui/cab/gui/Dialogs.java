package de.obermui.cab.gui;

import java.awt.*;

import javax.swing.*;

public class Dialogs {

	public static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

	public static String saveAs(JFrame frame) {
		FileDialog fDialog = new FileDialog(frame, "Speichern unter ...", FileDialog.SAVE);
		fDialog.setVisible(true);
		return fDialog.getFile();
	}
}
