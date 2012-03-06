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
package org.geotools.index;

import java.io.IOException;

/**
 * DOCUMENT ME!
 * 
 * @author Tommaso Nolli
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/plugin/shapefile/src/main/java/org/geotools/index/TreeException.java $
 */
public class TreeException extends IOException {

    private static final long serialVersionUID = 1988241322009839486L;

    public TreeException() {
        super();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param message
     */
    public TreeException(String message) {
        super(message);
    }
}
