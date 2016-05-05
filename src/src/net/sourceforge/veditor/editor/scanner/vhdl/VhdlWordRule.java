/*******************************************************************************
 * Copyright (c) 2004, 2016 KOBAYASHI Tadashi and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    KOBAYASHI Tadashi - initial API and implementation
 *******************************************************************************/

package net.sourceforge.veditor.editor.scanner.vhdl;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;

import net.sourceforge.veditor.editor.ColorManager;
import net.sourceforge.veditor.editor.scanner.HdlWordRule;

public class VhdlWordRule extends HdlWordRule {

	public VhdlWordRule(IWordDetector detector, IToken defaultToken, ColorManager manager) {
		super(detector, defaultToken, manager);
	}

	protected IToken getTokenByType(String[] types) {
		if (types[0].equals("port")) {
			if (types[1].equalsIgnoreCase("in")) {
				return input;
			} else if (types[1].equalsIgnoreCase("out")) {
				return output;
			} else if (types[1].equalsIgnoreCase("inout")) {
				return inout;
			}
		} else if (types[0].equalsIgnoreCase("constant")) {
			return constant;
		} else if (types[0].equalsIgnoreCase("signal")) {
			return signal;
		}
		return null;
	}
}
