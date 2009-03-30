/*
 * Copyright (C) 2008 Innovant
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, please contact:
 *
 *  Innovant
 *   juangui@gmail.com
 *   vidalfree@gmail.com
 *
 *  http://public.grupoinnovant.com/blog
 *
 */
/*
 * Christof Dallermassl
 * christof@dallermassl.at
 */

package com.innovant.josm.plugin.routing;

import static org.openstreetmap.josm.tools.I18n.tr;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.IconToggleButton;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.gui.layer.Layer.LayerChangeListener;
import org.openstreetmap.josm.gui.preferences.PreferenceSetting;
import org.openstreetmap.josm.plugins.Plugin;

import com.innovant.josm.plugin.routing.actions.AddRouteNodeAction;
import com.innovant.josm.plugin.routing.actions.MoveRouteNodeAction;
import com.innovant.josm.plugin.routing.actions.RemoveRouteNodeAction;
import com.innovant.josm.plugin.routing.gui.RoutingDialog;
import com.innovant.josm.plugin.routing.gui.RoutingMenu;
import com.innovant.josm.plugin.routing.gui.RoutingPreferenceDialog;

/**
 * The main class of the routing plugin
 * @author juangui
 * @author Jose Vidal
 * @author cdaller
 *
 * @version 0.3
 */
public class RoutingPlugin extends Plugin implements LayerChangeListener {
	/**
	 * Logger
	 */
	static Logger logger = Logger.getLogger(RoutingPlugin.class);

	/**
	 * Displays the layer menu when right clicking on the layer name, and manages
	 * how to paint the layer.
	 */
    private RoutingLayer routingLayer;

    /**
     * The side dialog where nodes are listed
     */
    private RoutingDialog routingDialog;

    /**
     * Preferences Settings Dialog.
     */
    private PreferenceSetting preferenceSettings;

    /**
     * MapMode for adding route nodes.
     * We use this field to enable or disable the mode automatically.
     */
    private AddRouteNodeAction addRouteNodeAction;

    /**
     * MapMode for removing route nodes.
     * We use this field to enable or disable the mode automatically.
     */
    private RemoveRouteNodeAction removeRouteNodeAction;

    /**
     * MapMode for moving route nodes.
     * We use this field to enable or disable the mode automatically.
     */
    private MoveRouteNodeAction moveRouteNodeAction;

    /**
     * IconToggleButton for adding route nodes, we use this field to show or hide the button.
     */
    private IconToggleButton addRouteNodeButton;

    /**
     * IconToggleButton for removing route nodes, we use this field to show or hide the button.
     */
    private IconToggleButton removeRouteNodeButton;

    /**
     * IconToggleButton for moving route nodes, we use this field to show or hide the button.
     */
    private IconToggleButton moveRouteNodeButton;

    /**
     * Reference for the plugin class (as if it were a singleton)
     */
    private static RoutingPlugin plugin;

    /**
     * Default Constructor
     */
	public RoutingPlugin() {
		super();
		//FIXME: maybe check if plugin already exists
		plugin = this; // Assign reference to the plugin class
		DOMConfigurator.configure("log4j.xml");
		logger.debug("Loading routing plugin...");
		preferenceSettings=new RoutingPreferenceDialog();
		// Create side dialog
		routingDialog = new RoutingDialog();
        // Add routing layer
        routingLayer = new RoutingLayer(tr("Navigation"));
        Main.main.menu.add(new RoutingMenu(tr("Routing")));
        // Register this class as LayerChangeListener
        Layer.listeners.add(this);
        logger.debug("Finished loading plugin");
	}

	/**
	 * Provides static access to the plugin instance, to enable access to the plugin methods
	 * @return the instance of the plugin
	 */
	public static RoutingPlugin getInstance() {
		return plugin;
	}

	/**
	 * Get the routing layer
	 * @return The instance of the routing layer
	 */
	public RoutingLayer getRoutingLayer() {
		return routingLayer;
	}

	/**
	 * Get the routing side dialog
	 * @return The instance of the routing side dialog
	 */
	public RoutingDialog getRoutingDialog() {
		return routingDialog;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openstreetmap.josm.plugins.Plugin#mapFrameInitialized(org.openstreetmap.josm.gui.MapFrame, org.openstreetmap.josm.gui.MapFrame)
	 */
    @Override
    public void mapFrameInitialized(MapFrame oldFrame, MapFrame newFrame) {
        if(newFrame != null) {
        	// Create plugin map modes
        	addRouteNodeAction = new AddRouteNodeAction(newFrame);
        	removeRouteNodeAction = new RemoveRouteNodeAction(newFrame);
        	moveRouteNodeAction = new MoveRouteNodeAction(newFrame);
        	// Create plugin buttons and add them to the tool bar
        	addRouteNodeButton = new IconToggleButton(addRouteNodeAction);
        	removeRouteNodeButton = new IconToggleButton(removeRouteNodeAction);
        	moveRouteNodeButton = new IconToggleButton(moveRouteNodeAction);
            newFrame.addMapMode(addRouteNodeButton);
            newFrame.addMapMode(removeRouteNodeButton);
            newFrame.addMapMode(moveRouteNodeButton);
            newFrame.toolGroup.add(addRouteNodeButton);
            newFrame.toolGroup.add(removeRouteNodeButton);
            newFrame.toolGroup.add(moveRouteNodeButton);
            // Hide them by default
			addRouteNodeButton.setVisible(false);
			removeRouteNodeButton.setVisible(false);
			moveRouteNodeButton.setVisible(false);
            newFrame.addToggleDialog(routingDialog);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.openstreetmap.josm.gui.layer.Layer.LayerChangeListener#activeLayerChange(org.openstreetmap.josm.gui.layer.Layer, org.openstreetmap.josm.gui.layer.Layer)
     */
	public void activeLayerChange(Layer oldLayer, Layer newLayer) {}

	/*
	 * (non-Javadoc)
	 * @see org.openstreetmap.josm.gui.layer.Layer.LayerChangeListener#layerAdded(org.openstreetmap.josm.gui.layer.Layer)
	 */
	public void layerAdded(Layer newLayer) {
		// Add button(s) to the tool bar when the routing layer is added
		if (newLayer instanceof RoutingLayer) {
			newLayer.name=tr("Routing")+" ["+Main.map.mapView.getActiveLayer().name+"]";
			addRouteNodeButton.setVisible(true);
			removeRouteNodeButton.setVisible(true);
			moveRouteNodeButton.setVisible(true);
			logger.debug("Added routing layer.");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.openstreetmap.josm.gui.layer.Layer.LayerChangeListener#layerRemoved(org.openstreetmap.josm.gui.layer.Layer)
	 */
	public void layerRemoved(Layer oldLayer) {
		// Remove button(s) from the tool bar when the routing layer is removed
		if (oldLayer instanceof RoutingLayer) {
			addRouteNodeButton.setVisible(false);
			removeRouteNodeButton.setVisible(false);
			moveRouteNodeButton.setVisible(false);
    		logger.debug("Removed routing layer.");
		}
	}

	/* (non-Javadoc)
	 * @see org.openstreetmap.josm.plugins.Plugin#getPreferenceSetting()
	 */
	@Override
	public PreferenceSetting getPreferenceSetting() {
		return preferenceSettings;
	}
}
