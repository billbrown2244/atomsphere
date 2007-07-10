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
 *  2007-02-05 wbrown - added sort method for sorting entries.
 *  2007-02-19 wbrown - add support for sorting entries by title and updated.
 *  2007-06-20 wbrown - adding support for sorting by summary. 
 *  2007-07-10 wbrown - commented out debug code.
 */
package com.colorful.atom.beans;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class represents an Atom 1.0 feed element.
 * @see <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom Syndication Format</a>
 * @author Bill Brown
 *  <pre>
 *      atomFeed =
 *          element atom:feed {
 *          atomCommonAttributes,
 *          (atomAuthor*
 *          & atomCategory*
 *          & atomContributor*
 *          & atomGenerator?
 *          & atomIcon?
 *          & atomId
 *          & atomLink*
 *          & atomLogo?
 *          & atomRights?
 *          & atomSubtitle?
 *          & atomTitle
 *          & atomUpdated
 *          & extensionElement*),
 *          atomEntry*
 *          }
 *  </pre>
 *  
 *  The default ordering of the entries is by date updated date descending. 
 */
public class Feed {

    private List attributes = null;
    private List authors = null;
    private List categories = null;
    private List contributors = null;
    private Generator generator = null;
    private Icon icon = null;
    private Id id = null;
    private List links = null;
    private Logo logo = null;
    private Rights rights = null;
    private Subtitle subtitle = null;
    private Title title = null;
    private Updated updated = null;
    private List extensions = null;
    private Map entries = null;
    public static final Comparator SORT_ASC = new Comparator(){        
        public int compare(Object key1, Object key2) {
            return key1.toString().compareTo(key2.toString());

        }
    };
    public static final Comparator SORT_DESC = new Comparator(){
        public int compare(Object key1, Object key2) {
            return key2.toString().compareTo(key1.toString());
        }
    };


    public Feed(){
        attributes = new LinkedList();
    }

    /**
     * 
     * @param attribute an attribute for the feed
     */
    public Feed(Attribute attribute){
        attributes = new LinkedList();
        attributes.add(attribute);
    }

    /**
     * 
     * @param attribute1 an attribute for the feed
     * @param attribute2 an attribute for the feed
     */
    public Feed(Attribute attribute1, Attribute attribute2){
        attributes = new LinkedList();
        attributes.add(attribute1);
        attributes.add(attribute2);
    }

