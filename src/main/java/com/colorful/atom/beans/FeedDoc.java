package com.colorful.atom.beans;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javanet.staxutils.IndentingXMLStreamWriter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class FeedDoc {
    /*
     *     * author  4.2.1
     * category  4.2.2
     * content  4.1.3
     * contributor  4.2.3
     * email  3.2.3
     * entry  4.1.2
     * feed  4.1.1
     * generator  4.2.4
     * icon  4.2.5
     * id  4.2.6
     * link  4.2.7
     * logo  4.2.8
     * name  3.2.1
     * published  4.2.9
     * rights  4.2.10
     * source  4.2.11
     * subtitle  4.2.12
     * summary  4.2.13
     * title  4.2.14
     * updated  4.2.15
     * uri  3.2.2
     * 
     * 
      */
    public static final Attribute atomBase = new Attribute("xmlns","http://www.w3.org/2005/Atom");
    public static final Attribute lang_en = new Attribute("xml:lang","en-US");
    static{
        
        
    }
    //writes files to the output doc. 
    public static void writeFeedDoc(String outFile,Feed feed) throws Exception{
        
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = new IndentingXMLStreamWriter(outputFactory.createXMLStreamWriter(new java.io.FileOutputStream(outFile)));
            new FeedWriter().writeFeed(writer,feed);
            writer.flush();
            writer.close();
    }
    
    public static Feed readFeedDoc(String inFile) throws Exception{
        
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader =
        inputFactory.createXMLStreamReader(new java.io.FileInputStream("inFile.xml"));
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
        
        Icon icon = new Icon("http://host/images/icon.png");
        feed.setIcon(icon);
        
        Logo logo = new Logo();
        logo.setUri("http://host/images/logo.png");
        feed.setLogo(logo);
        
        Category category = new Category("music","http://mtv.com/genere","music");
        feed.addCategory(category);
        
        Link link = new Link("http://www.yahoo.com","self");
        link.setHreflang(new Attribute("hreflang","en-US"));
        feed.addLink(link);
        
        Extension extension = new Extension("http://www.w3.org/1999/xhtml","div");
        extension.setContent("<span style='color:red;'>hello there</span>");
        feed.addExtension(extension);
        
        FeedDoc.writeFeedDoc("out.xml",feed);
        System.out.println("done");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
