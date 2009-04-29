/**
 * Copyright (C) 2009 William R. Brown <info@colorfulsoftware.com>
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
package com.colorful.atom;

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
		this.subtitle = (subtitle == null) ? null : new Subtitle(subtitle
				.getText(), subtitle.getAttributes());
		this.icon = (icon == null) ? null : new Icon(icon.getAttributes(), icon
				.getAtomUri());
		this.logo = (logo == null) ? null : new Logo(logo.getAttributes(), logo
				.getAtomUri());
	}

	/**
	 * 
	 * @return the generator for this element.
	 */
	public Generator getGenerator() {
		try {
			return (generator == null) ? null : new Generator(generator
					.getAttributes(), generator.getText());
		} catch (Exception e) {
			// we should never get here.
			return null;
		}
	}

	/**
	 * 
	 * @return the icon for this element.
	 */
	public Icon getIcon() {
		try {
			return (icon == null) ? null : new Icon(icon.getAttributes(), icon
					.getAtomUri());
		} catch (Exception e) {
			// we should never get here.
			return null;
		}
	}

	/**
	 * 
	 * @return the logo for this element.
	 */
	public Logo getLogo() {
		try {
			return (logo == null) ? null : new Logo(logo.getAttributes(), logo
					.getAtomUri());
		} catch (Exception e) {
			// we should never get here.
			return null;
		}
	}

	/**
	 * 
	 * @return the subtitle for this element.
	 */
	public Subtitle getSubtitle() {
		try {
			return (subtitle == null) ? null : new Subtitle(subtitle.getText(),
					subtitle.getAttributes());
		} catch (Exception e) {
			// we should never get here.
			return null;
		}
	}

	/**
	 * 
	 * @return the unique identifier for this entry.
	 */
	public Id getId() {
		return sourceAdaptor.getId();
	}

	/**
	 * 
	 * @return the title for this element.
	 */
	public Title getTitle() {
		return sourceAdaptor.getTitle();
	}

	/**
	 * 
	 * @return the updated date for this element.
	 */
	public Updated getUpdated() {
		return sourceAdaptor.getUpdated();
	}

	/**
	 * 
	 * @return the associated rights for this entry.
	 */
	public Rights getRights() {
		return sourceAdaptor.getRights();
	}

	/**
	 * 
	 * @return the authors for this entry.
	 */
	public List<Author> getAuthors() {
		return sourceAdaptor.getAuthors();
	}

	/**
	 * 
	 * @return the categories for this element.
	 */
	public List<Category> getCategories() {
		return sourceAdaptor.getCategories();
	}

	/**
	 * 
	 * @return the contributors for this entry.
	 */
	public List<Contributor> getContributors() {
		return sourceAdaptor.getContributors();
	}

	/**
	 * 
	 * @return the links for this entry.
	 */
	public List<Link> getLinks() {
		return sourceAdaptor.getLinks();
	}

	/**
	 * 
	 * @return the category attribute list.
	 */
	public List<Attribute> getAttributes() {
		return sourceAdaptor.getAttributes();
	}

	/**
	 * 
	 * @return the extensions for this entry.
	 */
	public List<Extension> getExtensions() {
		return sourceAdaptor.getExtensions();
	}
}
