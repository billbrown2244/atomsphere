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

Change History:
    2006-11-08 wbrown - changed API to include url's and make the method calls more intuitive.
    2006-11-12 wbrown - added javadoc documentation.
 */
package com.colorful.atom.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import javanet.staxutils.IndentingXMLStreamWriter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * This class reads and writes atom feeds to and from xml files and beans or xml strings.
 * @author bill
 *
 */
public class FeedDoc {

    public static final Attribute atomBase = new Attribute("xmlns","http://www.w3.org/2005/Atom");
    public static final Attribute lang_en = new Attribute("xml:lang","en-US");
    public static String encoding = "utf-8";
    public static String xml_version = "1.0";

    /**
     * Writes the atom feed to an xml file.
     * @param outFile the path and name of the file to write.
     * @param feed the atom feed bean containing the content of the feed
     * @param encoding the file encoding (default is utf-8)
     * @param version the xml version (default is 1.0)
     * @throws Exception thrown if the feed cannot be written to the outFile 
     */
    public static void writeFeedDoc(String outFile,Feed feed,String encoding,String version) throws Exception{
        //make sure id is present
        if(feed.getId() == null){
            throw new AtomSpecException("atom:entry elements MUST contain exactly one atom:id element.");
        }
        //make sure title is present
        if(feed.getTitle() == null){
            throw new AtomSpecException("atom:entry elements MUST contain exactly one atom:title element.");
        }
        //make sure updated is present
        if(feed.getUpdated() == null){
            throw new AtomSpecException("atom:entry elements MUST contain exactly one atom:updated element.");
        }
        //check for the author requirement
        if(feed.getAuthors() == null){
            if(feed.getEntries() == null){
                throw new AtomSpecException("atom:feed elements MUST contain one or more atom:author elements, unless all of the atom:feed element's child atom:entry elements contain at least one atom:author element.");
            }
            Map entries = feed.getEntries();
            Iterator entryKeys = entries.keySet().iterator();
            while(entryKeys.hasNext()){
                Entry entry = (Entry)entries.get(entryKeys.next());
                if(entry.getAuthors() == null){
                    if(entry.getSource() == null){
                        throw new AtomSpecException("atom:feed elements MUST contain one or more atom:author elements, unless all of the atom:feed element's child atom:entry elements contain at least one atom:author element.");
                    }
                    Source source = entry.getSource();
                    if(source.getAuthors() == null){
                        throw new AtomSpecException("atom:feed elements MUST contain one or more atom:author elements, unless all of the atom:feed element's child atom:entry elements contain at least one atom:author element.");
                    }

                }
            }

        }

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

    /**
     * This method reads a file from disk and outputs an atom 1.0 xml document string.
     * @param file the atom xml file to be read
     * @return an atom feed document string.
     */
    public static String readFeedToString(File file){
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

    /**
     * This method reads an atom file from the internet and writes it out to an atom feed string.
     * @param url the location of the atom file on the internet
     * @return an atom feed document string.
     */
    public static String readFeedToString(URL url){
        StringBuffer feedXML = new StringBuffer();
        try{
            BufferedReader reader = new BufferedReader( new InputStreamReader(url.openStream()));
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

    /**
     * @deprecated (use one of the readFeedToString() methods.  This will be removed in the next release.)
     * @param file the file path and name to be written to a string.
     * @return an atom feed document string.
     */
    public static String readFeedString(String file) {
        return readFeedToString(new File(file));
    }

    /**
     * This method reads an xml string into a Feed bean.
     * @param xmlString the xml string to be transformed into a Feed bean.
     * @return the atom Feed bean
     * @throws Exception if the string cannot be parsed into a Feed bean.
     */
    public static Feed readFeedToBean(String xmlString) throws Exception{
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(new java.io.StringReader(xmlString));
        return new FeedReader().readFeed(reader);
    }

    /**
     * This method
     * @param file
     * @return
     * @throws Exception
     */
    public static Feed readFeedToBean(File file) throws Exception{
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(new java.io.FileInputStream(file));
        return new FeedReader().readFeed(reader);
    }

    public static Feed readFeedToBean(URL url) throws Exception{
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(url.openStream());
        return new FeedReader().readFeed(reader);
    }

    /**
     * @deprecated use one of the readFeedToBean() methods. this method will be removed in the next release.
     * @param fileOrString file path or xml string
     * @param isFile whether the fileOrString is a file
     * @return a feed bean.
     * @throws Exception
     */
    public static Feed readFeedDoc(String fileOrString, boolean isFile) throws Exception{
        if(isFile){
            return readFeedToBean(new File(fileOrString));
        }
        return readFeedToBean(fileOrString);
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

            //Author author = new Author("Bill Brown","http://www.earthbeats.net","info@earthbeats.net");
            //feed.addAuthor(author);

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

            entry.addAuthor(new Author("Bill Brown"));
            
            feed.addEntry(entry);

            FeedDoc.writeFeedDoc("out.xml",feed,encoding,xml_version);

            Feed feed2 = FeedDoc.readFeedToBean(new File("out.xml"));
            FeedDoc.writeFeedDoc("out2.xml",feed2,encoding,xml_version);
            System.out.println("done");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
