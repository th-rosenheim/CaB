package de.obermui.cab.gui;

import de.obermui.cab.models.SafetyDataSheet;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static de.obermui.cab.gui.helper.getIcon;
import static de.obermui.cab.intern.Const.*;

public class MainWindow implements ActionListener {
	private JFrame fm_main;
	private JButton bt_new;
	private JButton bt_exit;

	private ctx Ctx;

	public void start() {

		this.Ctx = new ctx();

		//initial Form fm_main
		this.fm_main = new JFrame(AppName);
		fm_main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fm_main.setIconImage(getIcon().getImage());
		fm_main.setSize(AppStartWidth, AppStartHeight);
		fm_main.setLayout(null);
		Ctx.mainWindow = fm_main;

		// Frame appear centered
		fm_main.setLocationRelativeTo(null);

		//initial Button bt_exit
		bt_exit = new JButton();
		bt_exit.setBounds(AppStartWidth - 250, AppStartHeight - 100, 200, 40);
		bt_exit.setText("EXIT");
		bt_exit.setVisible(true);
		bt_exit.addActionListener(this);

		//initial Button bt_new
		bt_new = new JButton();
		bt_new.setBounds(AppStartWidth / 2 - 100, AppStartHeight / 2 - 20, 200, 40);
		bt_new.setText("Neues Blatt Erstellen");
		bt_new.setVisible(true);
		bt_new.addActionListener(this);

		//Add objects to Form fm_main
		fm_main.add(bt_exit);
		fm_main.add(bt_new);

		fm_main.setVisible(true);
	}

	public void setVisible(boolean b) {
		fm_main.setVisible(b);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.bt_exit) {
			System.exit(0);
		} else if (e.getSource() == this.bt_new) {
			Ctx.sheet = new SafetyDataSheet();
			fm_main.setVisible(false);
			if (Ctx.searchSelect == null) {
				Ctx.searchSelect = new SearchSelectWindow(Ctx);
			} else {
				Ctx.searchSelect.clean();
				Ctx.searchSelect.setVisible(true);
			}
		}
	}
}
