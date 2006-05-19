package com.colorful.atom.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Updated {
    /*
     * atomUpdated = element atom:updated { atomDateConstruct}
     */
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
        new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSS\'"+timeZoneOffset+"\'");
    private Date updated = null;
    
    public Updated(Date updated){
        this.updated = updated;
    }

    public Updated() {
        // TODO Auto-generated constructor stub
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
