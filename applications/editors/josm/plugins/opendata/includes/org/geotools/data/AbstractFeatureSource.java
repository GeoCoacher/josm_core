/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;


/**
 * This is a starting point for providing your own SimpleFeatureSource implementation.
 *
 * <p>
 * Subclasses must implement:
 * </p>
 *
 * <ul>
 * <li>
 * getDataStore()
 * </li>
 * <li>
 * getSchema()
 * </li>
 * <li>
 * addFeatureListener()
 * </li>
 * <li>
 * removeFeatureListener()
 * </li>
 * </ul>
 *
 * <p>
 * You may find a SimpleFeatureSource implementations that is more specific to your needs - such as
 * JDBCFeatureSource.
 * </p>
 *
 * <p>
 * For an example of this class customized for use please see MemoryDataStore.
 * </p>
 *
 * @author Jody Garnett, Refractions Research Inc
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/main/src/main/java/org/geotools/data/AbstractFeatureSource.java $
 */
public abstract class AbstractFeatureSource implements SimpleFeatureSource {
    
    protected Set hints = Collections.EMPTY_SET;
    
    protected QueryCapabilities queryCapabilities = new QueryCapabilities();
    
    public AbstractFeatureSource() {
        // just to keep the default constructor around
    }
    
    /**
     * Overrides to explicitly type narrow the return type to {@link DataStore}
     */
    public abstract DataStore getDataStore();
    
    /**
     * Returns the same name than the feature type (ie,
     * {@code getSchema().getName()} to honor the simple feature land common
     * practice of calling the same both the Features produces and their types
     * 
     * @since 2.5
     * @see FeatureSource#getName()
     */
    public Name getName() {
        return getSchema().getName();
    }
    
    /**
     * This constructors allows to set the supported hints 
     * @param hints
     */
    public AbstractFeatureSource(Set hints) {
        this.hints = Collections.unmodifiableSet(new HashSet(hints));
    }
    
    public QueryCapabilities getQueryCapabilities(){
        return queryCapabilities;
    }
    
    /**
     * Retrieve the Transaction this SimpleFeatureSource is operating against.
     *
     * <p>
     * For a plain SimpleFeatureSource that cannot modify this will always be Transaction.AUTO_COMMIT.
     * </p>
     *
     * @return Transacstion SimpleFeatureSource is operating against
     */
    public Transaction getTransaction() {
        return Transaction.AUTO_COMMIT;
    }
    
    /**
     * Provides an interface to for the Results of a Query.
     *
     * <p>
     * Various queries can be made against the results, the most basic being to retrieve Features.
     * </p>
     *
     * @param query
     *
     *
     * @see org.geotools.data.FeatureSource#getFeatures(org.geotools.data.Query)
     */
    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
    	SimpleFeatureType schema = getSchema();        
        String typeName = schema.getTypeName();
        
        if( query.getTypeName() == null ){ // typeName unspecified we will "any" use a default
            DefaultQuery defaultQuery = new DefaultQuery(query);
            defaultQuery.setTypeName( typeName );
        }
        else if ( !typeName.equals( query.getTypeName() ) ){
            return new EmptyFeatureCollection( schema );
        }
        
        final QueryCapabilities queryCapabilities = getQueryCapabilities();
        if(!queryCapabilities.supportsSorting(query.getSortBy())){
            throw new DataSourceException("DataStore cannot provide the requested sort order");
        }
        
