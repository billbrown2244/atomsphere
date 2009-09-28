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
 *  2006-11-14 wbrown - added javadoc documentation
 *  2007-02-19 wbrown - change looping through entries 
 *  					to be the same for all projects.
 *                      added support for writing empty extension elements.
 *  2007-06-20 wbrown - change the scope of writing entries to protected 
 *  					so that the FeedDoc.readEntryToString(Entry entry)  
 *  					will work.
 *  2008-03-11 wbrown - fix bug for atomXHTMLTextConstruct to 
 *  					wrap contents in xhtml:div element.
 *  2008-04-09 wbrown - add the atomsphere generator tag to the output. 
 *  2008-04-17 wbrown - move document encoding back to FeedDoc.
 *  2008-09-22 wbrown - fix issue with xhtml attribute values.
 */
package com.colorfulsoftware.atom;

import java.util.List;
import java.util.SortedMap;

import javax.xml.stream.XMLStreamWriter;

/**
 * Used by the FeedDoc to write a Feed bean to a xml file or java String.
 * 
 * @author Bill Brown
 * 
 */
class FeedWriter {

	private FeedDoc feedDoc = new FeedDoc();

	// used internally by FeedDoc to write feed to output streams.
	void writeFeed(XMLStreamWriter writer, Feed feed) throws AtomSpecException {
		try {
			// open the feed element
			writer.writeStartElement("feed");
			// write the attributes.
			writeAttributes(writer, feed.getAttributes());
			// write the id (REQUIRED)
			writeID(writer, feed.getId());
			// write the updated date (REQUIRED)
			writeUpdated(writer, feed.getUpdated());
			// write the generator (should be required but isn't)
			if (feed.getGenerator() != null) {
				writeGenerator(writer, feed.getGenerator());
			}
			// write the title (REQUIRED)
			writeTitle(writer, feed.getTitle());

			// write the subtitle
			if (feed.getSubtitle() != null) {
				writeSubtitle(writer, feed.getSubtitle());
			}

			// write the author
			if (feed.getAuthors() != null) {
				writeAuthors(writer, feed.getAuthors());
			}
			// write the contributors
			if (feed.getContributors() != null) {
				writeContributors(writer, feed.getContributors());
			}
			// write the links
			if (feed.getLinks() != null) {
				writeLinks(writer, feed.getLinks());
			}
			// write the categories
			if (feed.getCategories() != null) {
				writeCategories(writer, feed.getCategories());
			}
			// write the icon
			if (feed.getIcon() != null) {
				writeIcon(writer, feed.getIcon());
			}
			// write the logo
			if (feed.getLogo() != null) {
				writeLogo(writer, feed.getLogo());
			}
			// write the rights
			if (feed.getRights() != null) {
				writeRights(writer, feed.getRights());
			}
			// write the extensions
			if (feed.getExtensions() != null) {
				writeExtensions(writer, feed.getExtensions());
			}
			// write the entries
			if (feed.getEntries() != null) {
				writeEntries(writer, feed.getEntries());
			}

			writer.writeEndElement();
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	private void writeAttributes(XMLStreamWriter writer,
			List<Attribute> attributes) throws AtomSpecException {
		if (attributes != null) {
			for (Attribute attr : attributes) {
				try {
					writer.writeAttribute(attr.getName(), attr.getValue());
				} catch (Exception e) {
					throw new AtomSpecException(e.getMessage());
				}
			}
		}
	}

	void writeSubtitle(XMLStreamWriter writer, Subtitle subtitle)
			throws AtomSpecException {
		try {
			writer.writeStartElement("subtitle");
			writeAtomTextConstruct(writer, subtitle.getAttributes(), subtitle
					.getDivWrapperStart(), subtitle.getDivWrapperEnd(),
					subtitle.getText());

			writer.writeEndElement();
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeGenerator(XMLStreamWriter writer, Generator generator)
			throws AtomSpecException {
		try {
			writer.writeStartElement("generator");
			// write the attributes.
			writeAttributes(writer, generator.getAttributes());
			writer.writeCharacters(generator.getText());
			writer.writeEndElement();
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeID(XMLStreamWriter writer, Id id) throws AtomSpecException {
		try {
			writer.writeStartElement("id");
			// write the attributes.
			writeAttributes(writer, id.getAttributes());
			writer.writeCharacters(id.getAtomUri());
			writer.writeEndElement();
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeUpdated(XMLStreamWriter writer, Updated updated)
			throws AtomSpecException {
		try {
			writer.writeStartElement("updated");
			// write the attributes.
			writeAttributes(writer, updated.getAttributes());
			writer.writeCharacters(updated.getText());
			writer.writeEndElement();
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeTitle(XMLStreamWriter writer, Title title)
			throws AtomSpecException {
		try {
			writer.writeStartElement("title");
			writeAtomTextConstruct(writer, title.getAttributes(), title
					.getDivWrapperStart(), title.getDivWrapperEnd(), title
					.getText());

			writer.writeEndElement();
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeXHTML(XMLStreamWriter writer, String text)
			throws AtomSpecException {
		try {

			if (text == null || text.length() == 0) {
				return;
			}

			if (text.indexOf('<') == -1) {

				writer.writeCharacters(text.replace("&lt;", "<").replace(
						"&gt;", ">").replaceAll("&amp;", "&"));

			} else {

				if (text.startsWith("</")) {
					// write the end element
					writer.writeEndElement();
					// search for the next element
					text = text.substring(text.indexOf('>') + 1);
					writeXHTML(writer, text);

				} else {

					// write up until the
					// opening of the next element
					// except if this is an end tag.
					String localWrite = null;
					if (!text.startsWith("/")) {
						localWrite = text.substring(0, text.indexOf('<'));
						writer.writeCharacters(localWrite.replace("&lt;", "<")
								.replace("&gt;", ">").replaceAll("&amp;", "&"));
						text = text.substring(text.indexOf('<') + 1);
					}

					// if we reach another < before > then this
					// is not a start element.
					if ((text.indexOf(">") > text.indexOf("<"))
							&& !text.startsWith("/")) {

						writer.writeCharacters(localWrite.replace("&lt;", "<")
								.replace("&gt;", ">").replaceAll("&amp;", "&"));
						text = text.substring(localWrite.length() - 1);

						writeXHTML(writer, text);

					} else {

						// get the start element
						String startElement = text.substring(0,
								text.indexOf('>')).trim();

						// check for end element.
						if (startElement.startsWith("/")) {
							// write the end element
							writer.writeEndElement();
							// search for the next element
							text = text.substring(text.indexOf('>') + 1);
							writeXHTML(writer, text);

						} else {

							// check for empty element
							if (startElement.indexOf('/') == startElement
									.length() - 1) {
								// check for attributes
								String[] attributes = startElement.split(" ");
								if (attributes.length > 1) {
									// if the name has a prefix, just
									// write it as part of the local name.
									writer.writeEmptyElement(attributes[0]);
									for (int i = 1; i < attributes.length; i++) {
										if (attributes[i].indexOf("=") != -1) {
											// we nee to put everything
											// to the right of the first '='
											// sign
											// in the value part because we
											// could
											// have
											// a query string with multiple '='
											// signs.
											String attrName = attributes[i]
													.substring(0, attributes[i]
															.indexOf('='));
											String attrValue = attributes[i]
													.substring(
															attributes[i]
																	.indexOf('=') + 2,
															attributes[i]
																	.lastIndexOf("\""));
											writer.writeAttribute(attrName,
													attrValue);
										}
									}
								} else {
									// if the name has a prefix, just
									// write it as part of the local name.
									writer.writeEmptyElement(startElement);
								}

							} else {// this is regular start element

								// check for attributes
								String[] attributes = startElement.split(" ");

								if (attributes.length > 1) {
									// if the name has a prefix,
									// just write it as part of the local name.

									writer.writeStartElement(attributes[0]);
									for (int i = 1; i < attributes.length; i++) {
										if (attributes[i].indexOf("=") != -1) {
											String attrName = attributes[i]
													.substring(0, attributes[i]
															.indexOf('='));
											String attrValue = attributes[i]
													.substring(
															attributes[i]
																	.indexOf('=') + 2,
															attributes[i]
																	.lastIndexOf("\""));
											writer.writeAttribute(attrName,
													attrValue);
										}
									}
									startElement = attributes[0];
								} else {
									// if the name has a prefix,
									// just write it as part of the local name.
									writer.writeStartElement(attributes[0]);
								}
							}
							text = text.substring(text.indexOf('>') + 1);

							// search for the next element
							writeXHTML(writer, text);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AtomSpecException("Content is not valid XHTML: "
					+ e.getLocalizedMessage());
		}
	}

	void writeAuthors(XMLStreamWriter writer, List<Author> authors)
			throws AtomSpecException {
		try {
			// loop through and print out each author.
			for (Author author : authors) {
				writer.writeStartElement("author");
				// write the attributes.
				writeAttributes(writer, author.getAttributes());
				writeName(writer, author.getName());
				if (author.getUri() != null) {
					writeUri(writer, author.getUri());
				}
				if (author.getEmail() != null) {
					writeEmail(writer, author.getEmail());
				}
				if (author.getExtensions() != null) {
					writeExtensions(writer, author.getExtensions());
				}
				writer.writeEndElement();
			}
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeName(XMLStreamWriter writer, Name name) throws AtomSpecException {
		try {
			writer.writeStartElement("name");
			writer.writeCharacters(name.getText());
			writer.writeEndElement();
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeUri(XMLStreamWriter writer, URI uri) throws AtomSpecException {
		try {
			writer.writeStartElement("uri");
			writer.writeCharacters(uri.getText());
			writer.writeEndElement();
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeEmail(XMLStreamWriter writer, Email email)
			throws AtomSpecException {
		try {
			writer.writeStartElement("email");
			writer.writeCharacters(email.getText());
			writer.writeEndElement();
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeContributors(XMLStreamWriter writer,
			List<Contributor> contributors) throws AtomSpecException {
		try {
			// loop through and print out each contributor.
			for (Contributor contributor : contributors) {
				writer.writeStartElement("contributor");
				// write the attributes.
				writeAttributes(writer, contributor.getAttributes());
				writeName(writer, contributor.getName());
				if (contributor.getUri() != null) {
					writeUri(writer, contributor.getUri());
				}
				if (contributor.getEmail() != null) {
					writeEmail(writer, contributor.getEmail());
				}
				if (contributor.getExtensions() != null) {
					writeExtensions(writer, contributor.getExtensions());
				}
				writer.writeEndElement();
			}
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeRights(XMLStreamWriter writer, Rights rights)
			throws AtomSpecException {
		try {
			writer.writeStartElement("rights");
			writeAtomTextConstruct(writer, rights.getAttributes(), rights
					.getDivWrapperStart(), rights.getDivWrapperEnd(), rights
					.getText());

			writer.writeEndElement();
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeLogo(XMLStreamWriter writer, Logo logo) throws AtomSpecException {
		try {
			writer.writeStartElement("logo");
			// write the attributes.
			writeAttributes(writer, logo.getAttributes());
			writer.writeCharacters(logo.getAtomUri());
			writer.writeEndElement();
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeIcon(XMLStreamWriter writer, Icon icon) throws AtomSpecException {
		try {
			writer.writeStartElement("icon");
			// write the attributes.
			writeAttributes(writer, icon.getAttributes());
			writer.writeCharacters(icon.getAtomUri());
			writer.writeEndElement();
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeCategories(XMLStreamWriter writer, List<Category> categories)
			throws AtomSpecException {
		try {
			for (Category category : categories) {
				writer.writeEmptyElement("category");
				// write the attributes.
				writeAttributes(writer, category.getAttributes());
			}
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeLinks(XMLStreamWriter writer, List<Link> links)
			throws AtomSpecException {
		try {
			for (Link link : links) {
				writer.writeEmptyElement("link");
				// write the attributes.
				writeAttributes(writer, link.getAttributes());
			}
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeExtensions(XMLStreamWriter writer, List<Extension> extensions)
			throws AtomSpecException {
		try {
			for (Extension extension : extensions) {

				// if there is no content, then
				// write an empty extension element.
				if (extension.getContent() == null
						|| extension.getContent().trim().equals("")) {
					String elementName = extension.getElementName();
					if (elementName.indexOf(":") == -1) {
						writer.writeEmptyElement(elementName);
					} else {
						String prefix = elementName.substring(0, elementName
								.indexOf(":"));
						String localName = elementName.substring(elementName
								.indexOf(":") + 1);
						writer.writeEmptyElement(prefix, localName, "");
					}
					// write the attributes.
					writeAttributes(writer, extension.getAttributes());
				} else {
					String elementName = extension.getElementName();
					if (elementName.indexOf(":") == -1) {
						writer.writeStartElement(elementName);
					} else {
						String prefix = elementName.substring(0, elementName
								.indexOf(":"));
						String localName = elementName.substring(elementName
								.indexOf(":") + 1);
						writer.writeStartElement(prefix, localName, "");
					}
					// write the attributes.
					writeAttributes(writer, extension.getAttributes());
					// add the content.
					writer.writeCharacters(extension.getContent());

					// close the element.
					writer.writeEndElement();
				}
			}
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeEntries(XMLStreamWriter writer, SortedMap<String, Entry> entries)
			throws AtomSpecException {
		try {
			// print out the entries.
			for (Entry entry : entries.values()) {
				writer.writeStartElement("entry");
				// write the attributes.
				writeAttributes(writer, entry.getAttributes());
				// write the id
				writeID(writer, entry.getId());
				// write the updated date
				writeUpdated(writer, entry.getUpdated());
				// write the title
				writeTitle(writer, entry.getTitle());
				// write the source
				if (entry.getSource() != null) {
					writeSource(writer, entry.getSource());
				}
				// write the author
				if (entry.getAuthors() != null) {
					writeAuthors(writer, entry.getAuthors());
				}
				// write the contributors
				if (entry.getContributors() != null) {
					writeContributors(writer, entry.getContributors());
				}
				// write the links
				if (entry.getLinks() != null) {
					writeLinks(writer, entry.getLinks());
				}
				// write the categories
				if (entry.getCategories() != null) {
					writeCategories(writer, entry.getCategories());
				}
				// write the published date
				if (entry.getPublished() != null) {
					writePublished(writer, entry.getPublished());
				}
				// write the rights
				if (entry.getRights() != null) {
					writeRights(writer, entry.getRights());
				}
				// write the extensions
				if (entry.getExtensions() != null) {
					writeExtensions(writer, entry.getExtensions());
				}
				// write the summary
				if (entry.getSummary() != null) {
					writeSummary(writer, entry.getSummary());
				}
				// write the content
				if (entry.getContent() != null) {
					writeContent(writer, entry.getContent());
				}

				writer.writeEndElement();
			}
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeSummary(XMLStreamWriter writer, Summary summary)
			throws AtomSpecException {
		try {
			writer.writeStartElement("summary");
			writeAtomTextConstruct(writer, summary.getAttributes(), summary
					.getDivWrapperStart(), summary.getDivWrapperEnd(), summary
					.getText());

			writer.writeEndElement();
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	private void writeAtomTextConstruct(XMLStreamWriter writer,
			List<Attribute> attributes, String startDivWrapper,
			String endDivWrapper, String text) throws AtomSpecException {
		try {
			// write the attributes if there are any
			writeAttributes(writer, attributes);
			// check to see if we need to
			// wrap the text in a an <xhtml:div> tag.
			if (feedDoc.getContentType(attributes) == FeedDoc.ContentType.XHTML) {

				writeXHTML(writer, startDivWrapper + text + endDivWrapper);

				// check to see if we need to escape the data
			} else if (feedDoc.getContentType(attributes) == FeedDoc.ContentType.HTML) {

				writer.writeCharacters(text.replaceAll("&", "&amp;").replace(
						"&lt;", "<").replace(">", "&gt;"));

				// just write the text.
			} else {
				writer.writeCharacters(text);
			}
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writePublished(XMLStreamWriter writer, Published published)
			throws AtomSpecException {
		try {
			writer.writeStartElement("published");
			// write the attributes.
			writeAttributes(writer, published.getAttributes());
			writer.writeCharacters(published.getText());
			writer.writeEndElement();
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeContent(XMLStreamWriter writer, Content content)
			throws AtomSpecException {
		try {
			// look for the src attribute to
			// see if we need to build an empty tag.
			if (feedDoc.getAttributeFromGroup(content.getAttributes(), "src") != null) {
				writer.writeEmptyElement("content");
				// write the attributes.
				writeAttributes(writer, content.getAttributes());
			} else {
				writer.writeStartElement("content");
				writeAtomTextConstruct(writer, content.getAttributes(), content
						.getDivWrapperStart(), content.getDivWrapperEnd(),
						content.getContent());

				writer.writeEndElement();
			}
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}

	void writeSource(XMLStreamWriter writer, Source source)
			throws AtomSpecException {
		try {
			// open the source element
			writer.writeStartElement("source");
			// write the attributes.
			writeAttributes(writer, source.getAttributes());
			// write the id
			if (source.getId() != null) {
				writeID(writer, source.getId());
			}
			// write the updated date
			if (source.getUpdated() != null) {
				writeUpdated(writer, source.getUpdated());
			}
			// write the generator
			if (source.getGenerator() != null) {
				writeGenerator(writer, source.getGenerator());
			}
			// write the title
			if (source.getTitle() != null) {
				writeTitle(writer, source.getTitle());
			}
			// write the author
			if (source.getAuthors() != null) {
				writeAuthors(writer, source.getAuthors());
			}
			// write the contributors
			if (source.getContributors() != null) {
				writeContributors(writer, source.getContributors());
			}
			// write the links
			if (source.getLinks() != null) {
				writeLinks(writer, source.getLinks());
			}
			// write the categories
			if (source.getCategories() != null) {
				writeCategories(writer, source.getCategories());
			}
			// write the icon
			if (source.getIcon() != null) {
				writeIcon(writer, source.getIcon());
			}
			// write the logo
			if (source.getLogo() != null) {
				writeLogo(writer, source.getLogo());
			}
			// write the rights
			if (source.getRights() != null) {
				writeRights(writer, source.getRights());
			}
			// write the extensions
			if (source.getExtensions() != null) {
				writeExtensions(writer, source.getExtensions());
			}

			writer.writeEndElement();
		} catch (Exception e) {
			throw new AtomSpecException(e.getMessage());
		}
	}
}