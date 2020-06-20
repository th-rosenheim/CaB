package de.obermui.cabr2;

import de.obermui.cabr2.cli.MainCLI;
import de.obermui.cabr2.gui.MainWindow;
import de.obermui.cabr2.intern.html2pdf;

public class Main {
	public static void main(String[] args) {

		for (String s : args) {
			System.out.println(s);
		}

		if (args.length > 0) {
			if (args[0].equals("cli")) {
				MainCLI.start();
				return;
			} else if (args[0].equals("pdftest")) {
				html2pdf.test();
				return;
			}
		}

		// Default: start GUI
		new MainWindow();
	}
}