        SimpleFeatureCollection collection = new DefaultFeatureResults(this, query);
        if( collection.getSchema().getGeometryDescriptor() == null ){
            return collection; // no geometry no reprojection needed
        }
        return collection;
    }
    
    /**
     * Retrieve all Feature matching the Filter.
     *
     * @param filter Indicates features to retrieve
     *
     * @return FeatureResults indicating features matching filter
     *
     * @throws IOException If results could not be obtained
     */
    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        return getFeatures(new DefaultQuery(getSchema().getTypeName(), filter));
    }
    
    /**
     * Retrieve all Features.
     *
     * @return FeatureResults of all Features in FeatureSource
     *
     * @throws IOException If features could not be obtained
     */
    public SimpleFeatureCollection getFeatures() throws IOException {
        return getFeatures(Filter.INCLUDE);
    }
    
    /**
     * Retrieve Bounds of all Features.
     *
     * <p>
     * Currently returns null, consider getFeatures().getBounds() instead.
     * </p>
     *
     * <p>
     * Subclasses may override this method to perform the appropriate optimization for this result.
     * </p>
     *
     * @return null representing the lack of an optimization
     *
     * @throws IOException DOCUMENT ME!
     */
    public ReferencedEnvelope getBounds() throws IOException {
//        return getBounds(Query.ALL); // DZ should this not return just the bounds for this type?
        return getBounds(getSchema()==null?Query.ALL:new DefaultQuery(getSchema().getTypeName()));
    }
    
    /**
     * Retrieve Bounds of Query results.
     *
     * <p>
     * Currently returns null, consider getFeatures( query ).getBounds() instead.
     * </p>
     *
     * <p>
     * Subclasses may override this method to perform the appropriate optimization for this result.
     * </p>
     *
     * @param query Query we are requesting the bounds of
     *
     * @return null representing the lack of an optimization
     *
     * @throws IOException DOCUMENT ME!
     */
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        if (query.getFilter() == Filter.EXCLUDE) {
            return new ReferencedEnvelope(new Envelope(), getSchema().getCoordinateReferenceSystem());
        }
        
        DataStore dataStore = getDataStore();
        
        if ((dataStore == null) || !(dataStore instanceof AbstractDataStore)) {
            // too expensive
            return null;
        } else {
            // ask the abstract data store
            return ((AbstractDataStore) dataStore).getBounds( namedQuery( query ) );
        }
    }
    /**
     * Ensure query modified with typeName.
     * <p>
     * This method will make copy of the provided query, using
     * DefaultQuery, if query.getTypeName is not equal to
     * getSchema().getTypeName().
     * </p>
     * @param query Original query
     * @return Query with getTypeName() equal to getSchema().getTypeName()
     */
    protected Query namedQuery( Query query ){
        String typeName = getSchema().getTypeName();
        if( query.getTypeName() == null ||
                !query.getTypeName().equals( typeName )){
            
            return new DefaultQuery(
                    typeName,
                    query.getFilter(),
                    query.getMaxFeatures(),
                    query.getPropertyNames(),
                    query.getHandle()
                    );
        }
        return query;
    }
    
    /**
     * Retrieve total number of Query results.
     *
     * <p>
     * Currently returns -1, consider getFeatures( query ).getCount() instead.
     * </p>
     *
     * <p>
     * Subclasses may override this method to perform the appropriate optimization for this result.
     * </p>
     *
     * @param query Query we are requesting the count of
     *
     * @return -1 representing the lack of an optimization
     */
    public int getCount(Query query) throws IOException {
        if (query.getFilter() == Filter.EXCLUDE) {
            return 0;
        }
        
        DataStore dataStore = getDataStore();
        if ((dataStore == null) || !(dataStore instanceof AbstractDataStore)) {
            // too expensive
            return -1;
        } 
        // ask the abstract data store
        Transaction t = getTransaction();
        
        int nativeCount = ((AbstractDataStore) dataStore).getCount( namedQuery(query));
        if(nativeCount == -1)
        	return -1;
        
        //State state = t.getState(dataStore);
        int delta = 0;
        if(t != Transaction.AUTO_COMMIT) { 
        	if(t.getState(dataStore) == null)
        		return nativeCount;
        	
            if (!(t.getState(dataStore) instanceof TransactionStateDiff)) {
            	//we cannot proceed; abort!
            	return -1;
            }
        	Diff diff = ((AbstractDataStore)dataStore).state(t).diff(namedQuery(query).getTypeName());
        	synchronized (diff) {
        		Iterator it = diff.added.values().iterator();
        		while(it.hasNext()){
        			Object feature = it.next();
        			if( query.getFilter().evaluate(feature) )
        				delta++;
        		}
        		
        		it = diff.modified2.values().iterator();
        		while(it.hasNext()){
        			Object feature = it.next();
        			
        			if(feature == TransactionStateDiff.NULL && query.getFilter().evaluate(feature)) {
        				delta--;
        			}
        		}
        	}
        }
        
		return nativeCount + delta;
    }
    
    /**
     * By default, no Hints are supported
     */
    public Set getSupportedHints() {
        return hints;
    }
}
