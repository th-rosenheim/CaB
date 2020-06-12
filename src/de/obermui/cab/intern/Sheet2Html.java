package de.obermui.cab.intern;

import de.obermui.cab.models.Personal;
import de.obermui.cab.models.SafetyDataSheet;
import de.obermui.cab.models.Substance;

import java.util.List;

public class Sheet2Html {

	public static String Convert(SafetyDataSheet sheet) {
		//ResourceBundle R = ResourceBundle.getBundle("values", Locale.forLanguageTag("de"));

		StringBuilder builder = new StringBuilder();

		builder.append("<!DOCTYPE html>\n<html>");

		builder.append("<head>\n");
		builder.append(style);
		builder.append("</head>\n");
		builder.append("<body>\n");
		builder.append("\t<table>\n");

		builder.append("<!-- Page Title -->\n");
		builder.append(encodeTableLine(new String[]{
			encodeTableColumn(10, "font-size: 22pt; text-align: center;", "<b>" + sheet.Title + "</b>"),
		}));

		builder.append("<!-- Organisation -->\n");
		builder.append(encodeTableLine(new String[]{
			encodeTableColumn(10, "font-size: 22pt; text-align: center;", "<b>" + sheet.Org + "</b>"),
		}));

		builder.append("<!-- Lab -->\n");
		builder.append(encodeTableLine(new String[]{
			encodeTableColumn(10, "font-size: 22pt; text-align: center;", "<b>" + sheet.Course + "</b>"),
		}));

		builder.append("<!-- General meta infos -->\n");
		if (sheet.Personal == null) {
			sheet.Personal = new Personal("");
		}
		builder.append(encodeTableLine(new String[]{
			encodeTableColumn(4, "", encodeNewline("Name\n" + sheet.Personal.Name)),
			encodeTableColumn(3, "", encodeNewline("Platz\n" + sheet.Personal.Place)),
			encodeTableColumn(3, "", encodeNewline("Assistent/in\n" + sheet.Personal.Assistant)),
		}));
		builder.append(encodeTableLine(new String[]{
			encodeTableColumn(10, "", encodeNewline("Preparation/tasks:\n<b>" + sheet.Task + "</b>")),
		}));

		builder.append("<!-- header Substance list -->\n");
		builder.append(encodeTableLine(new String[]{
			//encodeTableColumn(2, "", "Materials: regents and products"),
			encodeTableColumn(2, "", "eingesetzte Stoffe und Produkt(e)"),
			//encodeTableColumn(1, "", encodeNewline("MW\n[g/mol]")),
			encodeTableColumn(1, "", encodeNewline("MG\n[g/mol]")),
			//encodeTableColumn(1, "", encodeNewline("BP/MP\n[C°]")),
			encodeTableColumn(1, "", encodeNewline("BP/MP\n[C°]")),
			//encodeTableColumn(2, "", "GHS hazard pictograms"),
			encodeTableColumn(2, "", "GHS-Symbol(e)"),
			//encodeTableColumn(2, "", "H/P phrases"),
			encodeTableColumn(2, "", "Nummern der H/P-Sätze"),
			//encodeTableColumn(1, "", "TLV, LD50, WGK"),
			encodeTableColumn(1, "", "MAK, LD50, WGK"),
			//encodeTableColumn(1, "", "Amount"),
			encodeTableColumn(1, "", "Menge"),
		}));

		builder.append("<!-- Substance list -->\n");
		for (Substance s : sheet.getSubstances()) {
			String wgk = "", hp = "";
			if (s.WGK > 0) {
				wgk = "WGK " + s.WGK;
			}
			if (s.HCodesShort.size() != 0) {
				hp = "H " + String.join("-", s.HCodesShort);
			}
			if (s.PCodesShort.size() != 0) {
				if (hp.length() != 0) hp += "\n";
				hp += "P " + String.join("-", s.PCodesShort);
			}
			builder.append(encodeTableLine(new String[]{
				encodeTableColumn(2, "height: 35pt;", encodeNewline(s.Name + "\n" + s.Sum)),
				encodeTableColumn(1, "", String.valueOf(s.MOL)),
				encodeTableColumn(1, "", encodeNewline(s.BoilingPoint + "/\n" + s.MeltingPoint)),
				encodeTableColumn(2, "", encodePictograms(s.Pictograms) + s.Signalword),
				encodeTableColumn(2, "", encodeNewline(hp)),
				encodeTableColumn(1, "", wgk),
				encodeTableColumn(1, "", s.Amount),
			}));
		}

		if (sheet.getSubstances().size() < 6) {
			for (int i = sheet.getSubstances().size(); i <= 6; i++) {
				builder.append(encodeTableLine(new String[]{
					encodeTableColumn(2, "height: 35pt;", ""),
					encodeTableColumn(1, "", ""),
					encodeTableColumn(1, "", ""),
					encodeTableColumn(2, "", ""),
					encodeTableColumn(2, "", ""),
					encodeTableColumn(1, "", ""),
					encodeTableColumn(1, "", ""),
				}));
			}
		}

		builder.append("<!-- header H/P phrases -->\n");
		builder.append(encodeTableLine(new String[]{
			//encodeTableColumn(10, "text-align: center;", "Text of the essential H/P phrases:"),
			encodeTableColumn(10, "text-align: center;", "Wortlaut der wesentlichen oben genannten H- und P-Sätze:"),
		}));

		builder.append("<!-- H/P phrases -->\n");
		String hcodes = "", pcodes = "";
		for (String item : sheet.HCodes) {
			hcodes += item + "\n";
		}
		for (String item : sheet.PCodes) {
			pcodes += item + "\n";
		}
		builder.append(encodeTableLine(new String[]{
			encodeTableColumn(5, "height: 120pt; vertical-align: unset; font-size: 10pt;", encodeNewline(hcodes)),
			encodeTableColumn(5, "height: 120pt; vertical-align: unset; font-size: 10pt;", encodeNewline(pcodes)),
		}));

		//
		// NEXT PAGE - ToDo: create how to make a new page?
		//
		String AdditionalDangerInfo = "Gefahren für Mensch und Umwelt, die von den Ausgangsmaterialien bzw. dem(n) " +
			"Produkt ausgehen, soweit sie nicht durch genannte Angaben abgedeckt sind (z.B. krebserregend, fruchtschädigend, hautresorptiv):";
		builder.append(encodeTableLine(new String[]{
			encodeTableColumn(10, "height: 120pt; vertical-align: unset;", "<div class=\"info\">" + AdditionalDangerInfo + "</div>" + encodeNewline("\n" + sheet.AdditionalDanger)),
		}));

		String AdditionalSafetyRulesInfo = "Schutzmaßnahmen und Verhaltensregeln:";
		builder.append(encodeTableLine(new String[]{
			encodeTableColumn(10, "height: 120pt; vertical-align: unset;", "<div class=\"info\">" + AdditionalSafetyRulesInfo + "</div>" + encodeNewline("\n" + sheet.AdditionalSafetyRules)),
		}));

		String AdditionalFirstAidInfo = "Verhalten im Gefahrenfall, Erste-Hilfe-Maßnahmen (gegebenfalls Kopie der entsprechenden Literaturstelle beiheften):";
		builder.append(encodeTableLine(new String[]{
			encodeTableColumn(10, "height: 120pt; vertical-align: unset;", "<div class=\"info\">" + AdditionalFirstAidInfo + "</div>" + encodeNewline("\n" + sheet.AdditionalFirstAid)),
		}));

		String AdditionalWasteDisposalInfo = "Entsorgung:";
		builder.append(encodeTableLine(new String[]{
			encodeTableColumn(10, "height: 120pt; vertical-align: unset;", "<div class=\"info\">" + AdditionalWasteDisposalInfo + "</div>" + encodeNewline("\n" + sheet.AdditionalWasteDisposal)),
		}));

		// Signing Fields
		builder.append(encodeTableLine(new String[]{
			encodeTableColumn(5, "height: 120pt; vertical-align: unset;", "" +
				"<div>" +
				"Hiermit verpflichte ich mich, den Versuch gemäß den in dieser Betriebsanweisung aufgeführten Sicherheitsvorschriften durchzuführen." +
				"</div>" + encodeNewline("\n\n\n\n") + "<div>" +
				"Unterschrift des/der Student/in" +
				"</div>"
			),
			encodeTableColumn(5, "height: 120pt; vertical-align: unset;", "" +
				"Präparat zur Synthese mit den auf der Vorderseite berechneten Chemikalienmengen freigegeben." +
				encodeNewline("\n\n\n\n") +
				"Unterschrift des/der Assistent/in"
			),
		}));


		builder.append("\t</table>\n");
		builder.append("</body>\n");
		builder.append("</html>\n");

		return builder.toString();
	}

