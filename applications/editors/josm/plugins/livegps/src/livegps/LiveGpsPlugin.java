package livegps;

import static org.openstreetmap.josm.gui.help.HelpUtil.ht;
import static org.openstreetmap.josm.tools.I18n.marktr;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.gpx.GpxData;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.MapView.LayerChangeListener;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.openstreetmap.josm.tools.Shortcut;

public class LiveGpsPlugin extends Plugin implements LayerChangeListener {
    private boolean enabled = false;
    private LiveGpsAcquirer acquirer = null;
    private Thread acquirerThread = null;
    private JMenu lgpsmenu = null;
    private JCheckBoxMenuItem lgpscapture;
    private JCheckBoxMenuItem lgpsautocenter;
    private LiveGpsDialog lgpsdialog;

    private GpxData data = new GpxData();
    private LiveGpsLayer lgpslayer = null;

    /**
     * The LiveGpsSuppressor is queried, if an event shall be suppressed.
     */
    private LiveGpsSuppressor suppressor = null;

    /**
     * separate thread, where the LiveGpsSuppressor executes.
     */
    private Thread suppressorThread;

    public class CaptureAction extends JosmAction {
        public CaptureAction() {
            super(
                    tr("Capture GPS Track"),
                    "capturemenu",
                    tr("Connect to gpsd server and show current position in LiveGPS layer."),
                    Shortcut.registerShortcut("menu:livegps:capture", tr(
                            "Menu: {0}", tr("Capture GPS Track")),
                            KeyEvent.VK_R, Shortcut.GROUP_MENU), true);
        }

        public void actionPerformed(ActionEvent e) {
            enableTracking(lgpscapture.isSelected());
        }
    }

    public class CenterAction extends JosmAction {
        public CenterAction() {
            super(tr("Center Once"), "centermenu",
                    tr("Center the LiveGPS layer to current position."),
                    Shortcut.registerShortcut("edit:centergps", tr("Edit: {0}",
                            tr("Center Once")), KeyEvent.VK_HOME,
                            Shortcut.GROUP_EDIT), true);
        }

        public void actionPerformed(ActionEvent e) {
            if (lgpslayer != null) {
                lgpslayer.center();
            }
        }
    }

    public class AutoCenterAction extends JosmAction {
        public AutoCenterAction() {
            super(
                    tr("Auto-Center"),
                    "autocentermenu",
                    tr("Continuously center the LiveGPS layer to current position."),
                    Shortcut.registerShortcut("menu:livegps:autocenter", tr(
                            "Menu: {0}", tr("Capture GPS Track")),
                            KeyEvent.VK_HOME, Shortcut.GROUP_MENU), true);
        }

        public void actionPerformed(ActionEvent e) {
            if (lgpslayer != null) {
                setAutoCenter(lgpsautocenter.isSelected());
            }
        }
    }

    public void activeLayerChange(Layer oldLayer, Layer newLayer) {
    }

    public void layerAdded(Layer newLayer) {
    }

    public void layerRemoved(Layer oldLayer) {
        assert (oldLayer == lgpslayer);

        enableTracking(false);
        lgpscapture.setSelected(false);
        MapView.removeLayerChangeListener(this);
        lgpslayer = null;
    }

    public LiveGpsPlugin(PluginInformation info) {
        super(info);
        MainMenu menu = Main.main.menu;
        lgpsmenu = menu.addMenu(marktr("LiveGPS"), KeyEvent.VK_G,
                menu.defaultMenuPos, ht("/Plugin/LiveGPS"));

        JosmAction captureAction = new CaptureAction();
        lgpscapture = new JCheckBoxMenuItem(captureAction);
        lgpsmenu.add(lgpscapture);
        lgpscapture.setAccelerator(captureAction.getShortcut().getKeyStroke());

        JosmAction centerAction = new CenterAction();
        JMenuItem centerMenu = new JMenuItem(centerAction);
        lgpsmenu.add(centerMenu);
        centerMenu.setAccelerator(centerAction.getShortcut().getKeyStroke());

        JosmAction autoCenterAction = new AutoCenterAction();
        lgpsautocenter = new JCheckBoxMenuItem(autoCenterAction);
        lgpsmenu.add(lgpsautocenter);
        lgpsautocenter.setAccelerator(autoCenterAction.getShortcut().getKeyStroke());
    }

    /**
     * Set to <code>true</code> if the current position should always be in the center of the map.
     * @param autoCenter if <code>true</code> the map is always centered.
     */
    public void setAutoCenter(boolean autoCenter) {
        lgpsautocenter.setSelected(autoCenter); // just in case this method was
        // not called from the menu
        if (lgpslayer != null) {
            lgpslayer.setAutoCenter(autoCenter);
            if (autoCenter)
                lgpslayer.center();
        }
    }

    /**
     * Returns <code>true</code> if autocenter is selected.
     * @return <code>true</code> if autocenter is selected.
     */
    public boolean isAutoCenter() {
        return lgpsautocenter.isSelected();
    }

    /**
     * Enable or disable gps tracking
     * @param enable if <code>true</code> tracking is started.
     */
    public void enableTracking(boolean enable) {
	
        if (enable && !enabled) {
            assert (suppressor == null);
            assert (suppressorThread == null);
            assert (acquirer == null);
            assert (acquirerThread == null);

            suppressor = new LiveGpsSuppressor();
            suppressorThread = new Thread(suppressor);

            acquirer = new LiveGpsAcquirer();
            acquirerThread = new Thread(acquirer);

	    if (lgpslayer == null) {
		lgpslayer = new LiveGpsLayer(data);
		Main.main.addLayer(lgpslayer);
		MapView.addLayerChangeListener(this);
		lgpslayer.setAutoCenter(isAutoCenter());
	    }

            lgpslayer.setSuppressor(suppressor);
            acquirer.addPropertyChangeListener(lgpslayer);
            acquirer.addPropertyChangeListener(lgpsdialog);

            suppressorThread.start();
            acquirerThread.start();

	    enabled = true;

        } else if (!enable && enabled) {
	    assert (lgpslayer != null);
            assert (suppressor != null);
            assert (suppressorThread != null);
            assert (acquirer != null);
            assert (acquirerThread != null);

	    acquirer.shutdown();
	    acquirer = null;
	    acquirerThread = null;

            suppressor.shutdown();
            suppressor = null;
            suppressorThread = null;

            lgpslayer.setSuppressor(null);

	    enabled = false;
	}
    }

    /* (non-Javadoc)
     * @see org.openstreetmap.josm.plugins.Plugin#mapFrameInitialized(org.openstreetmap.josm.gui.MapFrame, org.openstreetmap.josm.gui.MapFrame)
     */
    @Override
    public void mapFrameInitialized(MapFrame oldFrame, MapFrame newFrame) {
        if (newFrame != null)
            newFrame.addToggleDialog(lgpsdialog = new LiveGpsDialog(newFrame));
    }

    /**
     * @return the lgpsmenu
     */
    public JMenu getLgpsMenu() {
        return this.lgpsmenu;
    }
}
