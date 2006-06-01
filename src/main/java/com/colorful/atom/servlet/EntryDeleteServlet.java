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
package com.colorful.atom.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.colorful.atom.beans.Feed;
import com.colorful.atom.beans.FeedDoc;

public class EntryDeleteServlet extends HttpServlet {
    
    private static final long serialVersionUID = -1231695378642117995L;

    public void doPost( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String relativePath = request.getParameter("relativePath");
        String updated = request.getParameter("updated");
        try{
            //get the feed from the file
            String fullPath = getServletContext().getRealPath(relativePath);
            Feed feed = FeedDoc.readFeedDoc(fullPath,true);
            
            //remove the entry in the feed. 
            feed.getEntries().remove(updated);
            
            //rewrite the feed to the config and atom file
            AdminServlet.writeFeedToConfigFile(relativePath,FeedDoc.writeFeedToString(feed,FeedDoc.encoding,FeedDoc.xml_version));
            FeedDoc.writeFeedDoc(getServletContext().getRealPath(relativePath),feed,FeedDoc.encoding,FeedDoc.xml_version);

            
        }catch(Exception e){
            e.printStackTrace();
        }
        response.sendRedirect("../atom");
    }
    
    public void doGet( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doPost(request, response);
    }
}
