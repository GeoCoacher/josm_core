/*
 * The JTS Topology Suite is a collection of Java classes that
 * implement the fundamental operations required to validate a given
 * geo-spatial data set to a known topological specification.
 *
 * Copyright (C) 2001 Vivid Solutions
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * For more information, contact:
 *
 *     Vivid Solutions
 *     Suite #1A
 *     2328 Government Street
 *     Victoria BC  V8T 5G5
 *     Canada
 *
 *     (250)385-6040
 *     www.vividsolutions.com
 */
package com.vividsolutions.jts.geom.prep;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Computes the <tt>covers</tt> spatial relationship predicate
 * for a {@link PreparedPolygon} relative to all other {@link Geometry} classes.
 * Uses short-circuit tests and indexing to improve performance. 
 * <p>
 * It is not possible to short-circuit in all cases, in particular
 * in the case where the test geometry touches the polygon linework.
 * In this case full topology must be computed.
 * 
 * @author Martin Davis
 *
 */
class PreparedPolygonCovers 
	extends AbstractPreparedPolygonContains
{
	/**
	 * Computes the </tt>covers</tt> predicate between a {@link PreparedPolygon}
	 * and a {@link Geometry}.
	 * 
	 * @param prep the prepared polygon
	 * @param geom a test geometry
	 * @return true if the polygon covers the geometry
	 */
	public static boolean covers(PreparedPolygon prep, Geometry geom)
	{
    PreparedPolygonCovers polyInt = new PreparedPolygonCovers(prep);
    return polyInt.covers(geom);
	}

  /**
   * Creates an instance of this operation.
   * 
   * @param prepPoly the PreparedPolygon to evaluate
   */
	public PreparedPolygonCovers(PreparedPolygon prepPoly)
	{
		super(prepPoly);
		requireSomePointInInterior = false;
	}
		
	/**
	 * Tests whether this PreparedPolygon <tt>covers</tt> a given geometry.
	 * 
	 * @param geom the test geometry
	 * @return true if the test geometry is covered
	 */
	public boolean covers(Geometry geom)
	{
		return eval(geom);
	}
	
	/**
	 * Computes the full topological <tt>covers</tt> predicate.
	 * Used when short-circuit tests are not conclusive.
	 * 
	 * @param geom the test geometry
	 * @return true if this prepared polygon covers the test geometry
	 */
	protected boolean fullTopologicalPredicate(Geometry geom)
	{
		boolean result = prepPoly.getGeometry().covers(geom);
		return result;
	}
	
}
