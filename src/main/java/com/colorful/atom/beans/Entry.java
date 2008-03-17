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
 */
package com.colorful.atom.beans;

import java.util.Iterator;
import java.util.LinkedList;
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
public class Entry {
    
    private final List<Attribute> attributes;
    private final List<Author> authors;
    private final List<Category> categories;
    private final Content content;
    private final List<Contributor> contributors;
    private final Id id;
    private final List<Link> links;
    private final Published published;
    private final Rights rights;
    private final Source source;
    private final Summary summary;
    private final Title title;
    private final Updated updated;
    private final List<Extension> extensions;
    
    public Entry(Id id
    		,Title title
    		,Updated updated
    		,List<Author> authors
    		,List<Category> categories
    		,Content content
    		,List<Contributor> contributor
    		,List<Link> link
    		,Published published
    		,Rights rights
    		,Source source
    		,Summary summary
    		,List<Attribute> attributes
    		,List<Extension> extensions
    		){
    	this.id = (id == null)?null: new Id(id.getAtomUri(),id.getAttributes());
    	this.title = (title == null)?null: new Title(title.getText(),title.getAttributes());
    	this.updated = (updated == null)?null: new Updated(updated.getUpdated());
    	this.content = (content == null)?null: new Content(content.getContent(),content.getAttributes());
    	this.published = (published == null)?null: new Published(published.getPublished());
    	this.rights = (rights == null)?null: new Rights(rights.getText(),rights.getAttributes());
    	this.source = (source == null)?null: new Source();
    	this.summary = (summary == null)?null: new Summary(summary.getText(),summary.getAttributes());
    	
    	if(authors == null){
    		this.authors = null;
    	}else{
    		this.authors = new LinkedList<Author>();
    		Iterator<Author> attrItr = authors.iterator();
    		while(attrItr.hasNext()){
    			Author author = attrItr.next();
    			this.authors.add(new Author(author.getName()
    					,author.getUri()
    					,author.getEmail()
    					,author.getAttributes()
    					,author.getExtensions()));
    		}
    	}
    	
    	if(extensions == null){
    		this.extensions = null;
    	}else{
    		this.extensions = new LinkedList<Extension>();
    		Iterator<Extension> extItr = extensions.iterator();
    		while(extItr.hasNext()){
    			Extension extension = extItr.next();
    			this.extensions.add(new Extension(extension.getElementName(),extension.getContent(),extension.getAttributes()));
    		}
    	}
    	
    	if(attributes == null){
    		this.attributes = null;
    	}else{
    		this.attributes = new LinkedList<Attribute>();
    		Iterator<Attribute> attrItr = attributes.iterator();
    		while(attrItr.hasNext()){
    			Attribute attr = attrItr.next();
    			this.attributes.add(new Attribute(attr.getName(),attr.getValue()));
    		}
    	}
    	
    	if(extensions == null){
    		this.extensions = null;
    	}else{
    		this.extensions = new LinkedList<Extension>();
    		Iterator<Extension> extItr = extensions.iterator();
    		while(extItr.hasNext()){
    			Extension extension = extItr.next();
    			this.extensions.add(new Extension(extension.getElementName(),extension.getContent(),extension.getAttributes()));
    		}
    	}
    	
    	if(attributes == null){
    		this.attributes = null;
    	}else{
    		this.attributes = new LinkedList<Attribute>();
    		Iterator<Attribute> attrItr = attributes.iterator();
    		while(attrItr.hasNext()){
    			Attribute attr = attrItr.next();
    			this.attributes.add(new Attribute(attr.getName(),attr.getValue()));
    		}
    	}
    	
    	if(extensions == null){
    		this.extensions = null;
    	}else{
    		this.extensions = new LinkedList<Extension>();
    		Iterator<Extension> extItr = extensions.iterator();
    		while(extItr.hasNext()){
    			Extension extension = extItr.next();
    			this.extensions.add(new Extension(extension.getElementName(),extension.getContent(),extension.getAttributes()));
    		}
    	}
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
    public Content getContent() {
        return content;
    }
    public void setContent(Content content) {
        this.content = content;
    }
    public List getContributors() {
        return contributors;
    }
    public void setContributors(List contributors) {
        this.contributors = contributors;
    }
    public List getExtensions() {
        return extensions;
    }
    public void setExtensions(List extensions) {
        this.extensions = extensions;
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
    public Published getPublished() {
        return published;
    }
    public void setPublished(Published published) {
        this.published = published;
    }
    public Rights getRights() {
        return rights;
    }
    public void setRights(Rights rights) {
        this.rights = rights;
    }
    public Source getSource() {
        return source;
    }
    public void setSource(Source source) {
        this.source = source;
    }
    public Summary getSummary() {
        return summary;
    }
    public void setSummary(Summary summary) {
        this.summary = summary;
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
}
