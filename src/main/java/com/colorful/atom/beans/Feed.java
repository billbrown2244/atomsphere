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

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
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
		SortedMap<String,Entry> entriesCopy = new TreeMap<String,Entry>();
		Iterator<String> entryItr = entries.keySet().iterator();
		while(entryItr.hasNext()){
			Entry entry = entries.get(entryItr.next());
			try{
				entriesCopy.put(entry.getUpdated().getText(),
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
	 * This method sorts the entries of the feed.  The default ordering of the entries is by updated descending 
	 * @param comparator this gives the ordering for the entries.
	 * @param sortInstance an instance of the type to sort by.
	 */
	public Feed sortEntries(Comparator<String> comparator, Object sortInstance) throws AtomSpecException{

		if(getEntries() != null){
			//sort the entries with the passed in instance as the key
			SortedMap<String,Entry> resortedEntries = new TreeMap<String,Entry>(comparator);
			SortedMap<String,Entry> currentEntries = getEntries();
			Iterator<Entry> entryItr = currentEntries.values().iterator();
			while(entryItr.hasNext()){
				Entry entry = (Entry)entryItr.next();
				if (sortInstance instanceof Updated){
					resortedEntries.put(entry.getUpdated().getText(),entry);
				}
				if (sortInstance instanceof Title){
					resortedEntries.put(entry.getTitle().getText(),entry);
				}
				if (sortInstance instanceof Summary){
					resortedEntries.put(entry.getSummary().getText(),entry);
				}
			}

			//rebuild the top level feed attributes to include the sort
			//if it isn't already there.
			List<Attribute> localFeedAttrs = new LinkedList<Attribute>();
			Attribute attrLocal = FeedDoc.buildAttribute("xmlns:sort","http://www.colorfulsoftware.com/projects/atomsphere/extension/sort/1.0");
			if(getAttributes() == null){
				localFeedAttrs.add(attrLocal);
			}else{
				List<Attribute> currentAttributes = getAttributes();
				Iterator<Attribute> attrItr = currentAttributes.iterator();
				while(attrItr.hasNext()){
					Attribute attr = (Attribute)attrItr.next();
					if(!attr.getName().equals(attrLocal.getName())
							&& !attr.getValue().equals(attrLocal.getValue())){
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
			if(sortInstance instanceof Updated){
				sortElement = FeedDoc.buildAttribute("type","updated");
			}else if(sortInstance instanceof Title){
				sortElement = FeedDoc.buildAttribute("type","title");
			}else if(sortInstance instanceof Summary){
				sortElement = FeedDoc.buildAttribute("type","summary");
			}
			List<Attribute> extAttrs = new LinkedList<Attribute>();
			extAttrs.add(sortElement);
			Extension localFeedExtension = FeedDoc.buildExtension(elementName,extAttrs,null); 

			//rebuild the extensions
			//we have to look for the sort extension and 
			//replace any occurrences of it with the one we just created.
			List<Extension> localFeedExtensions = new LinkedList<Extension>();
			if(getExtensions() == null){
				localFeedExtensions.add(localFeedExtension);
			}else{			
				List<Extension> currentExtensions = getExtensions();
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
			return FeedDoc.buildFeed(this.getId(),this.getTitle(),this.getUpdated(),this.getRights()
				,this.getAuthors(),this.getCategories(),this.getContributors()
				,this.getLinks(),localFeedAttrs,localFeedExtensions,this.getGenerator()
				,this.getSubtitle(),this.getIcon(),this.getLogo(),resortedEntries);
		}
		//return the feed unsorted.
		return this;
	}

	/**
	 * Checks the namespace argument and applies the extension if it is recognized by the atomsphere library.
	 * @param xmlns the structuredExtension element's.
	 */
	protected Feed checkForAndApplyExtension(Attribute xmlns) {

		//if there aren't any attributes for the feed and thus no xmlns:sort attr
		//return the defaults.
		if(getAttributes() == null){
			return this;
		}

		//check for the first supported extension
		//currently only sort is implemented.
		Iterator<Attribute> attrs = getAttributes().iterator();
		while(attrs.hasNext()){
			Attribute attr = (Attribute)attrs.next();
			if(attr.equals(xmlns)){
				try{
					return applySort();
				}catch(Exception e){
					//this should never happen because 
					//we check for errors on initial creation
					//but if it does, print the stack trace
					e.printStackTrace();
				}
			}
		}
		return this;
	}

	//check for and apply the first sort extension.
	private Feed applySort() throws AtomSpecException{
		//only do the work if there are extensions.
		if(getExtensions() != null){
			//look for the first extension element if the namespace exists.
			Iterator<Extension> extItr = getExtensions().iterator();
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
								return sortEntries(FeedDoc.SORT_ASC, FeedDoc.buildUpdated(null)); 
							}
							if(value.equals("title")){
								return sortEntries(FeedDoc.SORT_ASC, FeedDoc.buildTitle(null,null));
							}
							if(value.equals("summary")){
								return sortEntries(FeedDoc.SORT_ASC, FeedDoc.buildSummary(null,null));
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
								return sortEntries(FeedDoc.SORT_DESC, FeedDoc.buildUpdated(null)); 
							}
							if(value.equals("title")){
								return sortEntries(FeedDoc.SORT_DESC, FeedDoc.buildTitle(null,null));
							}
							if(value.equals("summary")){
								return sortEntries(FeedDoc.SORT_DESC, FeedDoc.buildSummary(null,null));
							}
						}
					}
				}
			}
		}
		return this;
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