    public List getAttributes() {
        return attributes;
    }

    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }

    public List getAuthors() {
        return authors;
    }

    public void setAuthors(List authors) {
        this.authors = authors;
    }

    public List getCategories() {
        return categories;
    }

    public void setCategories(List categories) {
        this.categories = categories;
    }

    public List getContributors() {
        return contributors;
    }

    public void setContributors(List contributors) {
        this.contributors = contributors;
    }

    public Map getEntries() {
        return entries;
    }

    public void setEntries(Map entries) {
        this.entries = entries;
    }

    public List getExtensions() {
        return extensions;
    }

    public void setExtensions(List extensions) {
        this.extensions = extensions;
    }

    public Generator getGenerator() {
        return generator;
    }

    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public List getLinks() {
        return links;
    }

    public void setLinks(List links) {
        this.links = links;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public Rights getRights() {
        return rights;
    }

    public void setRights(Rights rights) {
        this.rights = rights;
    }

    public Subtitle getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(Subtitle subtitle) {
        this.subtitle = subtitle;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Updated getUpdated() {
        return updated;
    }

    public void setUpdated(Updated updated) {
        this.updated = updated;
    }

    public void addAttribute(Attribute attribute){
        if(this.attributes == null){
            this.attributes = new LinkedList();
        }
        this.attributes.add(attribute);
    }

    public void addAuthor(Author author) throws AtomSpecException{
        //check to make sure there is a name element
        if(author.getName() == null){
            throw new AtomSpecException("Person constructs MUST contain exactly one \"atom:name\" element.");
        }
        if(this.authors == null){
            this.authors = new LinkedList();
        }
        this.authors.add(author);        
    }

    public void addContributor(Contributor contributor) throws AtomSpecException{
        //check to make sure there is a name element
        if(contributor.getName() == null){
            throw new AtomSpecException("Person constructs MUST contain exactly one \"atom:name\" element.");
        }
        if(this.contributors == null){
            this.contributors = new LinkedList();
        }
        this.contributors.add(contributor);
    }

    public void addCategory(Category category) throws AtomSpecException{
        //check to make sure there is a term element
        if(category.getTerm() == null){
            throw new AtomSpecException("Category elements MUST have a \"term\" attribute.");
        }
        if(this.categories == null){
            this.categories = new LinkedList();
        }
        this.categories.add(category);
    }

    public void addLink(Link link) throws AtomSpecException{
        //check to make sure there is a href attribute
        if(link.getHref() == null){
            throw new AtomSpecException("atom:link elements MUST have an href attribute, whose value MUST be a IRI reference");
        }
        if(this.links == null){
            this.links = new LinkedList();
        }
        this.links.add(link);
    }

    public void addExtension(Extension extension) {
        if(this.extensions == null){
            this.extensions = new LinkedList();
        }
        this.extensions.add(extension);

    }

    public void addEntry(Entry entry) throws AtomSpecException{
        //make sure id is present
        if(entry.getId() == null){
            throw new AtomSpecException("atom:entry elements MUST contain exactly one atom:id element.");
        }
        //make sure title is present
        if(entry.getTitle() == null){
            throw new AtomSpecException("atom:entry elements MUST contain exactly one atom:title element.");
        }
        //make sure updated is present
        if(entry.getUpdated() == null){
            throw new AtomSpecException("atom:entry elements MUST contain exactly one atom:updated element.");
        }
        //if there is no author element at the feed level
        //check to make sure the entry has an author element
        if (this.authors == null){
            if(entry.getAuthors() == null){
                throw new AtomSpecException("atom:entry elements MUST contain one or more atom:author elements, unless the atom:entry contains an atom:source element that contains an atom:author element or, in an Atom Feed Document, the atom:feed element contains an atom:author element itself.");
            }
        }
        //check for the summary requirement
        if(entry.getContent() != null){
            Content content = entry.getContent();
            if(content.getAttributes() != null){
                //check for src attribute
                Iterator attrsItr = content.getAttributes().iterator();
                while(attrsItr.hasNext()){
                    Attribute attr = (Attribute)attrsItr.next();
                    if(attr.getName().equals("src")){
                        if(entry.getSummary() == null){
                            throw new AtomSpecException("atom:entry elements MUST contain an atom:summary element in either of the following cases: the atom:entry contains an atom:content that has a \"src\" attribute (and is thus empty).");
                        }
                    }
                }
                //check for non-xml media type
                attrsItr = content.getAttributes().iterator();
                while(attrsItr.hasNext()){
                    Attribute attr = (Attribute)attrsItr.next();
                    if(attr.getName().equals("type") 
                            && !attr.getValue().startsWith("text/")
                            && !attr.getValue().endsWith("/xml")
                            && !attr.getValue().endsWith("+xml")){
                        if(entry.getSummary() == null){
                            throw new AtomSpecException("atom:entry elements MUST contain an atom:summary element in either of the following cases: the atom:entry contains content that is encoded in Base64; i.e., the \"type\" attribute of atom:content is a MIME media type [MIMEREG], but is not an XML media type [RFC3023], does not begin with \"text/\", and does not end with \"/xml\" or \"+xml\".");
                        }
                    }
                }
            }
        }


        //sort the entries with the default sort type initially.
        if(this.entries == null){
            this.entries = new TreeMap(Feed.SORT_DESC);
        }

        //add the entry with the default keyType of Updated.
        this.entries.put(entry.getUpdated().getText(),entry);

    }


    /**
     * This method sorts the entries of the feed.  The default ordering of the entries is by updated descending 
     * @param comparator this gives the ordering for the entries.
     * @param type and instance of the type to sort by.
     */
    public void sortEntries(Comparator comparator, Object sortInstance){

        if(this.entries != null){
        	//sort the entries with the passed in instance as the key
            Map temp = new TreeMap(comparator);
            Iterator entryItr = this.entries.values().iterator();
            while(entryItr.hasNext()){
                Entry entry = (Entry)entryItr.next();
                if (sortInstance instanceof Updated){ 
                	//System.out.println("updated key = "+entry.getUpdated().getText());
                	temp.put(entry.getUpdated().getText(),entry);
                }
                if (sortInstance instanceof Title){
                	//System.out.println("title key = "+entry.getTitle().getText());
                    temp.put(entry.getTitle().getText(),entry);
                }
                if (sortInstance instanceof Summary){
                	//System.out.println("summary key = "+entry.getSummary().getText());
                    temp.put(entry.getSummary().getText(),entry);
                }
            }
            this.entries = temp;

            /*debug 
            entryItr = this.entries.values().iterator();
            while(entryItr.hasNext()){
            	System.out.println("sorted value = "+(String)entryItr.next());
            }
            */
            
            //add the sort extension declaration if it isn't already there.
            Attribute attrLocal = new Attribute("xmlns:sort","http://www.colorfulsoftware.com/projects/atomsphere/extension/sort/1.0");
        	if(attributes != null){
                boolean addSort = true;
                Iterator attrItr = attributes.iterator();
                while(attrItr.hasNext()){
                    Attribute attr = (Attribute)attrItr.next();
                    if(attr.equals(attrLocal)){
                        addSort = false;
                    }
                }
                if(addSort){
                    addAttribute(attrLocal);
                }
            }else{
                addAttribute(attrLocal);
            }

            //add or replace this extension element. 
            Extension extension = null;            
            if(comparator == Feed.SORT_ASC){
                extension = new Extension("sort:asc");
            }else{
                extension = new Extension("sort:desc");
            }
            if(sortInstance instanceof Updated){
            	extension.addAttribute(new Attribute("type","updated"));
            }else if(sortInstance instanceof Title){
            	extension.addAttribute(new Attribute("type","title"));
            }else if(sortInstance instanceof Summary){
            	extension.addAttribute(new Attribute("type","summary"));
            }
            //if there are already extensions,
            //we have to look for the sort extension and 
            //replace any occurrences of it with the one we just created.
            if(extensions == null){
                addExtension(extension);
            }else{
                boolean addExt = true;
                Iterator extensionItr = extensions.iterator();
                List tempExtensions = new LinkedList();
                while(extensionItr.hasNext()){
                    Extension extn = (Extension)extensionItr.next();
                    //if we find the extension remove it.
                    if(extn.getElementName().equalsIgnoreCase("sort:asc")
                            || extn.getElementName().equalsIgnoreCase("sort:desc")){
                       tempExtensions.add(extn);
                    }
                }
                //remove any sort extension we found above
                if(tempExtensions.size() > 0){
                    extensionItr = tempExtensions.iterator();
                    while(extensionItr.hasNext()){
                        extensions.remove(extensionItr.next());
                    }
                }
                
                //finally add the new one.
                if(addExt){
                    addExtension(extension);
                }
            }
        }
    }

    /**
     * Checks the namespace argument and applies the extension if it is recognized by the atomsphere library.
     * @param xmlns the structuredExtension element's.
     */
    protected void checkForAndApplyExtension(Attribute xmlns) {

        //if there aren't any attributes for the feed and thus no xmlns:sort attr
        //return the defaults.
        if(this.attributes == null){
            return;
        }

        //check for the first supported extension
        //currently only sort is implemented.
        Iterator attrs = attributes.iterator();
        while(attrs.hasNext()){
            Attribute attr = (Attribute)attrs.next();
            if(attr.equals(xmlns)){
            	applySort();
            }
        }
    }

    //check for and apply the first sort extension.
	private void applySort() {
		//only do the work if there are extensions.
        if(extensions != null){
        	//look for the first extension element if the namespace exists.
	        Iterator extItr = extensions.iterator();
	        Iterator attrs;
	        while(extItr.hasNext()){
	            Extension ext = (Extension)extItr.next();
	            if(ext.getElementName().equals("sort:asc")){
	                attrs = ext.getAttributes().iterator();
	                while(attrs.hasNext()){
	                    Attribute attr = (Attribute)attrs.next();
	                    if(attr.getName().equalsIgnoreCase("type")){
	                        String value = attr.getValue();
	                        if(value.equals("updated")){
	                        	//System.out.println("sorting by updated ascending.");
	                        	sortEntries(SORT_ASC, new Updated()); 
	                        	return;
	                        }
	                        if(value.equals("title")){
	                        	//System.out.println("sorting by title ascending.");
	                        	sortEntries(SORT_ASC, new Title());
	                        	return;
	                        }
	                        if(value.equals("summary")){
	                        	//System.out.println("sorting by summary ascending.");
	                        	sortEntries(SORT_ASC, new Summary());
	                        	return;
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
	                        	//System.out.println("sorting by updated descending.");
	                        	sortEntries(SORT_DESC, new Updated()); 
	                        	return;
	                        }
	                        if(value.equals("title")){
	                        	//System.out.println("sorting by title descending.");
	                        	sortEntries(SORT_DESC, new Title());
	                        	return;
	                        }
	                        if(value.equals("summary")){
	                        	//System.out.println("sorting by summary descending.");
	                        	sortEntries(SORT_DESC, new Summary());
	                        	return;
	                        }
	                    }
	                }
	            }
	        }
        }
	}
}
