/*******************************************************************************
 * Copyright (c) 2004, 2006 KOBAYASHI Tadashi and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    KOBAYASHI Tadashi - initial API and implementation
 *******************************************************************************/
package net.sourceforge.veditor;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

public class VerilogPreferenceInitializer extends AbstractPreferenceInitializer
{
	public VerilogPreferenceInitializer()
	{
		super();
	}

	/**
	 * Error parser
	 * <PRE>
	 *  version number for future extension 
	 *  (
	 *    compiler name
	 *    error matching pattern
	 *    warning matching pattern
	 *    info matching pattern
	 *  )*
	 * </PRE>
	 */
	private static final String DEFAULT_ERROR_PARSER_REGEX =
		"1"
		+ "\n" + "ModelSim"
		+ "\n" + "[#|\\*].*Error: ([^\\(]*)\\(([0-9]*)\\): (.*)"		
		+ "\n" + "[#|\\*].*Warning: ([^\\(]*)\\(([0-9]*)\\): (.*)"		
		+ "\n" + ""
		+ "\n" + "Cver"
		+ "\n" + "\\*\\*(.*)\\(([0-9]+)\\) ERROR\\*\\* (.*)"
		+ "\n" + "\\*\\*(.*)\\(([0-9]+)\\) WARN\\*\\* (.*)"
		+ "\n" + "--(.*)\\(([0-9]+)\\) INFORM-- (.*)" 
		+ "\n" + "Icarus Verilog"
		+ "\n" + "(.*):([0-9]+): [a-z ]*error: (.*)"
		+ "\n" + "(.*):([0-9]+): warning: (.*)"
		+ "\n" + ""
		+ "\n" + "FreeHDL"
		+ "\n" + "(.*):([0-9]+): error: (.*)" 
		+ "\n" + "(.*):([0-9]+): warning: (.*)" 
		+ "\n" + ""	
		+ "\n";
	public static final int NUM_OF_DEFAULT_ERROR_PARSERS = 4;
	
	public void initializeDefaultPreferences()
	{
		Preferences preferences = VerilogPlugin.getPlugin()
				.getPluginPreferences();

		setDefaultAttr(preferences, "DoxygenComment", "64,64,128");
		setDefaultAttr(preferences, "SingleLineComment", "00,128,128");
		setDefaultAttr(preferences, "MultiLineComment", "00,128,128");
		setDefaultAttr(preferences, "String", "00,00,128");
		setDefaultAttr(preferences, "Default", "00,00,00");
		setDefaultAttr(preferences, "KeyWord", "128,00,128", true);

		preferences.setDefault("Outline.Comment", true);
		preferences.setDefault("ContentAssist.ModuleParameter", false);
		preferences.setDefault("Compile.command", "iverilog -tnull -y . -Wall");
		
		preferences.setDefault("ErrorParser", DEFAULT_ERROR_PARSER_REGEX);
		
		preferences.setDefault("Style.indent","Tab");
		preferences.setDefault("Style.indentSize", "4");
		preferences.setDefault("Style.noSpaceInBracket", true);
		setDefaultStyleSpace(preferences);
	}
	
	private void setDefaultAttr(Preferences preferences, String name,
			String color)
	{
		setDefaultAttr(preferences, name, color, false);
	}

	private void setDefaultAttr(Preferences preferences, String name,
			String color, boolean bold)
	{
		preferences.setDefault("Color." + name, color);
		preferences.setDefault("Bold." + name, bold);
		preferences.setDefault("Italic." + name, false);
	}
	
	private void setDefaultStyleSpace(Preferences preferences)
	{
		Object values[][] = new Object[][] {
				{ "Style.spaceBeforeOperator2", true },
				{ "Style.spaceAfterOperator2", true },
				{ "Style.spaceBeforeOperator1", true },
				{ "Style.spaceAfterOperator1", false },
				{ "Style.spaceBeforeComma", false },
				{ "Style.spaceAfterComma", true },
				{ "Style.spaceBeforeSemicolon", false },
				{ "Style.spaceBeforeOpenParen", false },
				{ "Style.spaceAfterOpenParen", false },
				{ "Style.spaceBeforeCloseParen", false },
				{ "Style.spaceBeforeOpenBracket", false },
				{ "Style.spaceAfterOpenBracket", false },
				{ "Style.spaceBeforeCloseBracket", false },
				{ "Style.spaceBeforeOpenBrace", false },
				{ "Style.spaceAfterOpenBrace", false },
				{ "Style.spaceBeforeCloseBrace", false },
				{ "Style.spaceBeforeCaseColon", false },
				{ "Style.spaceAfterCaseColon", true },
				{ "Style.spaceAfterIf", true },
				{ "Style.spaceAfterFor", true },
				{ "Style.spaceAfterWhile", true },
				{ "Style.spaceAfterRepeat", true } };
		for (int i = 0; i < values.length; i++)
		{
			boolean flag = ((Boolean)values[i][1]).booleanValue();
			preferences.setDefault(values[i][0].toString(), flag);
		}
	}
}


