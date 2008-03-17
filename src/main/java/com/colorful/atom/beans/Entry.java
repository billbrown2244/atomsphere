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
 *  2008-03-16 wbrown - made class immutable.
 */
package com.colorful.atom.beans;

import java.util.Iterator;
import java.util.List;

/**
 * This class represents an Atom 1.0 entry element.
 * @see <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom Syndication Format</a>
 * @author Bill Brown
 *  <pre>
 *      atomEntry =
 *          element atom:entry {
 *          atomCommonAttributes,
 *          (atomAuthor*
 *          & atomCategory*
 *          & atomContent?
 *          & atomContributor*
 *          & atomId
 *          & atomLink*
 *          & atomPublished?
 *          & atomRights?
 *          & atomSource?
 *          & atomSummary?
 *          & atomTitle
 *          & atomUpdated
 *          & extensionElement*)
 *          }
 * </pre>
 */
public class Entry extends AtomEntrySourceAdaptor{
    
	private final Content content;         
    private final Published published;
    private final Source source;
    private final Summary summary;
    
    public Entry(Id id
    		,Title title
    		,Updated updated
    		,Rights rights
    		,List<Author> authors
    		,List<Category> categories    		
    		,List<Contributor> contributors
    		,List<Link> links
    		,List<Attribute> attributes
    		,List<Extension> extensions    		
    		,Published published
    		,Summary summary
    		,Source source
    		,Content content) throws AtomSpecException {
    	
    	super(id,title,updated,rights,authors,categories,contributors,links,attributes,extensions);

    	//check for functional requirements here because 
    	//they are all optional for a Source element.
    	
    	//make sure id is present
        if(id == null){
            throw new AtomSpecException("atom:entry elements MUST contain exactly one atom:id element.");
        }
        //make sure title is present
        if(title == null){
            throw new AtomSpecException("atom:entry elements MUST contain exactly one atom:title element.");
        }
        //make sure updated is present
        if(updated == null){
            throw new AtomSpecException("atom:entry elements MUST contain exactly one atom:updated element.");
        }
        
        if(content == null){
        	this.content = null;
        }else{
	        
	        Iterator<Attribute> attrsItr = content.getAttributes().iterator();
	        while(attrsItr.hasNext()){
	            Attribute attr = (Attribute)attrsItr.next();
	            //check for src attribute
	            if(attr.getName().equals("src") && summary == null){
	            	throw new AtomSpecException("atom:entry elements MUST contain an atom:summary element in either of the following cases: the atom:entry contains an atom:content that has a \"src\" attribute (and is thus empty).");
	            }
	            //check for valid xml mime type.
	            if(attr.getName().equals("type")
	            	&& !attr.getValue().startsWith("text/")
	            	&& !attr.getValue().endsWith("/xml")
	            	&& !attr.getValue().endsWith("\\+xml"))
	            	throw new AtomSpecException("the atom:entry contains content that is encoded in Base64; i.e., the \"type\" attribute of atom:content is a MIME media type [MIMEREG], but is not an XML media type [RFC3023], does not begin with \"text/\", and does not end with \"/xml\" or \"+xml\".");
	            }
	        
        	this.content = new Content(content.getContent(),content.getAttributes());
        }
        
    	this.published = (published == null)?null: new Published(published.getDateTime());
    	this.source = (source == null)?null: new Source(source.getId(),source.getTitle()
    			,source.getUpdated(),source.getRights(),source.getAuthors()
    			,source.getCategories(),source.getContributors(),source.getLinks()
    			,source.getAttributes(),source.getExtensions(),source.getGenerator()
    			,source.getSubtitle(),source.getIcon(),source.getLogo());
    	this.summary = (summary == null)?null: new Summary(summary.getText(),summary.getAttributes());
    }

    /**
     * 
     * @return the content for this entry.
     */
    public Content getContent() {
    	return (content == null)?null:new Content(content.getContent()
    			,content.getAttributes());
    }

    /**
     * 
     * @return the published date for this entry.
     */
    public Published getPublished() {
    	return (published == null)?null:new Published(published.getDateTime());
    }

    /**
     * 
     * @return the source for this element.
     */
    public Source getSource() {
    	try{
    		return (source == null)?null: new Source(source.getId(),source.getTitle()
    				,source.getUpdated(),source.getRights(),source.getAuthors()
    				,source.getCategories(),source.getContributors(),source.getLinks()
    				,source.getAttributes(),source.getExtensions(),source.getGenerator()
    				,source.getSubtitle(),source.getIcon(),source.getLogo());
    	}catch(Exception e){
			//this should never happen because 
			//we check for errors on initial creation
			//but if it does, print the stack trace
			e.printStackTrace();
			return null;
		}
    }

    /**
     * 
     * @return the summary for this element.
     */
    public Summary getSummary() {
        return (summary == null)?null: new Summary(summary.getText(),summary.getAttributes());
    }
}
