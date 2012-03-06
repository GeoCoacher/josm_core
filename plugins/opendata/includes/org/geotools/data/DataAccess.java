/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;

import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * This is the top-level interface for access to {@code FeatureData}.
 *
 * <p>
 * <h2>Description</h2>
 * The DataAccess interface provides the following information about its contents:
 * <ul>
 * <li>{@link DataAccess.getInfo()} - information about the file or server itself
 * <li>{@link DataAccess.getNames()} - list of the available contents (each is an individual resource)
 * <li>{@link DataAccess.getSchema( Name )} - FeatureType describing the information available in the named resource
 * </ul>
 * <p>
 * <h2>Contents</h2>
 * You can access the contents of a service or file using getFeatureSource( Name ). Depending the
 * abilities of your implementation and your credentials you will have access to
 * <ul>
 * <li>{@link FeatureSource}: read-only api similar to the WFS getFeature operations. Please note the reutrned
 *    FeatureCollection may be *lazy*; for many implementations no actual access will occur until you
 *    use the FetaureCollection for the first time.
 * <li>{@link FeatureStore}: read/write api similar to the WFS Transaction operation. Batch changes such as 
 *    addFeatures, modifyFeatures and removeFeatures are supported.
 * <li>{@link FeatureLocking}: concurrency control; the Data Access API is thread safe; one consequence of this
 * is modifications being held up while other threads read the contents. You may wish to Lock a selection
 * of features for your exclusive use. Locks are timed; and will expire after the indicated period.
 * </ul>
 * <p>
 * Please note that all interaction occurs within the context of a Transaction, this
 * facility provides session management and is strongly advised. Please note that
 * your application is responsible for managing its own Transactions; as an example
 * they are often associated with a single Map in a desktop application; or a single
 * session in a J2EE web app.
 * <p>
 * The use of Transaction.AUTO_COMMIT is suitable for read-only access when you wish
 * to minimize the number of connections in use, when used for writing performance
 * will often be terrible.
 * 
 * <h2>Lifecycle</h2>
 * 
 * Normal use:
 * <ul>
 * <li>Connect using a DataAccessFactory.createDataStore using a set of connection parameters
 * <li>Application is responsible for holding a single instance to the service or file, DataAccess
 * implementations will hold onto database connections, internal caches and so on - and as such
 * should not be duplicated.
 * <li>DataAccess.dispose() is called when the application is shut down
 * </ul>
 * 
 * Creation:
 * <ul>
 * <li>Created using a DataAccessFactory.createNewDataStore using a set of creation parameters
 * <li>DataAccess.createSchema( T ) is called to set up the contents
 * <li>DataAccess.getFetaureSource( Name ) is called, and FeatureStore.addFeatures( collection ) used to populate the contents
 * <li>DataAccess.dispose() is called when the application is shut down
 * </ul>
 * <p>
 * Applications are responsible for holding a single instance to the service or file, The
 * DataAccess implementations will hold onto database connections, internal caches and so on - and as such
 * should not be duplicated.
 * 
 * @see DataStore Subclass restricted to working with simple content
 * @param <T> Type of Feature Content, may be SimpleFeatureType
 * @param <F> Feature Content, may be SimpleFetaure
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/api/src/main/java/org/geotools/data/DataAccess.java $
 */
public interface DataAccess<T extends FeatureType, F extends Feature> {

    /**
     * Creates storage for a new <code>featureType</code>.
     *
     * <p>
     * The provided <code>featureType</code> we be accessable by the typeName
     * provided by featureType.getTypeName().
     * </p>
     *
     * @param featureType FetureType to add to DataStore
     *
     * @throws IOException If featureType cannot be created
     */
    void createSchema(T featureType) throws IOException;

    /**
     * Disposes of this data store and releases any resource that it is using.
     * <p>
     * A <code>DataStore</code> cannot be used after <code>dispose</code> has
     * been called, neither can any data access object it helped create, such
     * as {@link FeatureReader}, {@link FeatureSource} or {@link FeatureCollection}.
     * <p>
     * This operation can be called more than once without side effects.
     * <p>
     * There is no thread safety assurance associated with this method. For example,
     * client code will have to make sure this method is not called while retrieving/saving data
     * from/to the storage, or be prepared for the consequences.
     */
    void dispose();

    //FeatureSource<T,F> getView(Query query) throws IOException, SchemaException;

    //FeatureReader<T,F> getFeatureReader(Query query, Transaction transaction)
    //    throws IOException;
    
    //FeatureWriter<T,F> getFeatureWriter(Name typeName, Filter filter, Transaction transaction)
    //    throws IOException;

    //FeatureWriter<T,F> getFeatureWriter(Name typeName, Transaction transaction)
    //    throws IOException;

    //FeatureWriter<T,F> getFeatureWriterAppend(Name typeName, Transaction transaction)
    //    throws IOException;
}
