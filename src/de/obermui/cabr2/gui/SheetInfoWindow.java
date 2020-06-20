package de.obermui.cabr2.gui;

import de.obermui.cabr2.models.SafetyDataSheet;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static de.obermui.cabr2.gui.helper.getIcon;
import static de.obermui.cabr2.intern.Const.*;

public class SheetInfoWindow implements ActionListener {
	private JFrame fm_sheet_info;

	private JButton bt_next;
	private JButton bt_cancel;
	private JTextField tf_title;
	private JTextField tf_org;
	private JTextField tf_course;
	private JTextField tf_PersonalName;
	private JTextField tf_PersonalPlace;
	private JTextField tf_PersonalAssistant;
	private JPanel SheetInfoPanel;
	private JTextField tf_task;

	private ctx Ctx;

	public SheetInfoWindow(ctx c) {
		this.Ctx = c;

		this.fm_sheet_info = new JFrame(AppName);
		fm_sheet_info.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fm_sheet_info.setIconImage(getIcon().getImage());
		fm_sheet_info.setSize(AppStartWidth, AppStartHeight);
		fm_sheet_info.setLayout(null);
		fm_sheet_info.setLocationRelativeTo(null);
		fm_sheet_info.setContentPane(SheetInfoPanel);

		bt_next.addActionListener(this);
		bt_cancel.addActionListener(this);

		fm_sheet_info.setVisible(true);

		if (Ctx.sheet == null) {
			Ctx.sheet = new SafetyDataSheet();
		}
		loadTHdefault();

	}

	private void loadSheet(SafetyDataSheet s) {
		tf_title.setText(s.Title);
		tf_org.setText(s.Org);
		tf_course.setText(s.Course);
		tf_PersonalName.setText(s.Personal.Name);
		tf_PersonalPlace.setText(s.Personal.Place);
		tf_PersonalAssistant.setText(s.Personal.Assistant);
		tf_task.setText(s.Task);
	}

	public void setVisible(boolean b) {
		fm_sheet_info.setVisible(b);
	}

	private void next() {
		Ctx.sheet.Title = tf_title.getText();
		Ctx.sheet.Org = tf_org.getText();
		Ctx.sheet.Course = tf_course.getText();
		Ctx.sheet.Personal.Name = tf_PersonalName.getText();
		Ctx.sheet.Personal.Place = tf_PersonalPlace.getText();
		Ctx.sheet.Personal.Assistant = tf_PersonalAssistant.getText();
		Ctx.sheet.Task = tf_task.getText();
		clean();

		if (Ctx.searchSelect == null) {
			Ctx.searchSelect = new SearchSelectWindow(Ctx);
		} else {
			Ctx.searchSelect.clean();
		}
		Ctx.searchSelect.setVisible(true);
		fm_sheet_info.setVisible(false);
	}

	protected void clean() {
		tf_title.setText("");
		tf_org.setText("");
		tf_course.setText("");
		tf_PersonalName.setText("");
		tf_PersonalPlace.setText("");
		tf_PersonalAssistant.setText("");
		tf_task.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.bt_next) {
			next();
		} else if (e.getSource() == this.bt_cancel) {
			clean();
			fm_sheet_info.setVisible(false);
			Ctx.mainWindow.setVisible(true);
		}
	}

	protected void loadTHdefault() {
		Ctx.sheet.Title = "Betriebsanweisungen nach EG Nr. 1272/2008";
		Ctx.sheet.Org = "für chemische Laboratorien des Campus Burghausen";
		Ctx.sheet.Course = "Praktikum Anorganische Chemie";
		Ctx.sheet.Task = "Leere Substanzdaten";
		Ctx.sheet.AdditionalDanger = "Bei anhaltender Augenreizung ärztlichen Rat einholen. Funkenerzeugung und elektrische Aufladung vermeiden.\n" +
			"Hautkontakt mit der Nickellösung vermeiden und auf verschüttete Tropfen achten!";
		Ctx.sheet.AdditionalSafetyRules = "Hautschutz und Schutzkleidung mit Schutzbrille tragen.";
		Ctx.sheet.AdditionalFirstAid = "<b>Nach Einatmen:</b> An die frische Luft bringen. Sofort Arzt hinzuziehen.\n" +
			"<b>Nach Hautkontakt:</b> Sofort mit Wasser abwaschen. Kontaminierte Kleidung entfernen. Sofort Arzt hinzuziehen.\n" +
			"<b>Nach Verschlucken:</b> Mund mit Wasser spülen, Wasser trinken lassen. Kein Erbrechen auslösen. Nur bei Bewusstsein!\n" +
			"<b>Nach Augenkontakt:</b> Mit Wasser spülen. Falls vorhanden nach Möglichkeit Kontaktlinsen entfernen und weiter spülen. Sofort Augenarzt hinzuziehen.";

		loadSheet(Ctx.sheet);
	}
}
