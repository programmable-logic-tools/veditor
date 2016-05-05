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

package net.sourceforge.veditor.editor.scanner.verilog;

import net.sourceforge.veditor.editor.ColorManager;
import net.sourceforge.veditor.editor.scanner.HdlWordRule;

import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;

public class VerilogWordRule extends HdlWordRule {

	public VerilogWordRule(IWordDetector detector, IToken defaultToken, ColorManager manager) {
		super(detector, defaultToken, manager);
	}

	protected IToken getTokenByType(String[] types) {
		if (types[0].equals("port")) {
			if (types[1].equals("input")) {
				return input;
			} else if (types[1].equals("output")) {
				return output;
			} else if (types[1].equals("inout")) {
				return inout;
			}
		} else if (types[0].equals("variable")) {
			if (types[1].equals("reg")) {
				return reg;
			} else if (types[1].equals("wire")) {
				return signal;
			}
		} else if (types[0].equals("parameter")) {
			return constant;
		} else if (types[0].equals("localparam")) {
			return localparam;
		}
		return null;
	}
}
