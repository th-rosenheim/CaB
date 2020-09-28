package de.obermui.cabr2.gui;

import de.obermui.cabr2.clients.Client;
import de.obermui.cabr2.models.SafetyDataSheet;

import javax.swing.*;

public class ctx {
	protected JFrame mainWindow;
	protected SearchSelectWindow searchSelect;
	protected SheetInfoWindow sheetInfo;
	protected EditSheetWindow editSheet;

	protected SafetyDataSheet sheet;

	protected String lastSavedFileDir;

	protected Client client;
}
