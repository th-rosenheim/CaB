package de.obermui.cabr2.cli;

import de.obermui.cabr2.clients.*;
import de.obermui.cabr2.intern.Sheet2Html;
import de.obermui.cabr2.intern.html2pdf;
import de.obermui.cabr2.models.Personal;
import de.obermui.cabr2.models.SafetyDataSheet;
import de.obermui.cabr2.models.Substance;

import java.util.*;

import static de.obermui.cabr2.intern.Const.*;

public class MainCLI {
	public static void start() {
		Scanner sc = new Scanner(System.in);

		System.out.println("Start " + AppName);
		System.out.println("\n");
		System.out.println("Please Type In what you want:");
		String query = sc.next();

		SafetyDataSheet sheet = new SafetyDataSheet();

		Client client = new GESTIS();

		boolean next = true;
		while (next) {

			String[] content = client.SimpleSearch(query, false);
			for (int i = 0; i < content.length; i++) {
				System.out.println(i + ":\t" + content[i]);
			}
			System.out.println("\n");
			System.out.println("Please type the Number you want:");
			int i = sc.nextInt();
			if (i > content.length || i < 0) {
				System.out.println("invalid number: '" + i + "'");
				continue;
			}
			Substance s = client.GetSubstance(content[i], null);
			if (s.CAS != null) {
				sheet.addSubstance(s);
			}
			System.out.println("Add next substance? [Y/n]");
			String aw = sc.next();
			if (!aw.matches("(Y|y)(es|ES)?")) {
				// if answer is not yes finish substance adding
				next = false;
				continue;
			}
			System.out.println("Type in next substance:");
			query = sc.next();
		}

		System.out.println("================");
		System.out.println("enter course name:");
		sheet.Course = sc.next();
		System.out.println("enter course Personal:");
		sheet.Personal = new Personal(sc.next());


		sheet.Title = "Betriebsanweisung nach EG Nr. 1272/2008";
		sheet.Org = "TH Rosemheim Campus Burghausen";
		sheet.Task = "Empty data, SumFormularPfdsafdsafdsareparation, WhatSoEver you do with XY";

		String htmlResult = Sheet2Html.Convert(sheet, false);
		html2pdf.html2pdf(htmlResult, "result.pdf");

	}
}