	private static String encodePictograms(List<String> gifs) {
		if (gifs == null) {
			return "";
		}
		String result = "";
		for (String gif : gifs) {
			result += "<img style=\"width: 25pt; height: 25pt;\" src=\"" + Sheet2Html.class.getResource("/res/Images/signs/" + gif + ".gif") + "\" alt=\"" + gif + "\"/>";
		}
		return result;
	}

	private static String encodeNewline(String s) {
		return s.replace("\n", "<div id=\"line-break\"></div>");
	}

	public static String encodeTableLine(String[] content) {
		StringBuilder builder = new StringBuilder();
		builder.append("\t\t<tr>\n");
		for (String c : content) {
			builder.append(c);
		}
		builder.append("\t\t</tr>\n");
		return builder.toString();
	}

	public static String encodeTableColumn(int Rows, String style, String content) {
		if (style == null) {
			style = "";
		} else if (style.length() != 0) {
			style = "; " + style;
		}
		return "\t\t\t<td style=\"width: " + Rows + "0%" + style + "\" colspan=\"" + Rows + "\">" + content + "</td>\n";
	}

	private static String style = "\t<style>\n" +
		"\t\ttable {\n" +
		"\t\t\tfont-family: arial, sans-serif;\n" +
		"\t\t\tborder: 3px solid black;\n" +
		"\t\t\tborder-collapse: collapse;\n" +
		"\t\t\twidth: 100%;\n" +
		"\t\t}\n" +
		"\n" +
		"\t\t#line-break {\n" +
		"\t\t\twhite-space: pre-line;\n" +
		"\t\t}\n" +
		"\n" +
		"\t\ttd,\n" +
		"\t\tth {\n" +
		"\t\t\tborder: 1px solid black;\n" +
		"\t\t\ttext-align: left;\n" +
		"\t\t\tpadding: 8px;\n" +
		"\t\t}\n" +
		"\t</style>\n";
}
