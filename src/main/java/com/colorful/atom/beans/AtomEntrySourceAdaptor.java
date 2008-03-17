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
 *  2008-03-17 wbrown - added to share elements between entry and source.
 */
package com.colorful.atom.beans;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class serves as an adaptor for sharing the implementation of the comment elements
 * between the entry and source element.
 * 
 * @author billbrown
 *
 */
public abstract class AtomEntrySourceAdaptor {

	private final List<Attribute> attributes;
	private final List<Author> authors;
	private final List<Category> categories;
	private final List<Contributor> contributors;
	private final List<Link> links;
	private final List<Extension> extensions;
	private final Id id; 
	private final Title title;
	private final Updated updated;
	private final Rights rights;

	public AtomEntrySourceAdaptor(Id id
			,Title title
			,Updated updated
			,Rights rights
			,List<Author> authors
			,List<Category> categories    		
			,List<Contributor> contributors
			,List<Link> links
			,List<Attribute> attributes
			,List<Extension> extensions
	) throws AtomSpecException {
		this.id = (id == null)?null: new Id(id.getAtomUri(),id.getAttributes());
		this.title = (title == null)?null: new Title(title.getText(),title.getAttributes());
		this.updated = (updated == null)?null: new Updated(updated.getDateTime());
		this.rights = (rights == null)?null: new Rights(rights.getText(),rights.getAttributes());

		if(authors == null){
			this.authors = null;
		}else{
			this.authors = new LinkedList<Author>();
			Iterator<Author> authItr = authors.iterator();
			while(authItr.hasNext()){
				Author author = authItr.next();
				this.authors.add(new Author(author.getName()
						,author.getUri()
						,author.getEmail()
						,author.getAttributes()
						,author.getExtensions()));
			}
		}

		if(categories == null){
			this.categories = null;
		}else{
			this.categories = new LinkedList<Category>();
			Iterator<Category> catItr = categories.iterator();
			while(catItr.hasNext()){
				Category category = catItr.next();
				this.categories.add(new Category(category.getTerm()
						,category.getScheme()
						,category.getLabel()
						,category.getAttributes()
						,category.getContent()));
			}
		}

		if(contributors == null){
			this.contributors = null;
		}else{
			this.contributors = new LinkedList<Contributor>();
			Iterator<Contributor> contribItr = contributors.iterator();
			while(contribItr.hasNext()){
				Contributor contributor = contribItr.next();
				this.contributors.add(new Contributor(contributor.getName(),contributor.getUri()
						,contributor.getEmail(),contributor.getAttributes(),contributor.getExtensions()));
			}
		}

		if(links == null){
			this.links = null;
		}else{
			this.links = new LinkedList<Link>();
			Iterator<Link> linkItr = links.iterator();
			while(linkItr.hasNext()){
				Link link = linkItr.next();
				this.links.add(new Link(link.getHref()
						,link.getRel()
						,link.getType()
						,link.getHreflang()
						,link.getTitle()
						,link.getLength()
						,link.getAttributes()
						,link.getContent()));
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

	/**
	 * 
	 * @return the category attribute list.
	 */
	 public List<Attribute> getAttributes() {
		if(attributes == null){
			return null;
		}
		List<Attribute> attrsCopy = new LinkedList<Attribute>();
		Iterator<Attribute> attrItr = this.attributes.iterator();
		while(attrItr.hasNext()){
			Attribute attr = attrItr.next();
			attrsCopy.add(new Attribute(attr.getName(),attr.getValue()));
		}
		return attrsCopy;
	 }

	 /**
	  * 
	  * @return the authors for this entry.
	  */
	 public List<Author> getAuthors() {
		 if(authors == null){
			 return null;
		 }
		 List<Author> authrosCopy = new LinkedList<Author>();
		 Iterator<Author> authItr = authors.iterator();
		 while(authItr.hasNext()){
			 Author author = authItr.next();
			 try{
				 authrosCopy.add(new Author(author.getName()
						 ,author.getUri()
						 ,author.getEmail()
						 ,author.getAttributes()
						 ,author.getExtensions()));
			 }catch(Exception e){
				 //this should never happen because 
				 //we check for errors on initial creation
				 //but if it does, print the stack trace
				 e.printStackTrace();
			 }
		 }
		 return authrosCopy;
	 }

	 /**
	  * 
	  * @return the categories for this element.
	  */
	 public List<Category> getCategories() {
		 if(categories == null){
			 return null;
		 }
		 List<Category> catsCopy = new LinkedList<Category>();
		 Iterator<Category> catItr = categories.iterator();
		 while(catItr.hasNext()){
			 Category category = catItr.next();
			 try{
				 catsCopy.add(new Category(category.getTerm()
						 ,category.getScheme()
						 ,category.getLabel()
						 ,category.getAttributes()
						 ,category.getContent()));
			 }catch(Exception e){
				 //this should never happen because 
				 //we check for errors on initial creation
				 //but if it does, print the stack trace
				 e.printStackTrace();
			 }
		 }
		 return catsCopy;
	 }

	 /**
	  * 
	  * @return the contributors for this entry.
	  */
	 public List<Contributor> getContributors() {
		 if(contributors == null){
			 return null;
		 }
		 List<Contributor> contsCopy = new LinkedList<Contributor>();
		 Iterator<Contributor> contribItr = contributors.iterator();
		 while(contribItr.hasNext()){
			 Contributor contributor = contribItr.next();
			 try{
				 contsCopy.add(new Contributor(contributor.getName(),contributor.getUri()
						 ,contributor.getEmail(),contributor.getAttributes(),contributor.getExtensions()));
			 }catch(Exception e){
				 //this should never happen because 
				 //we check for errors on initial creation
				 //but if it does, print the stack trace
				 e.printStackTrace();
			 }
		 }
		 return contsCopy;
	 }

	 /**
	  * 
	  * @return the extensions for this entry.
	  */
	 public List<Extension> getExtensions() {
		 if(extensions == null){
			 return null;
		 }
		 List<Extension> extsCopy = new LinkedList<Extension>();
		 Iterator<Extension> extItr = extensions.iterator();
		 while(extItr.hasNext()){
			 Extension extension = extItr.next();
			 extsCopy.add(new Extension(extension.getElementName(),extension.getContent(),extension.getAttributes()));
		 }
		 return extsCopy;
	 }

	 /**
	  * 
	  * @return the links for this entry.
	  */
	 public List<Link> getLinks() {
		 if(links == null){
			 return null;
		 }
		 List<Link> linksCopy = new LinkedList<Link>();
		 Iterator<Link> linkItr = links.iterator();
		 while(linkItr.hasNext()){
			 Link link = linkItr.next();
			 try{
				 linksCopy.add(new Link(link.getHref()
						 ,link.getRel()
						 ,link.getType()
						 ,link.getHreflang()
						 ,link.getTitle()
						 ,link.getLength()
						 ,link.getAttributes()
						 ,link.getContent()));
			 }catch(Exception e){
				 //this should never happen because 
				 //we check for errors on initial creation
				 //but if it does, print the stack trace
				 e.printStackTrace();
			 }
		 }
		 return linksCopy;
	 }

	 /**
	  * 
	  * @return the unique identifier for this entry.
	  */
	 public Id getId() {
		 return (id == null)?null:new Id(id.getAtomUri(),id.getAttributes());
	 }

	 /**
	  * 
	  * @return the associated rights for this entry.
	  */
	 public Rights getRights() {
		 return (rights == null)?null:new Rights(rights.getText(),rights.getAttributes());
	 }

	 /**
	  * 
	  * @return the title for this element.
	  */
	 public Title getTitle() {
		 return (title == null)?null: new Title(title.getText(),title.getAttributes());
	 }

	 /**
	  * 
	  * @return the updated date for this element.
	  */
	 public Updated getUpdated() {
		 return (updated == null)?null:new Updated(updated.getDateTime());
	 }
}
