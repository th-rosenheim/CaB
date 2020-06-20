package de.obermui.cabr2.models;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Substance {
	// 1.1.1.1. Index number
	// Entries in Part 3 are listed according to the atomic number of the element most characteristic of the properties
	// of the substance. Organic substances, because of their variety, have been placed in classes.
	// The Index number foreach substance is in the form of a digit sequence of the type ABC-RST-VW-Y.
	// ABC corresponds to the atomic number of the most characteristic element or the most characteristic organic group in the molecule.
	// RST is the consecutive number of the substance in the series ABC. VW denotes the form in which the substance is produced or placed on the market.
	// Y is the check-digit calculated in accordance with the 10-digit ISBN method. This number is indicated in the column entitled ‘Index No’.
	public String Index;

	// 1.1.1.2. EC numbers
	// The EC number, i.e. EINECS, ELINCS or NLP, is the official number of the substance within the European Union.
	// The EINECS number can be obtained from the European Inventory of Existing Commercial Chemical Substance(EINECS) (1).
	// The ELINCS number can be obtained from the European List of Notified Substances (as amended)
	// (EUR 22543 EN, Office for Official Publications of the European Communities, 2006, ISSN 1018-5593).
	// TheNLP number can be obtained from the list of‘No-longer-polymers’(as amended)
	// (Document, Office for OfficialPublications of the European Communities, 1997, ISBN 92-827-8995-0).
	// The EC number is a seven-digitsystem of the type XXX-XXX-X which starts at 200-001-8 (EINECS), at 400-010-9 (ELINCS) and at 500-001-0(NLP).
	// This number is indicated in the column entitled ‘EC No’.
	public String EC;

	// 1.1.1.3. CAS number
	// The Chemical Abstracts Service (CAS) number is also included to assist identification of the entry.
	// It should be noted that the EINECS number includes both anhydrous and hydrated forms of a substance,
	// and there are frequently different CAS numbers for anhydrous and hydrated forms.
	// The CAS number included is for the anhydrous form only, and therefore the CAS number shown does not always describe the entry as accurately asthe EINECS number.
	// This number is indicated in the column entitled ‘CAS No’.
	public String CAS;

	// 1.1.1.4. International Chemical Identification
	// Wherever possible, hazardous substances are designated by their IUPAC names.
	// Substances listed in EINECS, ELINCS or the list of ‘No-longer-polymers’ are designated using the names in these lists.
	// Other names, such as usual or common names, are included in some cases. Whenever possible, plant protection products and
	// biocides are designated by their ISO names.
	public String InternationalChemicalIdentification;

	// 1.1.2.1. Classification codes
	public List<String> HCodesShort;
	public List<String> HCodes;
	public List<String> PCodesShort;
	public List<String> PCodes;
	public List<String> Pictograms;
	public String Signalword = "";

	/**
	 * Own Fields
	 */

	// Name
	public String Name;

	// Alias
	public List<String> Alias;

	// GESTIS internal Nr
	public String ZVG;

	// Sum Formula
	public String Sum = "";

	// Molecular weight g/mol
	public String MOL = "";

	// Melting Point in °C
	public String MeltingPoint = "";

	// Decomposition indicate if substance will decomposition on MeltingPoint
	public boolean Decomposition;

	// Boiling Point in °C
	public String BoilingPoint = "";

	// Density g/cm²
	public String Density = "";

	// GERMAN WATER HAZARD CLASS
	public int WGK;

	// Amount in g
	public String Amount = "";

	/*
	 * Constructors
	 */

	public Substance() {
		initArrays();
	}

	public Substance(String name) {
		this.Name = name;
		initArrays();
	}

	/*
	 * help methods
	 */

	private void initArrays() {
		this.Alias = new ArrayList<>();
		this.HCodesShort = new ArrayList<>();
		this.HCodes = new ArrayList<>();
		this.PCodesShort = new ArrayList<>();
		this.PCodes = new ArrayList<>();
		this.Pictograms = new ArrayList<>();
	}

	@NotNull
	public String toString() {
		String result = this.Name + ": CAS=" + this.CAS +
			"; EC=" + this.EC + "; Index=" + this.Index +
			"; SumFrom=" + this.Sum + "; Mol=" + this.MOL +
			"; MeltingPoint=" + this.MeltingPoint +
			"; Density=" + this.Density + "; BoilingPoint=" + this.BoilingPoint;
		return result;
	}
}
