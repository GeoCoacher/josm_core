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
package org.geotools.factory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.geotools.data.FeatureLockFactory;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.feature.FeatureCollections;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.FunctionFactory;
import org.geotools.resources.LazySet;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;


/**
 * Defines static methods used to access the application's default implementation for some
 * common factories. Those "common" factories comprise the {@linkplain StyleFactory style}
 * and {@linkplain FilterFactory filter} factories. Note that some specialized factories
 * finder like {@linkplain org.geotools.referencing.ReferencingFactoryFinder referencing} and
 * {@linkplain org.geotools.coverage.GeometryFactoryFinder coverage} are defined in specialized
 * classes.
 * <p>
 * <b>Tip:</b> The {@link BasicFactories} classes provides an other way to access the various
 * factories from a central point.
 *
 * @since 2.4
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/api/src/main/java/org/geotools/factory/CommonFactoryFinder.java $
 * @version $Id: CommonFactoryFinder.java 37280 2011-05-24 07:53:02Z mbedward $
 * @author Martin Desruisseaux
 * @author Jody Garnett
 */
public final class CommonFactoryFinder extends FactoryFinder {
    /**
     * The service registry for this manager.
     * Will be initialized only when first needed.
     */
    private static FactoryRegistry registry;

    /**
     * Do not allows any instantiation of this class.
     */
    private CommonFactoryFinder() {
        // singleton
    }

    /**
     * Returns the service registry. The registry will be created the first
     * time this method is invoked.
     */
    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(CommonFactoryFinder.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] {
                    FilterFactory.class,
                    FeatureLockFactory.class,
                    FileDataStoreFactorySpi.class,
//                  FunctionImpl.class, // TODO: remove
//                  FunctionExpression.class,//TODO: remove
                    Function.class,
                    FunctionFactory.class,
                    FeatureFactory.class,
                    FeatureTypeFactory.class,
                    FeatureCollections.class}));
        }
        return registry;
    }

    /**
     * Returns a set of all available implementations for the {@link FunctionExpression} interface.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return Set of available style factory implementations.
     * @deprecated Use FunctionExpression is now @deprecated
     */
    public static synchronized Set getFunctionExpressions(Hints hints) {
        //hints = mergeSystemHints(hints);
        //return new LazySet(getServiceRegistry().getServiceProviders(
        //        FunctionExpression.class, null, hints));
        return Collections.EMPTY_SET;
    }

    /**
     * Returns a set of all available implementations for the {@link Function} interface.
     * 
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return Set of available function expression implementations.
     */
    public static synchronized Set getFunctions(Hints hints) {
        hints = mergeSystemHints(hints);
        return new LazySet(getServiceRegistry().getServiceProviders(
                Function.class, null, hints));
    }

    /**
     * Returns a set of all available implementations of {@link FunctionFactory}.
     * 
     * @param hints An optional map of hints, or {@code null} if none.
     * 
     * @return Set of available function factory implementations.
     */
    public static synchronized Set<FunctionFactory> getFunctionFactories(Hints hints) {
        hints = mergeSystemHints(hints);
        return new LazySet(getServiceRegistry().getServiceProviders(
                FunctionFactory.class, null, hints));
    }
    
    /**
     * Returns a set of all available implementations for the {@link FileDataStoreFactorySpi} interface.
     * 
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return Set of available file data store factory implementations.
     */
    public static synchronized Set getFileDataStoreFactories(Hints hints) {
        hints = mergeSystemHints(hints);
        return new LazySet(getServiceRegistry().getServiceProviders(
                FileDataStoreFactorySpi.class, null, hints));
    }
    
    /** Return the first implementation of {@link FeatureFactory} matching the specified hints.
     * <p>
     * If no implementation matches, a new one is created if possible or an exception is thrown.
     * 
     * @param hints An optional map of hints; or {@code null} if none
     * @return Instance of FeatureFactory matching the supplied hints
     * @throws FactoryRegistryException if no implementation could be provided
     * @see Hints#FEATURE_FACTORY
     */
    public static FeatureFactory getFeatureFactory(Hints hints) {
        hints = mergeSystemHints(hints);
        if(hints.get(Hints.FEATURE_FACTORY) == null){
            try {
                Class<?> lenient = Class.forName("org.geotools.feature.LenientFeatureFactoryImpl");
                hints.put(Hints.FEATURE_FACTORY, lenient );
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return (FeatureFactory) lookup(FeatureFactory.class, hints, Hints.FEATURE_FACTORY);
    }
    
    /**
     * Returns the first implementation of {@link FilterFactory} matching the specified hints.
     * If no implementation matches, a new one is created if possible or an exception is thrown
     * otherwise.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first filter factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link FilterFactory} interface.
     *
     * @see Hints#FILTER_FACTORY
     */
    public static FilterFactory getFilterFactory(Hints hints)
            throws FactoryRegistryException
    {
        hints = mergeSystemHints(hints);
        return (FilterFactory) lookup(FilterFactory.class, hints, Hints.FILTER_FACTORY);
    }
    
    /**
     * Looks up a certain factory using two methods:
     * <ul><li>First and un-synchronized lookup in the hints, should the user have provided the
     *         preferred factroy</li>
     * <li>A standard SPI registry scan, which has to be fully synchronized</li>
     * @param category
     * @param hints
     * @param key
     * @return
     */
    private static Object lookup(Class category, Hints hints, Hints.Key key) {
        // nulls?
        if(hints == null || key == null) {
            return null;
        }
        
        // see if the user expressed a preference in the hints
        final Object hint = hints.get(key);
        if (hint != null) {
            if (category.isInstance(hint)) {
                return hint;
            }
        } 

        // otherwise do the lousy slow system scan
        synchronized (CommonFactoryFinder.class) {
            return getServiceRegistry().getServiceProvider(category, null, hints, key);
        }
    }

    /**
     * Returns the first implementation of {@link FilterFactory2} matching the specified hints.
     * This is a convenience method invoking {@link #getFilterFactory} with a hint value set
     * for requerying a {@link FactoryFilter2} implementation.
     *
     * @param  hints An optional map of hints, or {@code null} if none.
     * @return The first filter factory that matches the supplied hints.
     * @throws FactoryRegistryException if no implementation was found or can be created for the
     *         {@link FilterFactory2} interface.
     *
     * @see Hints#FILTER_FACTORY
     */
    public static FilterFactory2 getFilterFactory2(Hints hints)
            throws FactoryRegistryException
    {
        hints = mergeSystemHints(hints);
        
        final Object h = hints.get(Hints.FILTER_FACTORY);
        if (!(h instanceof Class ? FilterFactory2.class.isAssignableFrom((Class) h)
                                 : h instanceof FilterFactory2))
        {
            /*
             * Add the hint value only if the user didn't provided a suitable hint.
             * In any case, do not change the user-supplied hints; clone them first.
             */
            hints = new Hints(hints);
            hints.put(Hints.FILTER_FACTORY, FilterFactory2.class);
        }
        return (FilterFactory2) getFilterFactory(hints);
    }
}
