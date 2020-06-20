package de.obermui.cabr2.intern;

import de.obermui.cabr2.models.SafetyDataSheet;
import de.obermui.cabr2.models.Substance;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.util.Arrays;

public class Beryllium {
	public static SafetyDataSheet Import(String filePath) {
		SafetyDataSheet sheet = new SafetyDataSheet();

		FileInputStream in = null;
		Document doc = new Document("");

		try {
			in = new FileInputStream(filePath);
			doc = Jsoup.parse(in, null, "");
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// return null if file could not be loaded
		if (doc.select("beryllium").size() != 1) {
			return null;
		}

		// import all substances
		for (Element subs : doc.select("substance")) {
			sheet.addSubstance(getSubstance(subs));
		}

		Element general = doc.selectFirst("general");
		if (general != null) {
			Element title = general.selectFirst("title");
			if (title != null) sheet.Title = title.html();

			Element institute = general.selectFirst("institute");
			if (institute != null) sheet.Org = institute.html();
		}

		Element personal = doc.selectFirst("personal");
		if (personal != null) {
			Element name = personal.selectFirst("name");
			if (name != null) sheet.Personal.Name = name.html();

			Element spot = personal.selectFirst("spot");
			if (spot != null) sheet.Personal.Place = spot.html();

			Element assistant = personal.selectFirst("assistant");
			if (assistant != null) sheet.Personal.Assistant = assistant.html();
		}

		Element product = doc.selectFirst("product");
		if (product != null) {
			Element name = product.selectFirst("name");
			if (name != null) sheet.Task = name.html();
		}

		return sheet;
	}

	private static Substance getSubstance(Element subs) {
		Substance s = new Substance();

		Elements names = subs.select("name");
		if (names.size() != 0) {
			s.Name = names.get(0).html().strip();
			for (Element alias : names) {
				if (alias.html().strip() == s.Name) continue;
				s.Alias.add(alias.html().strip());
			}
		}

		Element mol = subs.selectFirst("molecular-weight");
		if (mol != null) {
			String MOL = mol.html();
			if (MOL.contains(",")) MOL = helper.stripTrailingChar(MOL, '0');
			s.MOL = MOL.strip();
		}

		Element density = subs.selectFirst("density");
		if (density != null) {
			String Density = density.html();
			if (Density.contains(",")) Density = helper.stripTrailingChar(Density, '0');
			s.Density = Density.strip();
		}

		Element cas = subs.selectFirst("CAS");
		if (cas != null) {
			s.CAS = cas.html().strip();
		}

		Element formula = subs.selectFirst("chemical-formula");
		if (formula != null) {
			s.Sum = formula.html().strip();
		}

		Element wgk = subs.selectFirst("WGK");
		if (density != null) {
			s.WGK = Integer.parseInt(wgk.html().strip());
		}

		Element signalword = subs.selectFirst("GHS-signalword");
		if (signalword != null) {
			s.Signalword = signalword.html().strip();
		}

		Element hcodes = subs.selectFirst("harzard-statements");
		if (hcodes != null) {
			s.HCodes.addAll(Arrays.asList(hcodes.html().strip().split("-")));
			s.HCodesShort.addAll(s.HCodes);
		}

		Element pcodes = subs.selectFirst("precautionary-statements");
		if (pcodes != null) {
			s.PCodes.addAll(Arrays.asList(pcodes.html().strip().split("-")));
			s.PCodesShort.addAll(s.PCodes);
		}

		Elements ghs_symbs = subs.select("GHS-symbol");
		for (Element symb : ghs_symbs) {
			s.Pictograms.add("ghs" + symb.html().strip());
		}

		Element flashpoint = subs.selectFirst("flashpoint");
		if (flashpoint != null) {
			s.FlashPoint = signalword.html().replace("°C", "").strip();
		}

		Element boilingpoint = subs.selectFirst("boiling-point");
		if (boilingpoint != null) {
			s.BoilingPoint = boilingpoint.html().replace("°C", "").strip();
		}

		Element meltingpoint = subs.selectFirst("melting-point");
		if (meltingpoint != null) {
			s.MeltingPoint = meltingpoint.html().replace("°C", "").strip();
		}

		Element mak = subs.selectFirst("MAK");
		if (mak != null) {
			String MAK = mak.html();
			if (MAK.contains(",")) MAK = helper.stripTrailingChar(MAK, '0');
			s.MAK = MAK.strip();
		}

		// source infos
		Elements sourceUrl = subs.getElementsByTag("source.url");
		if (sourceUrl.size() != 0) {
			s.SourceURL = sourceUrl.html().strip();
		}
		Elements sourceFetched = subs.getElementsByTag("source.fetched");
		if (sourceFetched.size() != 0) {
			s.SourceFetched = sourceFetched.html().strip();
		}
		Elements sourceProvider = subs.getElementsByTag("source.provider");
		if (sourceProvider.size() != 0) {
			s.SourceProvider = sourceProvider.html().strip();
		}

		if (s.SourceURL.matches("^[\\s\\S]+/gestis_de/[\\d]+.xml[\\s\\S]+$")) {
			String ZVG = s.SourceURL.split("/gestis_de/")[1].split(".xml")[0];
			s.ZVG = helper.stripLeadingChar(ZVG, '0');
		}

		return s;
	}
}
