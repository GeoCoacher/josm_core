/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.feature.type;

import java.util.List;

import org.geotools.resources.Classes;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.util.InternationalString;

/**
 * Base class for attribute types.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/main/src/main/java/org/geotools/feature/type/AttributeTypeImpl.java $
 */
public class AttributeTypeImpl extends PropertyTypeImpl implements AttributeType {
	
	final protected boolean identified;

	public AttributeTypeImpl(
		Name name, Class<?> binding, boolean identified, boolean isAbstract,
		List<Filter> restrictions, AttributeType superType, InternationalString description
	) {
		super(name, binding, isAbstract, restrictions, superType, description);
		
		this.identified = identified;
	}

	public boolean isIdentified() {
		return identified;
	}
	
	public AttributeType getSuper() {
	    return (AttributeType) super.getSuper();
	}
	
	/**
	 * Override of hashcode.
	 */
	public int hashCode() {
	    return super.hashCode() ^ Boolean.valueOf(identified).hashCode();
	   
	}
	
	/**
	 * Override of equals.
	 * 
	 * @param other
	 *            the object to be tested for equality.
	 * 
	 * @return whether other is equal to this attribute Type.
	 */
	public boolean equals(Object other) {
	    if(this == other)
            return true;
	    
		if (!(other instanceof AttributeType)) {
			return false;
		}

		if (!super.equals(other)) 
			return false;
		
		AttributeType att = (AttributeType) other;
		
		if (identified != att.isIdentified()) {
			return false;
		}
	
		return true;
	}
	
    public String toString() {
        StringBuffer sb = new StringBuffer(Classes.getShortClassName(this));
        sb.append(" ");
        sb.append( getName() );
        if( isAbstract() ){
            sb.append( " abstract" );           
        }
        if( isIdentified() ){
            sb.append( " identified" );
        }
        if( superType != null ){
            sb.append( " extends ");
            sb.append( superType.getName().getLocalPart() );
        }
        if( binding != null ){
            sb.append( "<" );
            sb.append( Classes.getShortName( binding ) );
            sb.append( ">" );
        }
        if( description != null ){
            sb.append("\n\tdescription=");
            sb.append( description );            
        }
        if( restrictions != null && !restrictions.isEmpty() ){
            sb.append("\nrestrictions=");
            boolean first = true;
            for( Filter filter : restrictions ){
                if( first ){
                    first = false;
                }
                else {
                    sb.append( " AND " );
                }
                sb.append( filter );
            }
        }
        return sb.toString();
    }

}
