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
    2007-02-22 wbrown - removed deprecated methods.
    2007-06-20 wbrown - added 2 methods readFeedToString(Feed feed) and readEntryToString(Entry entry)
    2008-03-18 wbrown - added factory methods for all the sub elements. Added new file write methods 
    					to decouple dependency on stax-utils.  Added new input methods to read 
    					input streams into feeds an strings.
 */
package com.colorful.atom.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * This class reads and writes atom feeds to and from xml files, feed objects, streams or xml strings.
 * It contains all of the factory methods for building immutable copies of the elements.
 * @author Bill Brown
 *
 */
public class FeedDoc {

	/**
	 * 
	 * An enumeration of the different types of supported content.
	 *
	 */
	public static enum ContentType {TEXT,HTML,XHTML,OTHER,EXTERNAL}

	/**
	 * the default atom xml namespace attribute 
	 * of "http://www.w3.org/2005/Atom" 
	 */
    public static final Attribute atomBase = 
    	buildAttribute("xmlns","http://www.w3.org/2005/Atom");
    
    /**
     * the default library language attribute 
     * of "en-US" 
     */
    public static final Attribute lang_en = 
    	buildAttribute("xml:lang","en-US");
    
    /**
     * the default document encoding of "UTF-8"
     */
    public static String encoding = "UTF-8";
    
    /**
     * the default XML version of "1.0"
     */
    public static String xml_version = "1.0";
    
    /**
     * the atomsphere sort extension attribute.
     * See the <a href="http://www.colorfulsoftware.com/projects/atomsphere">
     * Project Page</a>
     * for more details
     */
    public static final Attribute sort =
    	buildAttribute("xmlns:sort"
		,"http://www.colorfulsoftware.com/projects/atomsphere/extension/sort/1.0");
    
	/**
	 * Comparator for sorting feed entries in ascending order.
	 */
	public static final Comparator<String> SORT_ASC = 
		new Comparator<String>(){        
		public int compare(String key1, String key2) {
			return key1.compareTo(key2);
	
		}
	};

	/**
	 * Comparator for sorting feed entries in descending order 
	 */
	public static final Comparator<String> SORT_DESC = 
		new Comparator<String>(){
		public int compare(String key1, String key2) {
			return key2.compareTo(key1);
		}
	};
	
    /**
     * 
     * @param output the target output for the feed.
     * @param feed the atom feed object containing the content of the feed
     * @param encoding the file encoding (default is UTF-8)
     * @param version the xml version (default is 1.0)
     * @throws Exception thrown if the feed cannot be written to the output 
     */
    public static void writeFeedDoc(OutputStream output,Feed feed,String encoding,String version) throws Exception{
    	writeFeedDoc(XMLOutputFactory.newInstance().createXMLStreamWriter(output,encoding),feed,encoding,version);
    }
    
    /**
     * 
     * @param output the target output for the feed.
     * @param feed the atom feed object containing the content of the feed
     * @param encoding the file encoding (default is UTF-8)
     * @param version the xml version (default is 1.0)
     * @throws Exception thrown if the feed cannot be written to the output 
     */
    public static void writeFeedDoc(Writer output,Feed feed,String encoding,String version) throws Exception{
    	writeFeedDoc(XMLOutputFactory.newInstance().createXMLStreamWriter(output),feed,encoding,version);
    }
    
