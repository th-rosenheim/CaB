package de.obermui.cab.gui;

import de.obermui.cab.models.SafetyDataSheet;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static de.obermui.cab.gui.helper.getIcon;
import static de.obermui.cab.intern.Const.*;

public class MainWindow implements ActionListener {
	private JButton bt_new;
	private JButton bt_exit;
	private JPanel panel1;
	private JFrame fm_main;
	private ctx Ctx;

	public MainWindow() {
		this.Ctx = new ctx();

		//initial Form fm_main
		this.fm_main = new JFrame(AppName);
		fm_main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fm_main.setIconImage(getIcon().getImage());
		fm_main.setSize(AppStartWidth, AppStartHeight);
		fm_main.setLayout(null);
		fm_main.setLocationRelativeTo(null);
		fm_main.setContentPane(panel1);

		ImageIcon icon = new ImageIcon(helper.getIcon().getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));
		bt_new.setIcon(icon);

		bt_exit.addActionListener(this);
		bt_new.addActionListener(this);

		Ctx.mainWindow = fm_main;
		fm_main.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.bt_exit) {
			System.exit(0);
		} else if (e.getSource() == this.bt_new) {
			Ctx.sheet = new SafetyDataSheet();
			fm_main.setVisible(false);
			if (Ctx.sheetInfo == null) {
				Ctx.sheetInfo = new SheetInfoWindow(Ctx);
			} else {
				Ctx.sheetInfo.clean();
				Ctx.sheetInfo.loadTHdefault();
				Ctx.sheetInfo.setVisible(true);
			}
		}
	}
}
