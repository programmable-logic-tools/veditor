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
package net.sourceforge.veditor.document;

/**
 * This is called when outline database is updated.
 * The outline database is updated when a file is opened or saved.
 */
public interface IOutlineListener {
	void outlineChanged();
}