    /**
     * For example: to get an indented xml output using the stax-utils library 
     * 	do this: 
     * 	<pre>
XmlStreamWriter writer = 
	new IndentingXMLStreamWriter(
 		XMLOutputFactory.newInstance()
   			.createXMLStreamWriter(
   				new FileOutputStream(outputFilePath)
  				,encoding));
FeedDoc.writeFeedDoc(writer,myFeed,null,null);	
    	</pre>
     * @param output the target output for the feed.
     * @param feed the atom feed object containing the content of the feed
     * @param encoding the file encoding (default is UTF-8)
     * @param version the xml version (default is 1.0)
     * @throws Exception thrown if the feed cannot be written to the output 
     */
	public static void writeFeedDoc(XMLStreamWriter output,Feed feed,String encoding,String version) throws Exception{
        //make sure id is present
        if(feed.getId() == null){
            throw new AtomSpecException("atom:feed elements MUST contain exactly one atom:id element.");
        }
        //make sure title is present
        if(feed.getTitle() == null){
            throw new AtomSpecException("atom:feed elements MUST contain exactly one atom:title element.");
        }
        //make sure updated is present
        if(feed.getUpdated() == null){
            throw new AtomSpecException("atom:feed elements MUST contain exactly one atom:updated element.");
        }
        //check for the author requirement
        if(feed.getAuthors() == null){
            if(feed.getEntries() == null){
                throw new AtomSpecException("atom:feed elements MUST contain one or more atom:author elements, unless all of the atom:feed element's child atom:entry elements contain at least one atom:author element.");
            }
            SortedMap<String,Entry> entries = feed.getEntries();
            Iterator<String> entryKeys = entries.keySet().iterator();
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
            new FeedWriter().writeFeed(output,feed,encoding,version);
            output.flush();
            output.close();
        }catch(Exception e){
            System.err.println("error creating xml file.");
            e.printStackTrace();
        }
    }

	
	/**
	 * This method reads an input stream and outputs an atom 1.0 xml document string.
	 * @param inputStream the stream containing the atom xml to be read.
	 * @return  an atom feed document string.
	 */
	public static String readFeedToString(InputStream inputStream){
		StringBuffer feedXML = new StringBuffer();
        try{
            BufferedReader reader = new BufferedReader( new InputStreamReader(inputStream));
            String line = null;
            while((line = reader.readLine()) != null){
                feedXML.append(line);
            }
            reader.close();
        }catch(Exception e){
            System.err.println("error creating xml string from feed.");
            e.printStackTrace();
        }
        return feedXML.toString();
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
            System.err.println("error creating xml string from feed.");
            e.printStackTrace();
            return null;
        }
        return feedXML.toString();
    }

    /**
     * This method reads an atom file from a URL and writes it out to an atom feed string.
     * @param url the location of the atom file on the Internet
     * @return an atom feed document string.
     */
    public static String readFeedToString(URL url){
    	try{
    		return readFeedToString(url.openStream());
    	}catch(Exception e){
            System.err.println("error creating xml string from feed.");
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * This method reads in a Feed bean and returns the contents as an atom feed string
     * with formatting specified by the fully qualified XMLStreamWriter class name 
     * (uses reflection internally). For example you can pass the 
     * TXW com.sun.xml.txw2.output.IndentingXMLStreamWriter
     * or the stax-utils javanet.staxutils.IndentingXMLStreamWriter
     * for indented printing.  It will fall back to <pre>readFeedToString(Feed)</pre> 
     * if the XMLStreamWriter class cannot be found in the classpath.
     * @param feed the feed to be converted to an atom string.
     * @param xmlStreamWriter the fully qualified XMLStreamWriter class name.
     * @return an atom feed document string.
     */
    public static String readFeedToStringCusotm(Feed feed, String xmlStreamWriter){

    	StringWriter theString = new StringWriter();
    	try{
    		Class<?> cls = Class.forName(xmlStreamWriter);
    		Constructor<?> ct = cls.getConstructor(
    				new Class[]{XMLStreamWriter.class});
    		Object arglist[] = new Object[]{
    				XMLOutputFactory.newInstance()
    				.createXMLStreamWriter(theString)};
    		XMLStreamWriter writer = (XMLStreamWriter)
    			ct.newInstance(arglist);

    		new FeedWriter().writeFeed(writer,feed,encoding,xml_version);
    		writer.flush();
    		writer.close();
    	}catch(Exception e){
    		System.err.println("error creating indented xml string from feed trying non indented.");
    		e.printStackTrace();
    		return readFeedToString(feed);
    	}
    	return theString.toString();
    }

    /**
     * This method reads in a Feed bean and returns the contents as an atom feed string.
     * @param feed the feed to be converted to an atom string.
     * @return an atom feed document string.
     */
    public static String readFeedToString(Feed feed){
    	
    	StringWriter theString = new StringWriter();
    	try{
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(theString);
            new FeedWriter().writeFeed(writer,feed,encoding,xml_version);
            writer.flush();
            writer.close();
        }catch(Exception e){
            System.err.println("error creating xml file.");
            e.printStackTrace();
        }
        return theString.toString();
    }
    
    /**
     * This method reads in an atom Entry bean and returns the contents as an atom feed string containing the entry.
     * @param entry the entry to be converted to an atom string.
     * @return an atom entry element string containing the entry passed in.
     */
    public static String readEntryToString(Entry entry) throws Exception{
    	
    	StringWriter theString = new StringWriter();
    	try{
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(theString);
            SortedMap<String,Entry> entries = new TreeMap<String,Entry>();
            entries.put(entry.getUpdated().getText(),entry);
            Feed wrapper = buildFeed(entry.getId(),entry.getTitle(),entry.getUpdated(),entry.getRights()
            		,null,null,null,null,null,null,null,null,null,null,entries);
            new FeedWriter().writeEntries(writer, wrapper.getEntries());
            writer.flush();
            writer.close();
        }catch(Exception e){
            System.err.println("error creating xml file.");
            e.printStackTrace();
        }
        return theString.toString();
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
     * This method reads an xml File object into a Feed bean.
     * @param file the file object representing an atom file.
     * @return the atom Feed bean.
     * @throws Exception if the file cannot be parsed into a Feed bean.
     */
    public static Feed readFeedToBean(File file) throws Exception{
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(new FileInputStream(file));
        return new FeedReader().readFeed(reader);
    }

    /**
     * This method reads an atom file from a URL into a Feed bean.
     * @param url the Internet network location of an atom file.
     * @return the atom Feed bean.
     * @throws Exception if the URL cannot be parsed into a Feed bean.
     */
    public static Feed readFeedToBean(URL url) throws Exception{
        return readFeedToBean(url.openStream());
    }
    
    /**
     * This method reads an atom file from an input stream into a Feed bean.
     * @param inputStream the input stream containing an atom file.
     * @return the atom Feed bean.
     * @throws Exception if the URL cannot be parsed into a Feed bean.
     */
    public static Feed readFeedToBean(InputStream inputStream) throws Exception{
    	XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(inputStream);
        return new FeedReader().readFeed(reader);
    }

    /**
     * 
     * @param id the unique id element (optional)
     * @param title the title element (optional)
     * @param updated the updated element (optional)
     * @param rights the rights element (optional)
     * @param authors a list of author elements (optional)
     * @param categories a list of category elements (optional)
     * @param contributors a list of contributor elements (optional)
     * @param links a list of link elements (optional)
     * @param attributes additional attributes (optional)
     * @param extensions a list of extension elements (optional)
     * @param generator the generator element (optional)
     * @param subtitle the subtitle element (optional)
     * @param icon the icon element (optional)
     * @param logo the logo element (optional)
     * @param entries a list of entry elements (optional)
     * @return an immutable Feed object.
     * @throws AtomSpecException
     */
    public static Feed buildFeed(Id id
			,Title title
			,Updated updated
			,Rights rights
			,List<Author> authors
			,List<Category> categories    		
			,List<Contributor> contributors
			,List<Link> links
			,List<Attribute> attributes
			,List<Extension> extensions
			,Generator generator
			,Subtitle subtitle
			,Icon icon
			,Logo logo
			,SortedMap<String,Entry> entries) throws AtomSpecException{
    	return new Feed(id,title,updated,rights,authors,categories,contributors
    			,links,attributes,extensions,generator,subtitle,icon,logo,entries);
    }
    
    /**
     * 
     * @param name the attribute name.
     * @param value the attribute value.
     * @return an immutable Attribute object.
     */
    public static Attribute buildAttribute(String name, String value){
    	return new Attribute(name,value);
    }
    
    /**
     * 
     * @param name the name element. (required)
     * @param uri the uri element.
     * @param email the email element.
     * @param attributes additional attributes.
     * @param extensions a list of extension elements.
     * @return an immutable Author object.
     * @throws AtomSpecException
     */
    public static Author buildAuthor(Name name, URI uri, Email email
    		, List<Attribute> attributes
			, List<Extension> extensions) throws AtomSpecException{
    	return new Author(name,uri,email,attributes,extensions);
    }
    
    /**
     * 
     * @param attributes the attributes list which must contain "term" /
     * 		and may contain "scheme", "label" or others
     * @param content the undefined element content.
     * @return an immutable Category object.
     * @throws AtomSpecException
     */
    public static Category buildCategory(List<Attribute> attributes
    		, String content) throws AtomSpecException {
    	return new Category(attributes,content);
    }
    
    /**
     * 
     * @param content the content of this element
     * @param attributes additional attributes.
     * @return an immutable Content object.
     */
    public static Content buildContent(String content, List<Attribute> attributes){
    	return new Content(content,attributes);
    }
    
    /**
     * 
     * @param name the name element. (required)
     * @param uri the uri element.
     * @param email the email element.
     * @param attributes additional attributes.
     * @param extensions a list of extension elements.
     * @return an immutable Contributor object.
     * @throws AtomSpecException
     */
    public static Contributor buildContributor(Name name
    		, URI uri
    		, Email email
    		, List<Attribute> attributes
			, List<Extension> extensions) throws AtomSpecException{
    	return new Contributor(name,uri,email,attributes,extensions);
    }
    
    /**
     * 
     * @param email a human-readable email for the person
     * @return an immutable Email object.
     */
    public static Email buildEmail(String email){
    	return new Email(email);
    }
    
    /**
     * 
     * @param id the id element (required)
     * @param title the title element (required)
     * @param updated the updated element (required)
     * @param rights the rights element (optional)
     * @param content the content element (optional)
     * @param authors a list of author elements (optional)
     * @param categories a list of category elements (optional)
     * @param contributors a list of contributor elements (optional)
     * @param links a list of link elements (optional)
     * @param attributes additional attributes.(optional)
     * @param extensions a list of extension elements (optional)
     * @param published the published element (optional)
     * @param summary the summary element (optional)
     * @param source the source element (optional)
     * @return an immutable Entry object.
     * @throws AtomSpecException
     */
    public static Entry buildEntry(Id id
    		,Title title
    		,Updated updated
    		,Rights rights
    		,Content content
    		,List<Author> authors
    		,List<Category> categories    		
    		,List<Contributor> contributors
    		,List<Link> links
    		,List<Attribute> attributes
    		,List<Extension> extensions    		
    		,Published published
    		,Summary summary
    		,Source source) throws AtomSpecException {
    	return new Entry(id,title,updated,rights,content
    			,authors,categories,contributors,links
    			,attributes,extensions,published,summary,source);
    }
    
    /**
     * 
     * @param elementName the name of the extension element.
     * @param attributes additional attributes.
     * @param content the content of the extension element.
     * @return an immutable Extension object.
     */
    public static Extension buildExtension(String elementName
    		, List<Attribute> attributes, String content){
    	return new Extension(elementName,attributes,content);
    }

    /**
     * 
     * @param attributes the attributes list which can contain "uri" and or "version" or others 
     * @param text the text content.
     * @return an immutable Generator object.
     */
    public static Generator buildGenerator(List<Attribute> attributes, String text){
    	return new Generator(attributes,text);
    }
    
    /**
     * 
     * @param atomUri the URI reference.
     * @param attributes additional attributes.
     * @return an immutable Icon object.
     */
    public static Icon buildIcon(List<Attribute> attributes, String atomUri){
    	return new Icon(attributes,atomUri);
    }
    
    /**
     * 
     * @param atomUri the URI reference.
     * @param attributes additional attributes.
     * @return an immutable Id object.
     */
    public static Id buildId(List<Attribute> attributes, String atomUri){
    	return new Id(attributes,atomUri);
    }
    
    /**
     * 
     * @param attributes the attributes list which must contain "href" and may contain "rel", "type", "hreflang", "title", "length" or others
     * @param content the undefined link content.
     * @return an immutable Link object.
     * @throws AtomSpecException
     */
    public static Link buildLink(List<Attribute> attributes
    		, String content) throws AtomSpecException {
    	return new Link(attributes,content);
    }
    
    /**
     * 
     * @param atomUri the logo uri reference.
     * @param attributes additional attributes.
     * @return an immutable Logo object.
     */
    public static Logo buildLogo(List<Attribute> attributes, String atomUri){
    	return new Logo(attributes,atomUri);
    }
    
    /**
     * 
     * @param name a human-readable name for the person
     * @return an immutable Name object.
     */
    public static Name buildName(String name){
    	return new Name(name);
    }
    
    /**
     * 
     * @param published the date formatted to [RFC3339]
     * @return an immutable Published object.
     */
    public static Published buildPublished(Date published){
    	return new Published(published);
    }
    
    /**
     * 
     * @param rights the rights text.
     * @param attributes additional attributes.
     * @return an immutable Rights object.
     */
    public static Rights buildRights(String rights
    		, List<Attribute> attributes){
    	return new Rights(rights,attributes);
    }
    
    /**
     * 
     * @param id the unique id element (optional)
     * @param title the title element (optional)
     * @param updated the updated element (optional)
     * @param rights the rights element (optional)
     * @param authors a list of author elements (optional)
     * @param categories a list of category elements (optional)
     * @param contributors a list of contributor elements (optional)
     * @param links a list of link elements (optional)
     * @param attributes additional attributes (optional)
     * @param extensions a list of extension elements (optional)
     * @param generator the generator element (optional)
     * @param subtitle the subtitle element (optional)
     * @param icon the icon element (optional)
     * @param logo the logo element (optional)
     * @return an immutable Source object.
     * @throws AtomSpecException
     */
    public static Source buildSource(Id id
    		,Title title
    		,Updated updated
    		,Rights rights
    		,List<Author> authors
    		,List<Category> categories    		
    		,List<Contributor> contributors
    		,List<Link> links
    		,List<Attribute> attributes
    		,List<Extension> extensions
    		,Generator generator
    		,Subtitle subtitle
    		,Icon icon
    		,Logo logo) throws AtomSpecException {
    	return new Source(id,title,updated,rights,authors
    			,categories,contributors,links,attributes
    			,extensions,generator,subtitle,icon,logo);
    }
    
    /**
     * 
     * @param subtitle the subtitle text.
     * @param attributes additional attributes.
     * @return an immutable Subtitle object.
     */
    public static Subtitle buildSubtitle(String subtitle
    		, List<Attribute> attributes){
    	return new Subtitle(subtitle,attributes);
    }
    
    /**
     * 
     * @param summary the summary text.
     * @param attributes additional attributes.
     * @return an immutable Summary object.
     */
    public static Summary buildSummary(String summary
    		, List<Attribute> attributes){
    	return new Summary(summary,attributes);
    }
    
    /**
     * 
     * @param title the title text
     * @param attributes additional attributes.
     * @return an immutable Title object.
     */
    public static Title buildTitle(String title
    		, List<Attribute> attributes){
    	return new Title(title,attributes);
    }
    
    /**
     * 
     * @param updated the date formatted to [RFC3339]
     * @return a immutable Updated object.
     */
    public static Updated buildUpdated(Date updated){
    	return new Updated(updated);
    }
    
    /**
     * 
     * @param uri the content of the uri according to Section 7 of [RFC3986]
     * @return and immutable URI object.
     */
    public static URI buildURI(String uri){
    	return new URI(uri);
    }
    
    //used internally by feed reader
    static AtomPersonConstruct buildAtomPersonConstruct(Name name
    		, URI uri, Email email
    		, List<Attribute> attributes
    		, List<Extension> extensions) throws AtomSpecException {
    	return new AtomPersonConstruct(name,uri,email,attributes,extensions);
    }
    
    //checks for and returns the Attribute from the String attribute (argument)
    //in the list of attributes (argument) 
    //used by Category, Generator and Link.
    static Attribute getAttributeFromGroup(List<Attribute> attributes, String attributeName) {    	
    	if(attributes != null){
	    	Iterator<Attribute> attrItr = attributes.iterator();
			while(attrItr.hasNext()){
				Attribute current = attrItr.next();
				if(current.getName().equalsIgnoreCase(attributeName)){
						return buildAttribute(current.getName(),current.getValue());
				}	
			}
    	}
		return null;
	}
    
	/**
	 * Convenience method for getting the content type for this element.
	 * Examines the "type" and "src" attributes if they exist in the list. 
	 * 
	 * @return the content type for this element. One of TEXT,HTML,XHTML,OTHER or EXTERNAL
	 */
	public static ContentType getContentType(List<Attribute> attriubtes){
		ContentType contentType = ContentType.TEXT; //default
		if(attriubtes != null){
			Iterator<Attribute> attrItr = attriubtes.iterator();
			while(attrItr.hasNext()){
				Attribute attr = attrItr.next();
				if(attr.getName().equals("type") && attr.getValue().equals("text")){
					contentType = ContentType.TEXT;
					break;
				}else if(attr.getName().equals("type") && attr.getValue().equals("html")){
					contentType = ContentType.HTML;
					break;
				}else if(attr.getName().equals("type") && attr.getValue().equals("xhtml")){
					contentType = ContentType.XHTML;
				}else if(attr.getName().equals("type") && (!attr.getValue().equals("text")
						&& !attr.getValue().equals("html")
						&& !attr.getValue().equals("xhtml"))){
					contentType = ContentType.OTHER;
					break;
				}else if(attr.getName().equals("src")){
					contentType = ContentType.EXTERNAL;
					break;
				}
			}
		}
		return contentType;
	}
	
	/**
	 * This method sorts the entries of the feed.
	 * The Updated, Title and Summary are currently the only 
	 * elementInstance types supported.
	 * @param feed the feed whose entries are to be sorted
	 * @param comparator used to determine sort order
	 * @param elementInstance serves as the key element for the entries collection 
	 * @return the sorted feed.
	 * @throws AtomSpecException
	 */
	public static Feed sortEntries(Feed feed, Comparator<String> comparator, Object elementInstance) throws AtomSpecException{

		if(feed.getEntries() != null){
			//sort the entries with the passed in instance as the key
			SortedMap<String,Entry> resortedEntries = new TreeMap<String,Entry>(comparator);
			SortedMap<String,Entry> currentEntries = feed.getEntries();
			Iterator<Entry> entryItr = currentEntries.values().iterator();
			while(entryItr.hasNext()){
				Entry entry = (Entry)entryItr.next();
				if (elementInstance instanceof Updated){
					resortedEntries.put(entry.getUpdated().getText(),entry);
				}
				if (elementInstance instanceof Title){
					resortedEntries.put(entry.getTitle().getText(),entry);
				}
				if (elementInstance instanceof Summary){
					resortedEntries.put(entry.getSummary().getText(),entry);
				}
			}

			//rebuild the top level feed attributes to include the sort
			//if it isn't already there.
			List<Attribute> localFeedAttrs = new LinkedList<Attribute>();
			Attribute attrLocal = FeedDoc.buildAttribute("xmlns:sort","http://www.colorfulsoftware.com/projects/atomsphere/extension/sort/1.0");
			if(feed.getAttributes() == null){
				localFeedAttrs.add(attrLocal);
			}else{
				List<Attribute> currentAttributes = feed.getAttributes();
				Iterator<Attribute> attrItr = currentAttributes.iterator();
				while(attrItr.hasNext()){
					Attribute attr = (Attribute)attrItr.next();
					if(!attr.equals(attrLocal)){
						localFeedAttrs.add(attr);
					}
				}
				
				//finally add the sort extension attribute declaration
				localFeedAttrs.add(attrLocal);	
			}

			//add or replace this extension element. 

			String elementName = null;
			if(comparator == FeedDoc.SORT_ASC){
				elementName = "sort:asc";
			}else{
				elementName = "sort:desc";
			}
			Attribute sortElement = null;
			if(elementInstance instanceof Updated){
				sortElement = FeedDoc.buildAttribute("type","updated");
			}else if(elementInstance instanceof Title){
				sortElement = FeedDoc.buildAttribute("type","title");
			}else if(elementInstance instanceof Summary){
				sortElement = FeedDoc.buildAttribute("type","summary");
			}
			List<Attribute> extAttrs = new LinkedList<Attribute>();
			extAttrs.add(sortElement);
			Extension localFeedExtension = FeedDoc.buildExtension(elementName,extAttrs,null); 

			//rebuild the extensions
			//we have to look for the sort extension and 
			//replace any occurrences of it with the one we just created.
			List<Extension> localFeedExtensions = new LinkedList<Extension>();
			if(feed.getExtensions() == null){
				localFeedExtensions.add(localFeedExtension);
			}else{			
				List<Extension> currentExtensions = feed.getExtensions();
				Iterator<Extension> extensionItr = currentExtensions.iterator();
				while(extensionItr.hasNext()){
					Extension extn = (Extension)extensionItr.next();
					//if we find an existing sort extension, ignore it.
					//add all others to the return list.
					if(!extn.getElementName().equalsIgnoreCase("sort:asc")
							&& !extn.getElementName().equalsIgnoreCase("sort:desc")){
						localFeedExtensions.add(extn);
					}
				}
				//finally add the new one.
				localFeedExtensions.add(localFeedExtension);
			}
			
			//this is an immutable sorted copy of the feed.
			return FeedDoc.buildFeed(feed.getId()
					,feed.getTitle()
					,feed.getUpdated()
					,feed.getRights()
				,feed.getAuthors(),feed.getCategories(),feed.getContributors()
				,feed.getLinks(),localFeedAttrs,localFeedExtensions,feed.getGenerator()
				,feed.getSubtitle(),feed.getIcon(),feed.getLogo(),resortedEntries);
		}
		//return the feed in the original order.
		return feed;
	}
    


	
	// Checks the xmlns (namespace) argument and applies the extension 
	//to the feed argument if it is recognized by the atomsphere library.
	//used by FeedReader and FeedWriter
	static Feed checkForAndApplyExtension(Feed feed, Attribute xmlns) {

		//if there aren't any attributes for the feed and thus no xmlns:sort attr
		//return the defaults.
		if(feed.getAttributes() == null){
			return feed;
		}

		//check for the first supported extension
		//currently only sort is implemented.
		Iterator<Attribute> attrs = feed.getAttributes().iterator();
		while(attrs.hasNext()){
			Attribute attr = (Attribute)attrs.next();
			if(attr.equals(xmlns)){
				try{
					return applySort(feed);
				}catch(Exception e){
					//this should never happen because 
					//we check for errors on initial creation
					//but if it does, print the stack trace
					e.printStackTrace();
				}
			}
		}
		return feed;
	}

	//check for and apply the first sort extension.
	private static Feed applySort(Feed feed) throws AtomSpecException{
		//only do the work if there are extensions.
		if(feed.getExtensions() != null){
			//look for the first extension element if the namespace exists.
			Iterator<Extension> extItr = feed.getExtensions().iterator();
			Iterator<Attribute> attrs;
			while(extItr.hasNext()){
				Extension ext = (Extension)extItr.next();
				if(ext.getElementName().equals("sort:asc")){
					attrs = ext.getAttributes().iterator();
					while(attrs.hasNext()){
						Attribute attr = (Attribute)attrs.next();
						if(attr.getName().equalsIgnoreCase("type")){
							String value = attr.getValue();
							if(value.equals("updated")){
								return sortEntries(feed,FeedDoc.SORT_ASC, FeedDoc.buildUpdated(null)); 
							}
							if(value.equals("title")){
								return sortEntries(feed,FeedDoc.SORT_ASC, FeedDoc.buildTitle(null,null));
							}
							if(value.equals("summary")){
								return sortEntries(feed,FeedDoc.SORT_ASC, FeedDoc.buildSummary(null,null));
							}
						}
					}
				}else if(ext.getElementName().equals("sort:desc")){
					attrs = ext.getAttributes().iterator();
					while(attrs.hasNext()){
						Attribute attr = (Attribute)attrs.next();
						if(attr.getName().equalsIgnoreCase("type")){
							String value = attr.getValue();
							if(value.equals("updated")){
								return sortEntries(feed,FeedDoc.SORT_DESC, FeedDoc.buildUpdated(null)); 
							}
							if(value.equals("title")){
								return sortEntries(feed,FeedDoc.SORT_DESC, FeedDoc.buildTitle(null,null));
							}
							if(value.equals("summary")){
								return sortEntries(feed,FeedDoc.SORT_DESC, FeedDoc.buildSummary(null,null));
							}
						}
					}
				}
			}
		}
		return feed;
	}
    /**
     * Example library usage test method.
     * @param args
     */
    public static void main(String[] args){
        try{
            
            List<Attribute> genAttrs = new LinkedList<Attribute>();
            genAttrs.add(buildAttribute("uri","http://www.colorfulsoftware.com/projects/atomsphere"));
            genAttrs.add(buildAttribute("version","2.0.0.0"));
            Generator generator = buildGenerator(genAttrs,"Atomsphere");

            Id id = buildId(null,"http://www.colorfulsoftware.com/atom.xml");

            Updated updated = buildUpdated(Calendar.getInstance().getTime());

            Title title = buildTitle("test feed",null);

            List<Contributor> contributors = new LinkedList<Contributor>();
            Contributor contributor = buildContributor(new Name("Mad Dog"),null
            		,buildEmail("info@maddog.net"),null,null);
            contributors.add(contributor);
            
            Rights rights = buildRights("GPL 1.0",null);

            Icon icon = buildIcon(null,"http://host/images/icon.png");

            Logo logo = buildLogo(null,"http://host/images/logo.png");

            List<Attribute> catAttrs = new LinkedList<Attribute>();
            catAttrs.add(buildAttribute("term","music"));
            catAttrs.add(buildAttribute("scheme","http://mtv.com/genere"));
            catAttrs.add(buildAttribute("label","music"));
            List<Category> categories = new LinkedList<Category>();
            Category category = buildCategory(catAttrs,null);
            categories.add(category);
            
            List<Attribute> linkAttrs = new LinkedList<Attribute>();
            linkAttrs.add(buildAttribute("href","http://www.yahoo.com"));
            linkAttrs.add(buildAttribute("rel","self"));
            linkAttrs.add(buildAttribute("hreflang","en-US"));
            List<Link> links = new LinkedList<Link>();
            Link link = buildLink(linkAttrs,null);
            links.add(link);
            
            Attribute extAttr = buildAttribute("xmlns:xhtml","http://www.w3.org/1999/xhtml");
            List<Attribute> extAttrs = new LinkedList<Attribute>();
            extAttrs.add(extAttr);
            
            //the base feed attributes.
            List<Attribute> feedAttrs = new LinkedList<Attribute>();
            feedAttrs.add(atomBase);
            feedAttrs.add(lang_en);
            feedAttrs.addAll(extAttrs);
            
            List<Extension> extensions = new LinkedList<Extension>();
            Extension extension = buildExtension("xhtml:div",null
            		,"<span style='color:red;'>hello there</span>");
            extensions.add(extension);
            
            List<Author> authors = new LinkedList<Author>();
            authors.add(buildAuthor(buildName("Bill Brown"),null,null,null,null));
            Entry entry = buildEntry(
            		buildId(null,"http://www.colorfulsoftware.com/atom.xml#entry1")
            ,buildTitle("an example atom entry",null)
            ,buildUpdated(Calendar.getInstance().getTime())
            ,null
            ,buildContent("Hello World.  Welcome to the atomsphere feed builder for atom 1.0 builds.  I hope it is useful for you.",null)
            ,authors
            ,null
            ,null
            ,null
            ,null
            ,null
            ,null
            ,null
            ,null
            );

            SortedMap<String,Entry> entries = new TreeMap<String,Entry>();
            entries.put(entry.getUpdated().getText(),entry);
            
            
            Feed feed = buildFeed(id,title,updated,rights
            		,null,categories,contributors,links
            		,feedAttrs,extensions,generator,null,icon,logo,entries);
            
            System.out.println("com.sun.xml.txw2.output.IndentingXMLStreamWriter");
            System.out.println(FeedDoc.readFeedToStringCusotm(feed,
            		"com.sun.xml.txw2.output.IndentingXMLStreamWriter"));
            
            System.out.println("javanet.staxutils.IndentingXMLStreamWriter");
            System.out.println(FeedDoc.readFeedToStringCusotm(feed,
    		"javanet.staxutils.IndentingXMLStreamWriter"));
            
            System.out.println("bunk");
            System.out.println(FeedDoc.readFeedToStringCusotm(feed,
    		"bunk"));
            
            //pretty print version.
            FeedDoc.writeFeedDoc(new javanet.staxutils.IndentingXMLStreamWriter(
					XMLOutputFactory.newInstance()
					.createXMLStreamWriter(
						new FileOutputStream("out.xml")
						,encoding)),feed,encoding,xml_version);

            Feed feed2 = FeedDoc.readFeedToBean(new File("out.xml"));
            
            //no spaces version
            FeedDoc.writeFeedDoc(new FileOutputStream("out2.xml"),feed2,encoding,xml_version);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
