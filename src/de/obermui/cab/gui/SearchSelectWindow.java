package de.obermui.cab.gui;

import de.obermui.cab.clients.GESTIS;
import de.obermui.cab.models.SafetyDataSheet;
import de.obermui.cab.models.Substance;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static de.obermui.cab.gui.helper.getIcon;
import static de.obermui.cab.intern.Const.*;

public class SearchSelectWindow implements ActionListener {
	private JFrame fm;
	private JTextField tf_search;
	private JButton bt_search;
	private JButton bt_cancel;
	private JTextField tf_amount;
	private JButton bt_add;
	private JButton bt_next;

	private JLabel l_selected;
	private List<String> selected;

	private DefaultListModel listModel;
	private JList jlist_substances;
	private JPanel SearchSelectPanel;

	private ctx Ctx;

	public SearchSelectWindow(ctx c) {
		this.Ctx = c;

		//initial Form fm_main
		this.fm = new JFrame(AppName);
		fm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fm.setIconImage(getIcon().getImage());
		fm.setSize(AppStartWidth, AppStartHeight);
		fm.setLayout(null);
		fm.setLocationRelativeTo(null);

		bt_cancel.addActionListener(this);
		bt_next.addActionListener(this);
		bt_search.addActionListener(this);
		bt_add.addActionListener(this);
		tf_search.addActionListener(this);

		//initial Button jlist_substances & listModel
		listModel = new DefaultListModel();
		jlist_substances.setModel(listModel);

		selected = new ArrayList<>();
		if (Ctx.sheet == null) {
			Ctx.sheet = new SafetyDataSheet();
		}

		fm.setContentPane(SearchSelectPanel);
		fm.setVisible(true);
	}

	public void searchButtonPressed() {
		if (tf_search.getText().length() == 0 || tf_search.getText().equals("Search")) {
			Dialogs.infoBox("Kein Suchbegriff eingegeben", "Error: no keyword");
			return;
		}
		String[] result = GESTIS.SimpleSearch(tf_search.getText(), false);
		listModel.clear();
		for (String s : result) {
			listModel.addElement(s);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.bt_cancel) {
			this.clean();
			Ctx.mainWindow.setVisible(true);
			this.setVisible(false);
		} else if (e.getSource() == this.bt_search || e.getSource() == this.tf_search) {
			searchButtonPressed();
		} else if (e.getSource() == this.bt_add) {
			addSelected();
		} else if (e.getSource() == this.bt_next) {
			next();
		} else {
			System.out.println("Action found: " + e.toString());
		}
	}

	public void next() {
		if (Ctx.editSheet == null) {
			Ctx.editSheet = new EditSheetWindow(Ctx);
		} else {
			Ctx.editSheet.clean();
		}
		fm.setVisible(false);
		Ctx.editSheet.setVisible(true);
	}

	public void addSelected() {
		if (jlist_substances.getSelectedValue() == null) {
			Dialogs.infoBox("Bitte einen Stoff in der Liste Auswählen", "Error: no selected item");
			return;
		}
		String selected = jlist_substances.getSelectedValue().toString();
		if (this.selected.contains(selected)) {
			Dialogs.infoBox("Bereits hinzugefügt", "Error: dublicate");
			return;
		}
		String amount = tf_amount.getText();
		if (amount.length() == 0) {
			Dialogs.infoBox("Bitte Mänge angeben", "Error: no amount set");
			return;
		}
		this.selected.add(selected);
		this.l_selected.setText(l_selected.getText() + selected + "; ");

		Substance sub = GESTIS.getSubstance(selected);
		if (sub.CAS == null || sub.CAS.length() == 0) {
			Dialogs.infoBox("Ein Fehler trat beim laden der GESTIS daten auf", "Error: gestis return unexpected");
			return;
		}
		sub.Amount = amount;
		Ctx.sheet.addSubstance(sub);
	}

	// clean all lists objects etc
	public boolean clean() {
		listModel.clear();
		l_selected.setText("");
		tf_search.setText("Search");
		selected.clear();
		return true;
	}

	public void setVisible(boolean b) {
		this.fm.setVisible(b);
	}

}
