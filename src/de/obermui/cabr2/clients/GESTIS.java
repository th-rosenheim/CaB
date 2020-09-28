package de.obermui.cabr2.clients;

import de.obermui.cabr2.intern.http;
import de.obermui.cabr2.models.Substance;
import de.obermui.cabr2.models.SubstanceShort;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GESTIS implements Client {
	private static final String gestis_en = "http://gestis-en.itrust.de";
	private static final String gestis_de = "http://gestis.itrust.de";

	@Override
	public String[] SimpleSearch(String keyword, boolean exact) {

		keyword = keyword.trim();
		if (!exact) {
			keyword += "*";
		}

		String nameQueryString = gestis_de + "/NXT/gateway.dll?f=element&c=seek&q=[field,schnellsuche:" + keyword + "]&n=300&d=&vid=gestisdeu:sdbdeu";

		String content = new http().simpleGet(nameQueryString);

		String[] list = content.split("\n");
		if (list.length != 0) {
			// check error report
			if (list[0].equals("error 0")) {
				System.out.println("ERROR on GESTIS SimpleSearch: '" + keyword + "'");
				return new String[]{""};
			}
		}
		if (list.length < 5) {
			return new String[]{""};
		}
		String[] result = new String[list.length - 4];
		System.arraycopy(list, 4, result, 0, list.length - 4);

		return result;
	}

	@Override
	public List<SubstanceShort> Search(String keyword, boolean exact) {
		List<SubstanceShort> result = new ArrayList<>();

		if (!exact) {
			keyword += "*";
		}

		String url = "http://gestis.itrust.de/nxt/gateway.dll?qeingabe=&f=xhitlist&xhitlist_x=Advanced&xhitlist_s=field:sortiername&xhitlist_q=[Feld%20schnellsuche:%22" + keyword + "%22]&xhitlist_d=&xhitlist_hc=&xhitlist_mh=2000&xhitlist_vps=500&xhitlist_xsl=xhitlist.xsl&xhitlist_vpc=first&xhitlist_sel=title;path;relevance-weight;content-type;home-title;item-bookmark;field:stoffname;field:sortiername;field:zvgnr;field:casnr;field:egnr;field:indexnr;field:unnr;&searchform_list=#NoSelection";

		HttpClient instance = HttpClientBuilder.create().build();

		HttpGet getCookies = new HttpGet("http://gestis.itrust.de/nxt/gateway.dll/gestis_de/000000.xml?f=templates$fn=default.htm$vid=gestisdeu:sdbdeu$3.0");
		Document doc = new Document("http://gestis.itrust.de");

		try {
			HttpResponse resp = instance.execute(getCookies);
			HttpGet get = new HttpGet(url);

			for (Header h : resp.getAllHeaders()) {
				if (h.getName().equals("Set-Cookie")) {
					get.addHeader("Cookie", h.getValue());
				}
			}

			resp = instance.execute(get);


			InputStream httpInput = resp.getEntity().getContent();
			doc = Jsoup.parse(httpInput, null, "http://gestis.itrust.de");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Elements els = doc.select("tr[class=hitlist-row-odd]");
		for (Element e : els) {
			Elements nrs = e.select("td[class=nummern]");
			if (nrs.size() == 5) {
				SubstanceShort s = new SubstanceShort();
				Elements names = e.select("td[class=hit-title]").select("a");
				if (names.size() == 1 || names.get(0).childNodeSize() == 2) {
					s.Name = names.get(0).childNode(1).toString().trim();
				} else {
					continue;
				}
				s.ZVG = nrs.get(0).html().trim().split(" ")[0];
				s.CAS = nrs.get(1).html().trim().split(" ")[0];
				s.EG = nrs.get(2).html().trim().split(" ")[0];
				s.Index = nrs.get(3).html().trim().split(" ")[0];
				s.UN = nrs.get(4).html().trim().split(" ")[0];
				result.add(s);
			}
		}
		els = doc.select("tr[class=hitlist-row-even]");
		for (Element e : els) {
			Elements nrs = e.select("td[class=nummern]");
			if (nrs.size() == 5) {
				SubstanceShort s = new SubstanceShort();
				Elements names = e.select("td[class=hit-title]").select("a");
				if (names.size() == 1 || names.get(0).childNodeSize() == 2) {
					s.Name = names.get(0).childNode(1).toString().trim();
				} else {
					continue;
				}
				s.ZVG = nrs.get(0).html().trim().split(" ")[0];
				s.CAS = nrs.get(1).html().trim().split(" ")[0];
				s.EG = nrs.get(2).html().trim().split(" ")[0];
				s.Index = nrs.get(3).html().trim().split(" ")[0];
				s.UN = nrs.get(4).html().trim().split(" ")[0];
				result.add(s);
			}
		}

		return result;
	}

	@Override
	public Substance GetSubstance(String name, String ZVG) {
		Substance s = new Substance();

		if (ZVG == null) {
			ZVG = getZVGfromName(name.trim());
		}
		while (ZVG.length() < 6) {
			ZVG = "0" + ZVG;
		}

		String baseURL = "http://gestis.itrust.de/";
		String preURL = "nxt/gateway.dll/gestis_de/";
		String sufURL = ".xml?f=templates$fn=document-frame.htm$3.0$GLOBAL=G_&G_DIEXSL=gestis.xml$q=$uq=$x=$up=1";
		String substanceURL = baseURL + preURL + ZVG + sufURL;


		HttpClient client = HttpClientBuilder.create().build();

		Document doc = new Document(baseURL);
		int httpStatus;

		try {
			HttpGet get = new HttpGet(substanceURL);

			// set lang
			get.addHeader("Cookie", "nxt/gateway.dll/vid=gestisdeu%3Asdbdeu;");

			HttpResponse resp = client.execute(get);
			httpStatus = resp.getStatusLine().getStatusCode();

			InputStream httpInput = resp.getEntity().getContent();
			doc = Jsoup.parse(httpInput, null, "http://gestis.itrust.de");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// getName
		Elements els = doc.select("bevorzugtername");
		if (els.size() == 1) {
			s.Name = els.get(0).html();
		}

		Elements identE = doc.select("unterkapitel[drnr=0100]");

		// get CAS
		els = identE.select("casnr");
		if (els.size() >= 1) {
			s.CAS = els.get(0).html().trim().split(" ")[0];
		}

		// get ZVG
		els = identE.select("zvg");
		if (els.size() >= 1) {
			s.ZVG = els.get(0).html().trim().split(" ")[0];
		}

		// get EG/EC
		els = identE.select("egnr");
		if (els.size() >= 1) {
			s.EC = els.get(0).html().trim().split(" ")[0];
		}

		// get INDEX
		els = identE.select("indexnr");
		if (els.size() >= 1) {
			s.Index = els.get(0).html().trim().split(" ")[0];
		}

		// get Alias
		for (Element e : identE.select("span[class=aliasname]")) {
			s.Alias.add(e.html().trim());
		}

		Elements formelE = doc.select("unterkapitel[drnr=0400]");

		// get Sum Formula
		els = formelE.select("summenformel");
		if (els.size() == 1) {
			s.Sum = els.get(0).html().trim();
		}

		// get MOL
		els = formelE.select("table[class=feldmitlabel]");
		if (els.size() > 0) {
			els = els.select("td");
			if (els.size() > 0) {
				for (Element e : els) {
					if (e.html().contains("g/mol")) {
						String mol = e.html().replace("g/mol", "").trim();
						s.MOL = mol.replace(",", ".");
					}
				}
			}
		}

		Elements physicPropE = doc.select("hauptkapitel[drnr=0600]");

		// get Melting Point
		els = physicPropE.select("unterkapitel[drnr=0602]");
		els = els.select("table[class=feldmitlabel]");
		els = els.select("td");
		if (els.size() >= 2) {
			s.MeltingPoint = stripTempFields(els.get(1).html());
		}

		// get BoilingPoint
		els = physicPropE.select("unterkapitel[drnr=0603]");
		els = els.select("table[class=feldmitlabel]");
		els = els.select("td");
		if (els.size() >= 2) {
			s.BoilingPoint = stripTempFields(els.get(1).html());
		}

		// get Density
		els = physicPropE.select("unterkapitel[drnr=0604]");
		els = els.select("table[class=feldmitlabel]");
		els = els.select("td");
		if (els.size() >= 2) {
			s.Density = stripTempFields(els.get(1).html().replace("g/cm³", ""));
		}

		Elements roulesE = doc.select("unterkapitel[drnr=1303]");

		// get EU-GHS sgins
		els = roulesE.select("img");
		for (Element e : els) {
			String alt = e.attributes().get("alt");
			if (alt.length() != 0) {
				if (!s.Pictograms.contains(alt)) {
					s.Pictograms.add(alt);
				}
			}
		}

		els = roulesE.select("table[class=feldmitlabel]");
		for (Element e : els) {
			Elements tds = e.select("td");
			if (tds.size() >= 2) {
				if (tds.get(0).html().matches("[\\s\\S]+(Signalwort|Signal Word):[\\s\\S]+")) {
					s.Signalword = tds.get(1).html().replace("\"", "").trim();
				}
			}
		}

		// get H/P phrases
		els = roulesE.select("table[class=block]");
		for (Element e : els) {
			Elements tds = e.select("td");
			if (tds.size() >= 2) {
				if (tds.get(0).html().matches("[\\s\\S]+(H-Sätze|H-phrases):[\\s\\S]+")) {
					String all = tds.get(1).html().replace("\"", "").trim();
					for (String item : all.split("<br>")) {
						if (item.contains("</verstecktercode>")) {
							item = item.split("</verstecktercode>")[1];
						}
						if (item.trim().length() != 0) {
							if (!s.HCodes.contains(item.trim())) {
								s.HCodes.add(item.trim());
							}
							item = stripPHphrase(item);
							if (item.length() != 0 && !s.HCodesShort.contains(item)) {
								s.HCodesShort.add(item);
							}
						}
					}
				} else if (tds.get(0).html().matches("[\\s\\S]+(P-Sätze|P-phrases):[\\s\\S]+")) {
					String all = tds.get(1).html().replace("\"", "").trim();
					for (String item : all.split("<br>")) {
						if (item.contains("</verstecktercode>")) {
							item = item.split("</verstecktercode>")[1];
						}
						if (item.trim().length() != 0) {
							if (!s.PCodes.contains(item.trim())) {
								s.PCodes.add(item.trim());
							}
							item = stripPHphrase(item);
							if (item.length() != 0 && !s.PCodesShort.contains(item)) {
								s.PCodesShort.add(item);
							}
						}
					}
				}
			}
		}

		// get WGK
		els = doc.select("hauptkapitel[drnr=1100]").select("unterkapitel[drnr=1106]");
		els = els.select("table[class=block]");
		for (Element e : els.select("td")) {
			if (e.html().matches("^WGK \\d[\\s\\S]+")) {
				int wgk = Integer.parseInt(e.html().substring(4, 5));
				if (s.WGK < wgk) {
					s.WGK = wgk;
				}
			}
		}

		return s;
	}

	/*
	 * Helpers...
	 */

	private static String stripPHphrase(String raw) {
		raw = raw.split(":")[0].trim();
		if (raw.length() != 0 && raw.matches("^(H|P)\\d\\d\\d(\\+(H|P)\\d\\d\\d)*$")) {
			return raw;
		}
		return "";
	}

	private static String getZVGfromName(String name) {
		List<SubstanceShort> s = new GESTIS().Search(name, true);
		if (s.size() == 0) {
			return "";
		}
		String result = s.get(0).ZVG;
		while (result.length() < 6) {
			result = "0" + result;
		}
		return result;
	}

	private static String stripTempFields(String raw) {
		return raw.replace(",", ".").replace(" ", "").replace("°C", "").replace("ca.", "");
	}
}
