package de.obermui.cab.intern;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class html2pdf {
	public static void test() {

		try {
			File pdfDest = new File("output.pdf");

			ConverterProperties converterProperties = new ConverterProperties();
			converterProperties.setCharset("UTF-8");

			InputStream streamIn = html2pdf.class.getResourceAsStream("/res/Templates/SafetyDataSheet.html");

			HtmlConverter.convertToPdf(streamIn, new FileOutputStream(pdfDest), converterProperties);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void html2pdf(String input, String outputFile) {

		try {
			File pdfDest = new File(outputFile);

			ConverterProperties converterProperties = new ConverterProperties();
			converterProperties.setCharset("UTF-8");

			HtmlConverter.convertToPdf(input, new FileOutputStream(pdfDest), converterProperties);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
