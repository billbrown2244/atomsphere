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

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlbeans.XmlOptions;
import org.w3.x2005.atom.FeedDocument;



public class AdminServlet extends HttpServlet {
	
	private static final long serialVersionUID = -1934331481171473784L; 
	public static File atomConfigFile = null;
	public static String docRootURL = null;
	public static String cssURL = null;    
	public static String atomSpecURL = null;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		Map atomConfig = null;
		
		try{
			atomConfigFile = new File(config.getInitParameter("atomConfigFile"));
			
			//if the file exists decode it.
			//and write out the files to their web locations.
			if(atomConfigFile.exists()){
				XMLDecoder decode = new XMLDecoder(new BufferedInputStream(new FileInputStream(atomConfigFile)));
				atomConfig = (Map)decode.readObject();
				decode.close();
				
				Iterator feeds = atomConfig.keySet().iterator();
				while(feeds.hasNext()){
					String relativePath = (String)feeds.next();
                    String fullPath = getServletContext().getRealPath(relativePath);
                    File makeDirs = new File(fullPath.substring(0,fullPath.lastIndexOf("\\")));
                    makeDirs.mkdirs();            
                    FileWriter feedFile = new FileWriter(fullPath);
					XmlOptions options = new XmlOptions();
					options.setLoadStripWhitespace();
					options.setLoadTrimTextBuffer();
					FeedDocument feedDoc = FeedDocument.Factory.parse((String)atomConfig.get(relativePath),options);
					feedFile.write(feedDoc.toString());
					feedFile.flush();
					feedFile.close();
				}
				
			}else{//otherwise create the initial file.
				atomConfig = new TreeMap();
				//write the output to a file.
				XMLEncoder encode = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(atomConfigFile)));
				encode.writeObject(atomConfig);
				encode.close();
			}
			
			//initialize the other static variables
			docRootURL = config.getInitParameter("docRootURL");
			cssURL = config.getInitParameter("cssURL");
			atomSpecURL = config.getInitParameter("atomSpecURL");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public synchronized static void removeFeed(String relativePath, ServletContext context) throws Exception{
		//remove the file and remove the entry from the config.
		File feedXML = new File(context.getRealPath(relativePath));
		if(!feedXML.delete()){
			throw new Exception("could not delete file: "+relativePath);
		}else{
			//decode the current state
			XMLDecoder decode = new XMLDecoder(new BufferedInputStream(new FileInputStream(atomConfigFile)));
			Map atomConfig = (Map)decode.readObject();
			decode.close();
			
			//remove the entry
			atomConfig.remove(relativePath);
			
			//re encode it to the file. 
			XMLEncoder encode = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(atomConfigFile)));
			encode.writeObject(atomConfig);
			encode.close();
		}
		
	}
	
	public synchronized static void writeFeedToConfigFile(String relativePath,String feedXML) throws Exception{
		//decode the current state
		XMLDecoder decode = new XMLDecoder(new BufferedInputStream(new FileInputStream(atomConfigFile)));
		Map atomConfig = (Map)decode.readObject();
		decode.close();
		
		//add the new path to the sorted tree map
		atomConfig.put(relativePath,feedXML);
		
		//re encode it to the file. 
		XMLEncoder encode = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(atomConfigFile)));
		encode.writeObject(atomConfig);
		encode.close();
	}
	
	public void doGet( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		XMLDecoder decode = new XMLDecoder(new BufferedInputStream(new FileInputStream(atomConfigFile)));
		Map atomConfig = (Map)decode.readObject();
		decode.close();
		
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();        
		out.println("<html><head><title>Atomsphere</title><link rel=\"stylesheet\" type=\"text/css\" href=\""+cssURL+"\"/></head><body>");
		out.println("<h3>Atom Feed Admin</h3>");
		out.println("Document Root = "+docRootURL);
		out.println("<form method=\"post\" action=\"atom/create\" >");
		out.println("<table><tr><td>Feed Path and Name:</td><td><input name=\"relativePath\" value=\"\" /></td><td><input type=\"submit\" value=\"New\" /></td></tr></table>");
		out.println("</form>");
		
		try{
			Iterator feeds = atomConfig.keySet().iterator();
			while(feeds.hasNext()){
				//open the feed and read out the title of the feed.
				//provide a delete button and manage entries button.
				String relativePath = (String)feeds.next();  
				XmlOptions options = new XmlOptions();
				options.setLoadStripWhitespace();
				options.setLoadTrimTextBuffer();
				FeedDocument feedDoc = FeedDocument.Factory.parse((String)atomConfig.get(relativePath),options);
				String fullURLPath = feedDoc.getFeed().getIdArray()[0].getDomNode().getFirstChild().getNodeValue();
				out.println("<table><tr>"); 
				out.println("<td><a href=\""+fullURLPath+"\" >"+feedDoc.getFeed().getTitleArray(0).xmlText()+"</a></td>");
				out.println("<td><form method=\"post\" action=\"atom/modify\"><input type=\"hidden\" name=\"relativePath\" value=\""+relativePath+"\" /><input type=\"hidden\" name=\"relativePath\" value=\""+relativePath+"\" /><input type=\"submit\" value=\"Modify\" /></form></td>");
				out.println("<td><form method=\"post\" action=\"atom/delete\"><input type=\"hidden\" name=\"relativePath\" value=\""+relativePath+"\" /><input type=\"submit\" value=\"Delete\" /></form></td>");
				out.println("</tr></table>");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		out.println("</body></html>");
		
		out.flush();
		out.close();
	}
	
	
	public void doPost( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		doGet(request, response);
	}
}