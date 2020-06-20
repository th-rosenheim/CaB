package de.obermui.cabr2.models;

// Based on Regulation (EC) No 1272/2008 of the European Parliament and of the Council of 16 December 2008
// Ref: https://eur-lex.europa.eu/legal-content/EN/TXT/?qid=1590769556893&uri=CELEX:32008R1272

import java.util.ArrayList;
import java.util.List;

public class SafetyDataSheet {
	// Sheet title
	public String Title = "";

	// Org
	public String Org = "";

	// Course/Event
	public String Course = "";

	// Personal
	public Personal Personal;

	// Task/Preparation
	public String Task = "";

	// Additional dangers for humans and environment (e.g. carcinogenic, teratogenic)
	public String AdditionalDanger = "";

	// Additional safety rules and protective measures
	public String AdditionalSafetyRules = "";

	// Additional first aid and behaviour in case of danger
	public String AdditionalFirstAid = "";

	// Additional waste disposal infos
	public String AdditionalWasteDisposal = "";

	// List of Substances
	private List<Substance> substances;

	// Hazard & Precautionary Statements
	public List<String> HCodesShort;
	public List<String> HCodes;
	public List<String> PCodesShort;
	public List<String> PCodes;

	public SafetyDataSheet() {
		// init Lists
		this.substances = new ArrayList<>();
		this.HCodesShort = new ArrayList<>();
		this.HCodes = new ArrayList<>();
		this.PCodesShort = new ArrayList<>();
		this.PCodes = new ArrayList<>();
		// init Personal
		this.Personal = new Personal("");
	}

	public List<Substance> getSubstances() {
		return substances;
	}

	public void addSubstance(Substance s) {
		this.substances.add(s);
		for (String code : s.HCodes) {
			String[] sl_short = code.split(":")[0].replace("H", "").split("\\+");
			boolean next = false;
			for (String s_short : sl_short) {
				if (HCodesShort.contains(s_short)) {
					next = true;
					break;
				}
				HCodesShort.add(s_short);
			}
			if (next) continue;
			HCodes.add(code);
		}
		for (String code : s.PCodes) {
			String[] sl_short = code.split(":")[0].replace("H", "").split("\\+");
			boolean next = false;
			for (String s_short : sl_short) {
				if (PCodesShort.contains(s_short)) {
					next = true;
					break;
				}
				PCodesShort.add(s_short);
			}
			if (next) continue;
			PCodes.add(code);
		}
	}

}
