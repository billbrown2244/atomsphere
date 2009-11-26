/**
 * Copyright (C) 2009 William R. Brown <wbrown@colorfulsoftware.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/* Change History:
 *  2008-03-16 wbrown - made class to share text fied for email, uri and name.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;

/**
 * This class represents a text object.
 * 
 * @author Bill Brown
 * 
 */
class AtomPlainText implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -601318756294822761L;
	private final String text;

	AtomPlainText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
