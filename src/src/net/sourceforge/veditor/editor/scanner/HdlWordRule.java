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
package net.sourceforge.veditor.editor.scanner;

import org.eclipse.jface.text.rules.WordRule;

import net.sourceforge.veditor.document.HdlDocument;
import net.sourceforge.veditor.document.IOutlineListener;
import net.sourceforge.veditor.editor.ColorManager;
import net.sourceforge.veditor.editor.HdlTextAttribute;
import net.sourceforge.veditor.editor.scanner.HdlScanner;
import net.sourceforge.veditor.parser.HdlParserException;
import net.sourceforge.veditor.parser.OutlineContainer;
import net.sourceforge.veditor.parser.OutlineElement;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

public class HdlWordRule extends WordRule {
	protected IToken input;
	protected IToken output;
	protected IToken inout;
	protected IToken signal;
	protected IToken constant;
	protected IToken reg;
	protected IToken localparam;
	
	private OutlineListener listener = null;
	private IFile file = null;
	private OutlineElement[] modules = null;

	public HdlWordRule(IWordDetector detector, IToken defaultToken, ColorManager manager) {
		super(detector, defaultToken);
		input = new Token(HdlTextAttribute.INPUT.getTextAttribute(manager));
		output = new Token(HdlTextAttribute.OUTPUT.getTextAttribute(manager));
		inout = new Token(HdlTextAttribute.INOUT.getTextAttribute(manager));
		signal = new Token(HdlTextAttribute.SIGNAL.getTextAttribute(manager));
		constant = new Token(HdlTextAttribute.CONSTANT.getTextAttribute(manager));
		reg = new Token(HdlTextAttribute.REG.getTextAttribute(manager));
		localparam = new Token(HdlTextAttribute.LOCALPARAM.getTextAttribute(manager));
	}

	@Override
	public IToken evaluate(ICharacterScanner iscanner) {
		if (!(iscanner instanceof HdlScanner)) {
			return super.evaluate(iscanner);
		}

		HdlScanner scanner = (HdlScanner) iscanner;
		HdlDocument doc = scanner.getHdlDocument();
		if (doc == null) {
			return super.evaluate(iscanner);
		}

		if (file == null || !file.equals(doc.getFile())) {
			try {
				OutlineContainer container = doc.getOutlineContainer(true);
				file = doc.getFile();
				modules = container.getTopLevelElements();
				if (listener == null) {
					listener = new OutlineListener();
					doc.addOutlineListener(listener);
				}
			} catch (HdlParserException e) {
				file = null;
			}
		}

		IToken token = super.evaluate(scanner);
		if (file != null && scanner.getTokenLength() > 0) {
			int start = scanner.getTokenOffset();
			int len = scanner.getTokenLength();
			try {
				int line = doc.getLineOfOffset(start);
				for (OutlineElement module : modules) {
					if (module.getStartingLine() <= line && line <= module.getEndingLine()) {
						String str = doc.get(start, len);
						IToken t = findToken(module, str);
						if (t != null) {
							return t;
						}
					}
				}
			} catch (BadLocationException e) {
			}
		}
		return token;
	}

	private IToken findToken(OutlineElement module, String name) {
		OutlineElement[] variables = module.getChildren();
		for (OutlineElement variable : variables) {
			if (name.equals(variable.getName())) {
				String[] types = variable.getType().split("#");
				if (types.length >= 2) {
					return getTokenByType(types);
				}
			}
		}
		return null;
	}
	
	protected IToken getTokenByType(String[] types) {
		return null;
	}

	/**
	 * reload outline elements when outline database is updated
	 */
	private class OutlineListener implements IOutlineListener {
		@Override
		public void outlineChanged() {
			file = null;
		}
	}
}
