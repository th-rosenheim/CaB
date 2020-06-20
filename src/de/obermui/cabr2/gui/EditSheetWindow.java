package de.obermui.cabr2.gui;

import de.obermui.cabr2.intern.Sheet2Html;
import de.obermui.cabr2.intern.html2pdf;
import de.obermui.cabr2.models.SafetyDataSheet;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import static de.obermui.cabr2.gui.helper.getIcon;
import static de.obermui.cabr2.intern.Const.*;

public class EditSheetWindow implements ActionListener {
	private JPanel panel1;
	private JTable table1;
	private JTextField tf_Title;
	private JTextField tf_course;
	private JTextField tf_org;
	private JTextField tf_PersonName;
	private JTextField tf_PersonPlatz;
	private JTextField tf_PersonAssistant;
	private JTextField tf_task;
	private JTabbedPane tabbedPane1;
	private JTextArea ta_p_phrases;
	private JTextArea ta_h_phrases;
	private JTextArea ta_additional_risks;
	private JTextArea ta_additional_safety_rules;
	private JTextArea ta_additional_first_aid_hints;
	private JTextArea ta_additional_disposal;
	private JButton bt_cancel;
	private JButton bt_export_pdf;
	private JButton bt_export_html;
	private JButton bt_back;
	private JFrame fm_edit;

	private ctx Ctx;

	public EditSheetWindow(ctx c) {
		this.Ctx = c;

		fm_edit = new JFrame(AppName);
		fm_edit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fm_edit.setIconImage(getIcon().getImage());
		fm_edit.setSize(AppStartWidth, AppStartHeight);
		fm_edit.setLayout(null);
		fm_edit.setLocationRelativeTo(null);

		fm_edit.setContentPane(panel1);

		// add Listener
		bt_cancel.addActionListener(this);
		bt_export_html.addActionListener(this);
		bt_export_pdf.addActionListener(this);
		bt_back.addActionListener(this);

		if (this.Ctx.sheet == null) {
			this.Ctx.sheet = new SafetyDataSheet();
		} else {
			loadSheet(this.Ctx.sheet);
		}

	}

	public void loadSheet(SafetyDataSheet s) {
		tf_Title.setText(s.Title);
		tf_org.setText(s.Org);
		tf_course.setText(s.Course);
		tf_PersonName.setText(s.Personal.Name);
		tf_PersonPlatz.setText(s.Personal.Place);
		tf_PersonAssistant.setText(s.Personal.Assistant);
		tf_task.setText(s.Task);
		ta_h_phrases.setText(String.join("\n", s.HCodes));
		ta_p_phrases.setText(String.join("\n", s.PCodes));

		ta_additional_risks.setText(s.AdditionalDanger);
		ta_additional_safety_rules.setText(s.AdditionalSafetyRules);
		ta_additional_first_aid_hints.setText(s.AdditionalFirstAid);
		ta_additional_disposal.setText(s.AdditionalWasteDisposal);
	}

	public void clean() {
		tf_Title.setText("");
		tf_org.setText("");
		tf_course.setText("");
		tf_PersonName.setText("");
		tf_PersonPlatz.setText("");
		tf_PersonAssistant.setText("");
		tf_task.setText("");
		ta_h_phrases.setText("");
		ta_p_phrases.setText("");

		ta_additional_risks.setText("");
		ta_additional_safety_rules.setText("");
		ta_additional_first_aid_hints.setText("");
		ta_additional_disposal.setText("");
	}

	public void setVisible(boolean b) {
		fm_edit.setVisible(b);
	}

	public void exportPDF() {
		syncBack();
		String outputPath = Dialogs.saveAs(Ctx, ".pdf");
		if (outputPath.length() == 0) return;
		html2pdf.html2pdf(Sheet2Html.Convert(Ctx.sheet, false), outputPath);
	}

	public void exportHTML() {
		syncBack();
		String outputPath = Dialogs.saveAs(Ctx, ".html");
		if (outputPath.length() == 0) return;

		if (!Sheet2Html.Export(Ctx.sheet, outputPath)) {
			Dialogs.infoBox(this.fm_edit, "Error on Export HTML", "Error");
		}
	}

	public void syncBack() {
		Ctx.sheet.Title = tf_Title.getText();
		Ctx.sheet.Org = tf_org.getText();
		Ctx.sheet.Course = tf_course.getText();
		Ctx.sheet.Personal.Name = tf_PersonName.getText();
		Ctx.sheet.Personal.Place = tf_PersonPlatz.getText();
		Ctx.sheet.Personal.Assistant = tf_PersonAssistant.getText();
		Ctx.sheet.Task = tf_task.getText();
		Ctx.sheet.HCodes.clear();
		Ctx.sheet.HCodes.addAll(Arrays.asList(ta_h_phrases.getText().split("\n")));
		Ctx.sheet.PCodes.clear();
		Ctx.sheet.PCodes.addAll(Arrays.asList(ta_p_phrases.getText().split("\n")));

		Ctx.sheet.AdditionalDanger = ta_additional_risks.getText();
		Ctx.sheet.AdditionalSafetyRules = ta_additional_safety_rules.getText();
		Ctx.sheet.AdditionalFirstAid = ta_additional_first_aid_hints.getText();
		Ctx.sheet.AdditionalWasteDisposal = ta_additional_disposal.getText();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.bt_cancel) {
			clean();
			Ctx.mainWindow.setVisible(true);
			fm_edit.setVisible(false);
		} else if (e.getSource() == this.bt_export_pdf) {
			exportPDF();
		} else if (e.getSource() == this.bt_export_html) {
			exportHTML();
		} else if (e.getSource() == this.bt_back) {
			syncBack();
			if (Ctx.searchSelect == null) {
				Ctx.searchSelect = new SearchSelectWindow(Ctx);
			}
			Ctx.searchSelect.setVisible(true);
			fm_edit.setVisible(false);
		} else {
			System.out.println("Action found: " + e.toString());
		}
	}
}
