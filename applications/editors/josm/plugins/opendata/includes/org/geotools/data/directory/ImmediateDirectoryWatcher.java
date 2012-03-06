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
package org.geotools.data.directory;

import java.io.File;

/**
 * Performs a last updated check each time isStale is called. Accurate, but will
 * incur in scalability issues under heavy multithreaded load on servers (file
 * access is typically expensive as it requires a switch to kernel space)
 * 
 * @author Andrea Aime
 * 
 */
class ImmediateDirectoryWatcher implements DirectoryWatcher {

    File directory;

    Long lastUpdated;

    public ImmediateDirectoryWatcher(File directory) {
        this.directory = directory;
    }

    public synchronized boolean isStale() {
        return lastUpdated == null || lastUpdated < directory.lastModified();
    }

    public synchronized void mark() {
        lastUpdated = directory.lastModified();
    }

}
