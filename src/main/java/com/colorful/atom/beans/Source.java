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
 *  2008-03-17 wbrown - made class immutable.
 */
package com.colorful.atom.beans;

import java.util.List;

/**
 * This class represents an Atom 1.0 source element.
 * @see <a href="http://www.atomenabled.org/developers/syndication/atom-format-spec.php">Atom Syndication Format</a>
 * @author Bill Brown
 *	<pre>
 *		atomSource =
 *          element atom:source {
 *          atomCommonAttributes,
 *          (atomAuthor*
 *          & atomCategory*
 *          & atomContributor*
 *          & atomGenerator?
 *          & atomIcon?
 *          & atomId?
 *          & atomLink*
 *          & atomLogo?
 *          & atomRights?
 *          & atomSubtitle?
 *          & atomTitle?
 *          & atomUpdated?
 *          & extensionElement*)
 *          }
 *	</pre>
 */
public class Source extends AtomEntrySourceAdaptor{

	private Generator generator = null;
    private Icon icon = null;    
    private Logo logo = null;    
    private Subtitle subtitle = null;

    public Source(Id id
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
    		,Logo logo) throws AtomSpecException {
    	super(id,title,updated,rights,authors,categories,contributors,links,attributes,extensions);
    	this.generator = (generator == null)?null: new Generator(generator.getUri(),generator.getVersion()
    			,generator.getAttributes(),generator.getText());
    	this.subtitle = (subtitle == null)?null: new Subtitle(subtitle.getText(),subtitle.getAttributes());
    	this.icon = (icon == null)?null: new Icon(icon.getAtomUri(),icon.getAttributes());
    	this.logo = (logo == null)?null: new Logo(logo.getAtomUri(),logo.getAttributes());
    }


    public Generator getGenerator() {
        return (generator == null)?null: new Generator(generator.getUri(),generator.getVersion()
    			,generator.getAttributes(),generator.getText());
    }

    public Icon getIcon() {
        return (icon == null)?null: new Icon(icon.getAtomUri(),icon.getAttributes());
    }

    public Logo getLogo() {
        return (logo == null)?null: new Logo(logo.getAtomUri(),logo.getAttributes());
    }

    public Subtitle getSubtitle() {
        return (subtitle == null)?null: new Subtitle(subtitle.getText(),subtitle.getAttributes());
    }
}
