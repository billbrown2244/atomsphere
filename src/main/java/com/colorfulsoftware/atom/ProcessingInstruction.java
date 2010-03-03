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
package com.colorfulsoftware.atom;

import java.io.Serializable;

/**
 * @author Bill Brown
 *
 */
public class ProcessingInstruction implements Serializable {
	private final String target;
	private final String data;
	private static final long serialVersionUID = -4261298860522801834L;

	ProcessingInstruction(String target, String data) {
		this.target = target;
		this.data = data;
	}

	/**
	 * @return the target of the processing instruction
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @return the processing instruction data.
	 */
	public String getData() {
		return data;
	}
}
