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

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an Atom 1.0 entry element.
 * @see http://www.atomenabled.org/developers/syndication/atom-format-spec.php
 * @author wbrown
 * <code>
 *  atomEntry =
 *    element atom:entry {
 *     atomCommonAttributes,
 *     (atomAuthor*
 *      & atomCategory*
 *      & atomContent?
 *      & atomContributor*
 *      & atomId
 *      & atomLink*
 *      & atomPublished?
 *      & atomRights?
 *      & atomSource?
 *      & atomSummary?
 *      & atomTitle
 *      & atomUpdated
 *      & extensionElement*)
 *      }
 * </code>
 */
public class Entry {
    
    private List attributes;
    private List authors;
    private List categories;
    private Content content;
    private List contributors;
    private Id id;
    private List links;
    private Published published;
    private Rights rights;
    private Source source;
    private Summary summary;
    private Title title;
    private Updated updated;
    private List extensions;
    
    public Entry(){
    	//nothing specific here.
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
