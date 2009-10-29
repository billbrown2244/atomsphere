/**
 * Copyright (C) 2009 William R. Brown <wbrown@colorfulsoftware.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
/* Change History:
 *  2008-03-16 wbrown - Introduced to share between published and updated.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

class AtomDateConstruct implements Serializable {

	private static final long serialVersionUID = 4497374180937974L;

	private final List<Attribute> attributes;

	private final Date dateTime;

	private final String text;

	/*
	 * 
	 * @param dateTime the date
	 * 
	 * @param attributes the attributes for the date. the date formatted to
	 * [RFC3339]
	 * 
	 * Internet Date/Time Format
	 * 
	 * 
	 * The following profile of ISO 8601 [ISO8601] dates SHOULD be used in new
	 * protocols on the Internet. This is specified using the syntax description
	 * notation defined in [ABNF].
	 * 
	 * date-fullyear = 4DIGIT date-month = 2DIGIT ; 01-12 date-mday = 2DIGIT ;
	 * 01-28, 01-29, 01-30, 01-31 based on ; month/year time-hour = 2DIGIT ;
	 * 00-23 time-minute = 2DIGIT ; 00-59 time-second = 2DIGIT ; 00-58, 00-59,
	 * 00-60 based on leap second ; rules time-secfrac = "." 1*DIGIT
	 * time-numoffset = ("+" / "-") time-hour ":" time-minute time-offset = "Z"
	 * / time-numoffset
	 * 
	 * partial-time = time-hour ":" time-minute ":" time-second [time-secfrac]
	 * full-date = date-fullyear "-" date-month "-" date-mday full-time =
	 * partial-time time-offset
	 * 
	 * date-time = full-date "T" full-time
	 * 
	 * 
	 * @throws AtomSpecException if the date format is not valid.
	 */
	AtomDateConstruct(List<Attribute> attributes, String dateTime)
			throws AtomSpecException {
		if (attributes == null) {
			this.attributes = null;
		} else {
			this.attributes = new LinkedList<Attribute>();
			for (Attribute attr : attributes) {
				// check for unsupported attribute.
				if (!new AttributeSupport(attr).verify(this)) {
					throw new AtomSpecException("Unsupported attribute "
							+ attr.getName() + " for this Atom Date Construct.");
				}
				this.attributes.add(new Attribute(attr));
			}
		}

		// specification customization
		if (dateTime == null) {
			throw new AtomSpecException(
					"AtomDateConstruct Dates SHOULD NOT be null.");
		} else {

			Date local = null;
			String timeZoneOffset = null;

			int hours = (((TimeZone.getDefault().getRawOffset() / 1000) / 60) / 60);
			if (hours >= 0) {
				timeZoneOffset = TimeZone.getTimeZone("GMT" + "+" + hours)
						.getID().substring(3);
			} else {
				timeZoneOffset = TimeZone.getTimeZone(
						"GMT" + "-" + Math.abs(hours)).getID().substring(3);
			}
			// this is the preferred default format.
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd\'T\'HH:mm:ss.SS\'" + timeZoneOffset + "\'");

			boolean valid = true;
			try {
				// example: 2006-04-28T12:50:43.337-05:00
				local = sdf.parse(dateTime);
			} catch (Exception e1) {
				valid = false;
			}

			if (!valid) {
				try {
					// example: 2009-10-15T11:11:30.52Z
					sdf = new SimpleDateFormat(
							"yyyy-MM-dd\'T\'HH:mm:ss.SS\'Z\'");
					local = sdf.parse(dateTime);
					valid = true;
				} catch (Exception e2) {
					valid = false;
				}
			}

			if (!valid) {
				try {
					// example: 2009-10-15T11:11:30Z
					sdf = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss\'Z\'");
					local = sdf.parse(dateTime);
					valid = true;
				} catch (Exception e3) {
					valid = false;
				}
			}

			if (!valid) {
				try {
					// example: Mon Oct 19 10:52:18 CDT 2009
					// or: Tue Oct 20 02:48:09 GMT+10:00 2009
					sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
					local = sdf.parse(dateTime);
					valid = true;
				} catch (Exception e4) {
					valid = false;
				}
			}

			if (!valid) {
				throw new AtomSpecException(
						"error trying to create the date element with string: "
								+ dateTime);
			}

			;

			this.text = sdf.format(this.dateTime = new Date(local.getTime()));

		}
	}

	AtomDateConstruct(AtomDateConstruct atomDateConstruct) {
		this.attributes = atomDateConstruct.getAttributes();
		this.dateTime = atomDateConstruct.getDateTime();
		this.text = atomDateConstruct.getText();
	}

	/**
	 * 
	 * @return the date timestamp for this element.
	 */
	protected Date getDateTime() {
		return new Date(dateTime.getTime());
	}

	/**
	 * 
	 * @return the string formated version of the time for example
	 *         2006-04-28T12:50:43.337-05:00
	 */
	public String getText() {
		return text;
	}

	/**
	 * 
	 * @return the category attribute list.
	 */
	public List<Attribute> getAttributes() {
		List<Attribute> attrsCopy = new LinkedList<Attribute>();
		if (this.attributes != null) {
			for (Attribute attribute : this.attributes) {
				attrsCopy.add(new Attribute(attribute));
			}
		}
		return (this.attributes == null) ? null : attrsCopy;
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		if (this.attributes != null) {
			for (Attribute attribute : this.attributes) {
				if (attribute.getName().equals(attrName)) {
					return new Attribute(attribute);
				}
			}
		}
		return null;
	}

	/**
	 * Shows the contents of the <updated> or <published> elements.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (attributes != null) {
			for (Attribute attribute : attributes) {
				sb.append(attribute);
			}
		}
		// close the parent element
		// and add the date string
		sb.append(" >" + text);
		return sb.toString();
	}
}
