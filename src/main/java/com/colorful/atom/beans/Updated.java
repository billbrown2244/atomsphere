/*
Atomsphere - an atom feed library.
Copyright (C) 2006 William R. Brown.

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
/* Change History:
 *  2006-11-14 wbrown - added javadoc documentation.
 */
package com.colorful.atom.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * This class represents an Atom 1.0 updated element.
 * @see http://www.atomenabled.org/developers/syndication/atom-format-spec.php
 * @author bill
 *	<code>
 *		atomUpdated = element atom:updated { atomDateConstruct}
 *	</code>
 */
public class Updated {
    
    //example 2006-04-28T12:50:43.337-05:00
    private static String timeZoneOffset = null;
    static{
        TimeZone timeZone = TimeZone.getDefault();
        int hours = (((timeZone.getRawOffset()/1000)/60)/60);
        if(hours >= 0){
            timeZoneOffset = TimeZone.getTimeZone("GMT"+"+"+hours).getID().substring(3);
        }else{
            timeZoneOffset = TimeZone.getTimeZone("GMT"+"-"+Math.abs(hours)).getID().substring(3);
        }
    }
    
    private static final SimpleDateFormat format = 
        new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SS\'"+timeZoneOffset+"\'");
    private Date updated = null;
    
    /**
     * 
     * @param updated the date formatted to [RFC3339]
     */
    public Updated(Date updated){
        this.updated = updated;
    }

    public Updated() {
        // nothing
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getText() {
        return format.format(updated);
    }

}
