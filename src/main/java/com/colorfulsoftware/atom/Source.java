/**
 * Copyright 2011 Bill Brown
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* Change History:
 *  2006-11-14 wbrown - added javadoc documentation.
 *  2008-03-17 wbrown - made class immutable.
 *  2008-04-09 wbrown - wrapped checked exceptions.
 */
package com.colorfulsoftware.atom;

import java.io.Serializable;
import java.util.LinkedList;
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
	private List<String> unboundPrefixes = null;

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
		this.generator = (generator == null) ? null : new Generator(generator);
		this.subtitle = (subtitle == null) ? null : new Subtitle(subtitle);
		this.icon = (icon == null) ? null : new Icon(icon);
		this.logo = (logo == null) ? null : new Logo(logo);

		this.unboundPrefixes = new LinkedList<String>();

		// check that the extension prefixes are bound to a namespace
		if (sourceAdaptor.getUnboundPrefixes() != null) {
			this.unboundPrefixes.addAll(sourceAdaptor.getUnboundPrefixes());
		}

		if (generator != null && generator.getUnboundPrefixes() != null) {
			for (String unboundPrefix : generator.getUnboundPrefixes()) {
				if (getAttribute("xmlns:" + unboundPrefix) == null) {
					this.unboundPrefixes.add(unboundPrefix);
				}
			}
		}

		if (subtitle != null && subtitle.getUnboundPrefixes() != null) {
			for (String unboundPrefix : subtitle.getUnboundPrefixes()) {
				if (getAttribute("xmlns:" + unboundPrefix) == null) {
					this.unboundPrefixes.add(unboundPrefix);
				}
			}
		}

		if (icon != null && icon.getUnboundPrefixes() != null) {
			for (String unboundPrefix : icon.getUnboundPrefixes()) {
				if (getAttribute("xmlns:" + unboundPrefix) == null) {
					this.unboundPrefixes.add(unboundPrefix);
				}
			}
		}

		if (logo != null && logo.getUnboundPrefixes() != null) {
			for (String unboundPrefix : logo.getUnboundPrefixes()) {
				if (getAttribute("xmlns:" + unboundPrefix) == null) {
					this.unboundPrefixes.add(unboundPrefix);
				}
			}
		}

	}

	Source(Source source) {
		this.sourceAdaptor = new AtomEntrySourceAdaptor(source.sourceAdaptor);
		this.generator = source.getGenerator();
		this.icon = source.getIcon();
		this.logo = source.getLogo();
		this.subtitle = source.getSubtitle();
		this.unboundPrefixes = source.getUnboundPrefixes();
	}

	/**
	 * 
	 * @return the generator for this element.
	 */
	public Generator getGenerator() {
		return (generator == null) ? null : new Generator(generator);
	}

	/**
	 * 
	 * @return the icon for this element.
	 */
	public Icon getIcon() {
		return (icon == null) ? null : new Icon(icon);
	}

	/**
	 * 
	 * @return the logo for this element.
	 */
	public Logo getLogo() {
		return (logo == null) ? null : new Logo(logo);
	}

	/**
	 * 
	 * @return the subtitle for this element.
	 */
	public Subtitle getSubtitle() {
		return (subtitle == null) ? null : new Subtitle(subtitle);
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

	/**
	 * @param attrName
	 *            the name of the attribute to get.
	 * @return the Attribute object if attrName matches or null if not found.
	 */
	public Attribute getAttribute(String attrName) {
		return sourceAdaptor.getAttribute(attrName);
	}

	/**
	 * @param name
	 *            the name of the author to get.
	 * @return the Author object if the name matches or null if not found.
	 */
	public Author getAuthor(String name) {
		return sourceAdaptor.getAuthor(name);
	}

	/**
	 * @param termValue
	 *            the term value.
	 * @return the Category object if the term matches or null if not found.
	 */
	public Category getCategory(String termValue) {
		return sourceAdaptor.getCategory(termValue);
	}

	/**
	 * @param name
	 *            the name of the author to get.
	 * @return the Contributor object if the name matches or null if not found.
	 */
	public Contributor getContributor(String name) {
		return sourceAdaptor.getContributor(name);
	}

	/**
	 * 
	 * @param relAttributeValue
	 *            the value of the rel attribute.
	 * @return the Link object based on the semantics of the rel attribute of
	 *         the link element. See <a href=
	 *         "http://www.atomenabled.org/developers/syndication/atom-format-spec.php#element.link"
	 *         >atom:link</a>.
	 */
	public Link getLink(String relAttributeValue) {
		return sourceAdaptor.getLink(relAttributeValue);
	}

	/**
	 * @param extName
	 *            the element name of the extension to get.
	 * @return the Extension object if extName matches or null if not found.
	 */
	public Extension getExtension(String extName) {
		return sourceAdaptor.getExtension(extName);
	}

	/**
	 * Shows the contents of the &lt;source> element.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("<source");
		sb.append(sourceAdaptor);

		if (generator != null) {
			sb.append(generator);
		}

		if (subtitle != null) {
			sb.append(subtitle);
		}

		if (icon != null) {
			sb.append(icon);
		}

		if (logo != null) {
			sb.append(logo);
		}

		sb.append("</source>");
		return sb.toString();
	}

	List<String> getUnboundPrefixes() {
		return unboundPrefixes;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Source)) {
			return false;
		}
		return this.toString().equals(obj.toString());
	}
	
	@Override public int hashCode() {
		return toString().hashCode();
	}
}
