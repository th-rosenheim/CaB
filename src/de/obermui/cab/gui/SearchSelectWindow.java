package de.obermui.cab.gui;

import de.obermui.cab.clients.GESTIS;
import de.obermui.cab.models.SafetyDataSheet;
import de.obermui.cab.models.Substance;
import de.obermui.cab.models.SubstanceShort;

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

	private List<String> selected;

	private List<SubstanceShort> l_searchResult;

	private DefaultListModel listModel;
	private JList jlist_substances;
	private JPanel SearchSelectPanel;
	private JComboBox cb_selected;
	private JLabel l_selected;

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
		listModel = new DefaultListModel<>();
		jlist_substances.setModel(listModel);

		selected = new ArrayList<>();
		if (Ctx.sheet == null) {
			Ctx.sheet = new SafetyDataSheet();
		}
		l_searchResult = new ArrayList<>();

		fm.setContentPane(SearchSelectPanel);
		fm.setVisible(true);
	}

	public void searchButtonPressed() {
		if (tf_search.getText().length() == 0 || tf_search.getText().equals("Search")) {
			Dialogs.infoBox(this.fm, "Kein Suchbegriff eingegeben", "Error: no keyword");
			return;
		}
		List<SubstanceShort> result = GESTIS.Search(tf_search.getText(), false);
		listModel.clear();
		l_searchResult.clear();
		l_searchResult.addAll(result);
		for (SubstanceShort s : result) {
			listModel.addElement("CAS: " + s.CAS + "; Name: " + s.Name);
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
			Ctx.editSheet.loadSheet(Ctx.sheet);
		}
		fm.setVisible(false);
		Ctx.editSheet.setVisible(true);
	}

	public void addSelected() {
		// check if all needed values are given
		// check if item in list was selected
		if (jlist_substances.getSelectedIndex() < 0) {
			Dialogs.infoBox(this.fm, "Bitte einen Stoff in der Liste Auswählen", "Error: no selected item");
			return;
		}

		// get selected substance
		SubstanceShort slectedSub = l_searchResult.get(jlist_substances.getSelectedIndex());

		// check if it was already added
		if (this.selected.contains(slectedSub.Name)) {
			Dialogs.infoBox(this.fm, "Bereits hinzugefügt", "Error: dublicate");
			return;
		}

		// an amount has to be specified
		String amount = tf_amount.getText();
		if (amount.length() == 0) {
			Dialogs.infoBox(this.fm, "Bitte Mänge angeben", "Error: no amount set");
			return;
		}

		// get the whole substance data from GESTIS
		Substance sub = GESTIS.getSubstance(slectedSub.Name, slectedSub.ZVG);
		if (sub.CAS == null || sub.CAS.length() == 0) {
			Dialogs.infoBox(this.fm, "Ein Fehler trat beim laden der GESTIS daten auf", "Error: gestis return unexpected");
			return;
		}

		// add Name to selected list to be able to check if it already was added
		this.selected.add(slectedSub.Name);
		// add name and amount into check box
		this.cb_selected.addItem(slectedSub.Name + " (" + amount + ")");
		// set focus to new item in check box
		this.cb_selected.setSelectedIndex(this.cb_selected.getItemCount() - 1);

		sub.Amount = amount;
		// finally add substance to sheet
		Ctx.sheet.addSubstance(sub);
	}

	// clean all lists objects etc
	public boolean clean() {
		listModel.clear();
		tf_search.setText("");
		selected.clear();
		return true;
	}

	public void setVisible(boolean b) {
		this.fm.setVisible(b);
	}

}
