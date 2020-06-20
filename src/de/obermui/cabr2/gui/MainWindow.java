package de.obermui.cabr2.gui;

import de.obermui.cabr2.intern.Beryllium;
import de.obermui.cabr2.intern.Const;
import de.obermui.cabr2.models.SafetyDataSheet;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static de.obermui.cabr2.gui.helper.getIcon;
import static de.obermui.cabr2.intern.Const.*;

public class MainWindow implements ActionListener {
	private JButton bt_new;
	private JButton bt_exit;
	private JButton bt_importBe;

	private JMenuBar mb_menu;
	private JMenuItem mi_about_version;

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
		bt_importBe.addActionListener(this);
		bt_new.addActionListener(this);

		//add a Menu
		mb_menu = new JMenuBar();

		// create & init menu & menu items
		JMenu m_about = new JMenu("About");
		mi_about_version = new JMenuItem("Version");
		mi_about_version.addActionListener(this);
		m_about.add(mi_about_version);

		// put menu things together
		fm_main.setJMenuBar(mb_menu);
		mb_menu.add(Box.createHorizontalGlue());
		mb_menu.add(m_about);

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
		} else if (e.getSource() == this.bt_importBe) {
			importBe();
		} else if (e.getSource() == this.mi_about_version) {
			Dialogs.infoBox(this.fm_main, "Version: " + AppVersion, "Version");
		} else {
			System.out.println(e.getSource().toString());
		}
	}

	public void importBe() {
		if (Ctx.editSheet == null) {
			Ctx.editSheet = new EditSheetWindow(Ctx);
		}

		String filePath = Dialogs.open(Ctx, ".be");
		// if nothing selected ...
		if (filePath.length() == 0) return;

		SafetyDataSheet sheet = Beryllium.Import(filePath);
		if (sheet == null) {
			Dialogs.infoBox(this.fm_main, "Error could not import Beryllium file", "Error: import Be");
			return;
		}

		Ctx.editSheet.clean();
		Ctx.sheet = sheet;
		Ctx.editSheet.loadSheet(sheet);
		Ctx.editSheet.setVisible(true);
		this.fm_main.setVisible(false);
	}
}
