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

import java.util.List;


/**
 * This class represents an Atom 1.0 author.
 * @see <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom Syndication Format</a>
 * @author Bill Brown
 *  <pre>
 *      atomAuthor = element atom:author { atomPersonConstruct }
 *  </pre>
 */
public class Author {
	
	private final AtomPersonConstruct person;
	
	//use the factory method in the FeedDoc.
	Author(Name name, URI uri, Email email, List<Attribute> attributes
			, List<Extension> extensions) throws AtomSpecException{
		this.person = new AtomPersonConstruct(name,uri,email,attributes,extensions);
	}
	
	public Name getName(){
		return person.getName();
	}
	
	public URI getUri(){
		return person.getUri();
	}
	
	public Email getEmail(){
		return person.getEmail();
	}
	
	public List<Attribute> getAttributes(){
		return person.getAttributes();
	}
	
	public List<Extension> getExtensions(){
		return person.getExtensions();
	}
}
