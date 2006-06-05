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
package com.colorful.atom.beans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Calendar;

import javanet.staxutils.IndentingXMLStreamWriter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class FeedDoc {

    public static final Attribute atomBase = new Attribute("xmlns","http://www.w3.org/2005/Atom");
    public static final Attribute lang_en = new Attribute("xml:lang","en-US");
    public static String encoding = "utf-8";
    public static String xml_version = "1.0";
    //utility constant to use instead of File.separator
    public static final String URL_separator = "/";

    //writes files to the output doc. 
    public static void writeFeedDoc(String outFile,Feed feed,String encoding,String version) throws Exception{
        try{
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = new IndentingXMLStreamWriter(outputFactory.createXMLStreamWriter(new java.io.FileOutputStream(outFile),encoding));
            new FeedWriter().writeFeed(writer,feed,encoding,version);
            writer.flush();
            writer.close();
        }catch(Exception e){
            System.out.println("error creating xml file.");
            e.printStackTrace();
        }
    }
    
    //writes out an feed to an eml string.
    public static String readFeedString(String file) {
        StringBuffer feedXML = new StringBuffer();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = reader.readLine()) != null){
            	feedXML.append(line);
            }
            reader.close();
            
        }catch(Exception e){
            System.out.println("error creating xml string from feed.");
            e.printStackTrace();
        }
        return feedXML.toString();
    }
    
    public static Feed readFeedDoc(String fileOrString, boolean isFile) throws Exception{
        
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        
        XMLStreamReader reader = null;
        if(isFile){
            reader = inputFactory.createXMLStreamReader(new java.io.FileInputStream(fileOrString));
        }else{
            reader = inputFactory.createXMLStreamReader(new java.io.StringReader(fileOrString));
        }
        return new FeedReader().readFeed(reader);
    }
    
    public static void main(String[] args){
        try{
        Feed feed = new Feed(atomBase,lang_en);
        
        Generator generator = new Generator("http://www.colorfulsoftware.com/projects/atomsphere","1.0.0");
        generator.setText("Atomsphere");
        feed.setGenerator(generator);
        
        Id id = new Id();
        id.setUri(new URI("http://www.colorfulsoftware.com/atom.xml"));
        feed.setId(id);
        
        Updated updated = new Updated(Calendar.getInstance().getTime());
        feed.setUpdated(updated);
        
        Title title = new Title();
        title.setText("test feed");
        feed.setTitle(title);
        
        Author author = new Author("Bill Brown","http://www.earthbeats.net","info@earthbeats.net");
        feed.addAuthor(author);
        
        Contributor contributor = new Contributor("Mad Dog");
        contributor.setEmail(new Email("info@maddog.net"));
        feed.addContributor(contributor);
        
        Rights rights = new Rights();
        rights.setText("GPL 1.0");
        feed.setRights(rights);
        
        Icon icon = new Icon(new URI("http://host/images/icon.png"));
        feed.setIcon(icon);
        
        Logo logo = new Logo();
        logo.setUri(new URI("http://host/images/logo.png"));
        feed.setLogo(logo);
        
        Category category = new Category("music","http://mtv.com/genere","music");
        feed.addCategory(category);
        
        Link link = new Link("http://www.yahoo.com","self");
        link.setHreflang(new Attribute("hreflang","en-US"));
        feed.addLink(link);
        
        Extension extension = new Extension("http://www.w3.org/1999/xhtml","div");
        extension.setContent("<span style='color:red;'>hello there</span>");
        feed.addExtension(extension);
        
        Entry entry = new Entry();
        Id id2 = new Id();
        id2.setUri(new URI("http://www.colorfulsoftware.com/atom.xml#entry1"));
        entry.setId(id2);
        
        Updated updated2 = new Updated(Calendar.getInstance().getTime());
        entry.setUpdated(updated2);
        
        Title title2 = new Title("an exapmle atom entry");
        entry.setTitle(title2);
        
        Content content = new Content();
        content.setContent("hello. and welcome the the atomsphere feed builder for atom 1.0 builds.  I hope it is useful for you.");
        entry.setContent(content);
        
        feed.addEntry(entry);
        
        FeedDoc.writeFeedDoc("out.xml",feed,encoding,xml_version);
        
        Feed feed2 = FeedDoc.readFeedDoc("out.xml",true);
        FeedDoc.writeFeedDoc("out2.xml",feed2,encoding,xml_version);
        System.out.println("done");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
