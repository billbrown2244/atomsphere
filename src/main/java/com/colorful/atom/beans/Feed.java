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
 *  2008-03-17 wbrown - made class immutable.
 *  2008-03-21 wbrown - move comparators to feed doc.
 */
package com.colorful.atom.beans;

import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
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
 */
public class Feed {

	private final Source source;
	private final SortedMap<String,Entry> entries;

	//use the factory method in the FeedDoc.
	Feed(Id id
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
			,SortedMap<String,Entry> entries) throws AtomSpecException {
		this.source = new Source(id,title,updated,rights,authors,categories
				,contributors,links,attributes,extensions
				,generator,subtitle,icon,logo);

		if(entries == null){
			this.entries = null;
		}else{
			
			this.entries = new TreeMap<String,Entry>(entries.comparator());
			Iterator<String> entryItr = entries.keySet().iterator();
			while(entryItr.hasNext()){
				String entryKey = entryItr.next();
				Entry entry = entries.get(entryKey);

				//if there is no author element at the feed level
				//check to make sure the entry has an author element
				if (authors == null){
					if(entry.getAuthors() == null){
						throw new AtomSpecException("atom:entry elements MUST contain one or more atom:author elements, unless the atom:entry contains an atom:source element that contains an atom:author element or, in an Atom Feed Document, the atom:feed element contains an atom:author element itself.");
					}
				}   			

				this.entries.put(entryKey,
						new Entry(entry.getId()
								,entry.getTitle(),entry.getUpdated()
								,entry.getRights(),entry.getContent(),entry.getAuthors()
								,entry.getCategories(),entry.getContributors()
								,entry.getLinks(),entry.getAttributes()
								,entry.getExtensions(),entry.getPublished()
								,entry.getSummary(),entry.getSource()
						));
			}
		}
	}

	public SortedMap<String,Entry> getEntries() {
		if(entries == null){
			return null;
		}
		SortedMap<String,Entry> entriesCopy = 
			new TreeMap<String,Entry>(entries.comparator());
		Iterator<String> entryItr = entries.keySet().iterator();
		while(entryItr.hasNext()){
			String entryKey = entryItr.next();
			Entry entry = entries.get(entryKey);
			try{
				entriesCopy.put(entryKey,
						new Entry(entry.getId()
								,entry.getTitle(),entry.getUpdated()
								,entry.getRights(),entry.getContent(),entry.getAuthors()
								,entry.getCategories(),entry.getContributors()
								,entry.getLinks(),entry.getAttributes()
								,entry.getExtensions(),entry.getPublished()
								,entry.getSummary(),entry.getSource()
						));
			}catch(Exception e){
				//this should never happen because 
				//we check for errors on initial creation
				//but if it does, print the stack trace
				e.printStackTrace();
			}
		}
		return entriesCopy;
	}
	
	/**
     * 
     * @return the generator for this element.
     */
    public Generator getGenerator() {
        return source.getGenerator();
    }

    /**
     * 
     * @return the icon for this element.
     */
    public Icon getIcon() {
        return source.getIcon();
    }

    /**
     * 
     * @return the logo for this element.
     */
    public Logo getLogo() {
        return source.getLogo();
    }

    /**
     * 
     * @return the subtitle for this element.
     */
    public Subtitle getSubtitle() {
        return source.getSubtitle();
    }
    
    
    /**
	  * 
	  * @return the unique identifier for this entry.
	  */
    public Id getId(){
    	return source.getId();
    }
    
    /**
	  * 
	  * @return the title for this element.
	  */
    public Title getTitle(){
    	return source.getTitle();
    }
    
    /**
	  * 
	  * @return the updated date for this element.
	  */
    public Updated getUpdated(){
    	return source.getUpdated();
    }
    
    /**
	  * 
	  * @return the associated rights for this entry.
	  */
    public Rights getRights(){
    	return source.getRights();
    }
    
    /**
	  * 
	  * @return the authors for this entry.
	  */
    public List<Author> getAuthors(){
    	return source.getAuthors();
    }
    
    /**
	  * 
	  * @return the categories for this element.
	  */
    public List<Category> getCategories(){
    	return source.getCategories();
    }
    
    /**
	  * 
	  * @return the contributors for this entry.
	  */
    public List<Contributor> getContributors(){
    	return source.getContributors();
    }
    
    /**
	  * 
	  * @return the links for this entry.
	  */
    public List<Link> getLinks(){
    	return source.getLinks();
    }
    
    /**
	 * 
	 * @return the category attribute list.
	 */
    public List<Attribute> getAttributes(){
    	return source.getAttributes();
    }
    
    /**
	  * 
	  * @return the extensions for this entry.
	  */
    public List<Extension> getExtensions(){
    	return source.getExtensions();
    }
}
