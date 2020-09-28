package de.obermui.cabr2.clients;

import de.obermui.cabr2.models.Substance;
import de.obermui.cabr2.models.SubstanceShort;

import java.util.List;

public interface Client {
	// GetSubstance return Substance based on ZVG and have a fallback to name
	Substance GetSubstance(String name, String ZVG);

	// Search return list of Substances (SubstanceShort) based on a keyword
	// if exact is true it will look only for exact matches
	List<SubstanceShort> Search(String keyword, boolean exact);

	// SimpleSearch works similar to Search but only return names of substances in a string list
	String[] SimpleSearch(String keyword, boolean exact);
}
