/**
 * Copyright (C) 2009 William R. Brown <wbrown@colorfulsoftware.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
/* Change History:
 *  2006-11-14 wbrown - added javadoc documentation.
 *  2008-03-17 wbrown - made class immutable.
 *  2008-04-09 wbrown - wrapped checked exceptions.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents an Atom 1.0 source element.
 * 
 * @see <a
 *      href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom
 *      Syndication Format</a>
 * @author Bill Brown
 * 
 *         <pre>
 * 	atomSource =
 *          element atom:source {
 *          atomCommonAttributes,
 *          (atomAuthor*
 *          &amp; atomCategory*
 *          &amp; atomContributor*
 *          &amp; atomGenerator?
 *          &amp; atomIcon?
 *          &amp; atomId?
 *          &amp; atomLink*
 *          &amp; atomLogo?
 *          &amp; atomRights?
 *          &amp; atomSubtitle?
 *          &amp; atomTitle?
 *          &amp; atomUpdated?
 *          &amp; extensionElement*)
 *          }
 * </pre>
 */
public class Source implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4239639733689483651L;
	private final AtomEntrySourceAdaptor sourceAdaptor;
	private final Generator generator;
	private final Icon icon;
	private final Logo logo;
	private final Subtitle subtitle;

	// use the factory method in the FeedDoc.
	Source(Id id, Title title, Updated updated, Rights rights,
			List<Author> authors, List<Category> categories,
			List<Contributor> contributors, List<Link> links,
			List<Attribute> attributes, List<Extension> extensions,
			Generator generator, Subtitle subtitle, Icon icon, Logo logo)
			throws AtomSpecException {
		this.sourceAdaptor = new AtomEntrySourceAdaptor(id, title, updated,
				rights, authors, categories, contributors, links, attributes,
				extensions);
		this.generator = (generator == null) ? null : new Generator(generator
				.getAttributes(), generator.getText());
		this.subtitle = (subtitle == null) ? null : new Subtitle(subtitle);
		this.icon = (icon == null) ? null : new Icon(icon.getAttributes(), icon
				.getAtomUri());
		this.logo = (logo == null) ? null : new Logo(logo.getAttributes(), logo
				.getAtomUri());
	}

	/**
	 * 
	 * @return the generator for this element.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Generator getGenerator() throws AtomSpecException {
		return (generator == null) ? null : new Generator(generator
				.getAttributes(), generator.getText());
	}

	/**
	 * 
	 * @return the icon for this element.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Icon getIcon() throws AtomSpecException {
		return (icon == null) ? null : new Icon(icon.getAttributes(), icon
				.getAtomUri());
	}

	/**
	 * 
	 * @return the logo for this element.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Logo getLogo() throws AtomSpecException {
		return (logo == null) ? null : new Logo(logo.getAttributes(), logo
				.getAtomUri());
	}

	/**
	 * 
	 * @return the subtitle for this element.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Subtitle getSubtitle() throws AtomSpecException {
		return (subtitle == null) ? null : new Subtitle(subtitle);
	}

	/**
	 * 
	 * @return the unique identifier for this entry.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Id getId() throws AtomSpecException {
		return sourceAdaptor.getId();
	}

	/**
	 * 
	 * @return the title for this element.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Title getTitle() throws AtomSpecException {
		return sourceAdaptor.getTitle();
	}

	/**
	 * 
	 * @return the updated date for this element.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Updated getUpdated() throws AtomSpecException {
		return sourceAdaptor.getUpdated();
	}

	/**
	 * 
	 * @return the associated rights for this entry.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Rights getRights() throws AtomSpecException {
		return sourceAdaptor.getRights();
	}

	/**
	 * 
	 * @return the authors for this entry.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public List<Author> getAuthors() throws AtomSpecException {
		return sourceAdaptor.getAuthors();
	}

	/**
	 * 
	 * @return the categories for this element.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public List<Category> getCategories() throws AtomSpecException {
		return sourceAdaptor.getCategories();
	}

	/**
	 * 
	 * @return the contributors for this entry.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public List<Contributor> getContributors() throws AtomSpecException {
		return sourceAdaptor.getContributors();
	}

	/**
	 * 
	 * @return the links for this entry.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public List<Link> getLinks() throws AtomSpecException {
		return sourceAdaptor.getLinks();
	}

	/**
	 * 
	 * @return the category attribute list.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public List<Attribute> getAttributes() throws AtomSpecException {
		return sourceAdaptor.getAttributes();
	}

	/**
	 * 
	 * @return the extensions for this entry.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public List<Extension> getExtensions() throws AtomSpecException {
		return sourceAdaptor.getExtensions();
	}

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Attribute getAttribute(String attrName) throws AtomSpecException {
		return sourceAdaptor.getAttribute(attrName);
	}

	/**
	 * @param name
	 *            the name of the author to get.
	 * @return the Author object if the name matches or null if not found.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Author getAuthor(String name) throws AtomSpecException {
		return sourceAdaptor.getAuthor(name);
	}

	/**
	 * @param termValue
	 *            the term value.
	 * @return the Category object if the term matches or null if not found.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Category getCategory(String termValue) throws AtomSpecException {
		return sourceAdaptor.getCategory(termValue);
	}

	/**
	 * @param name
	 *            the name of the author to get.
	 * @return the Contributor object if the name matches or null if not found.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Contributor getContributor(String name) throws AtomSpecException {
		return sourceAdaptor.getContributor(name);
	}

	/**
	 * @param hrefVal
	 *            the href attribute value to look for.
	 * @return the Link object if href matches or null if not found.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Link getLink(String hrefVal) throws AtomSpecException {
		return sourceAdaptor.getLink(hrefVal);
	}

	/**
	 * @param extName
	 *            the element name of the extension to get.
	 * @return the Extension object if extName matches or null if not found.
	 * @throws AtomSpecException
	 *             if the format of the data is not valid.
	 */
	public Extension getExtension(String extName) throws AtomSpecException {
		return sourceAdaptor.getExtension(extName);
	}
}
