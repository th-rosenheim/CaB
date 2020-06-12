package de.obermui.cab;

import de.obermui.cab.cli.MainCLI;
import de.obermui.cab.gui.MainWindow;
import de.obermui.cab.intern.html2pdf;

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
		new MainWindow().start();
	}
}
