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
package net.sourceforge.veditor.editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Composite;

import net.sourceforge.veditor.document.VerilogDocumentProvider;
import net.sourceforge.veditor.editor.scanner.HdlPartitionScanner;

public class VerilogEditor extends HdlEditor {
	public VerilogEditor() {
		super();
		setDocumentProvider(new VerilogDocumentProvider());
		setSourceViewerConfiguration(HdlSourceViewerConfiguration
				.createForVerilog(this));
		OutlineLabelProvider = new VerilogOutlineLabelProvider();
		TreeContentProvider = new VerilogHierarchyProvider();
	}

	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		if (getViewer() instanceof SourceViewer) {
			SourceViewer viewer = (SourceViewer) getViewer();

			viewer.prependAutoEditStrategy(new CommentExtender(),
					HdlPartitionScanner.MULTI_LINE_COMMENT);
			viewer.prependAutoEditStrategy(new CommentExtender(),
					HdlPartitionScanner.DOXYGEN_MULTI_LINE_COMMENT);
		}
	}

	private class CommentExtender implements IAutoEditStrategy {
		public void customizeDocumentCommand(IDocument document,
				DocumentCommand command) {
			try {
				int offset = command.offset;
				int lineOffset = document.getLineOffset(document
						.getLineOfOffset(offset));
				String str = document.get(lineOffset, offset - lineOffset);
				if (command.text.startsWith("\n") || command.text.startsWith("\r")) {
					command.text = TextUtilities.getDefaultLineDelimiter(document)
							+ getIndentOfLine(str) + "* ";
				}
			} catch (BadLocationException e) {
			}
		}
		
		private String getIndentOfLine(String str) {
			for(int i = 0; i < str.length(); i++) {
				if (Character.isWhitespace(str.charAt(i)) == false) {
					if (str.charAt(i) == '/')
						return str.substring(0, i) + " ";
					else
						return str.substring(0, i);
				}
			}
			return str;
		}
	}
}
