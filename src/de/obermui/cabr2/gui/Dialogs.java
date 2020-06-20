package de.obermui.cabr2.gui;

import java.awt.*;

import javax.swing.*;

public class Dialogs {

	public static void infoBox(JFrame frame, String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(frame, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

	public static String saveAs(ctx Ctx, String format) {
		// init Dialog
		FileDialog fDialog = new FileDialog(Ctx.mainWindow, "Speichern unter ...", FileDialog.SAVE);
		fDialog.setFilenameFilter((dir, filename) -> filename.endsWith(format));
		fDialog.setFile("Untitled" + format);
		if (Ctx.lastSavedFileDir != null) {
			fDialog.setDirectory(Ctx.lastSavedFileDir);
		}
		fDialog.setVisible(true);

		// Wait & Get Input from User
		String file = fDialog.getFile();
		String dir = fDialog.getDirectory();

		// User canceled dialog
		if (file == null) {
			return "";
		}

		// Make sure Windows can open it
		if (!file.endsWith(format)) {
			file += format;
		}
		// remember directory for next time
		Ctx.lastSavedFileDir = dir;
		// return absolute path
		return dir + file;
	}

	public static String open(ctx Ctx, String format) {
		// init Dialog
		FileDialog fDialog = new FileDialog(Ctx.mainWindow, "Öffne", FileDialog.LOAD);
		fDialog.setFilenameFilter((dir, filename) -> filename.endsWith(format));
		fDialog.setVisible(true);

		// Wait & Get Input from User
		String file = fDialog.getFile();
		String dir = fDialog.getDirectory();

		// User canceled dialog
		if (file == null) {
			return "";
		}

		// return absolute path
		return dir + file;
	}
}
