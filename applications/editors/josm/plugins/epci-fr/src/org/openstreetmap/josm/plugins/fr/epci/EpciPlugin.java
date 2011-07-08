// License: GPL. v2 and later. Copyright 2011 by Don-vip
package org.openstreetmap.josm.plugins.fr.epci;

import org.openstreetmap.josm.gui.DefaultNameFormatter;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

/**
 * @author Don-vip
 * @version 0.1
 * History:
 * 0.1 08-Jul-2011 first prototype
 */
public class EpciPlugin extends Plugin {

	/**
	 * @param info
	 */
	public EpciPlugin(PluginInformation info) {
		super(info);
		DefaultNameFormatter.registerFormatHook(new EpciNameFormatter());
	}
}
