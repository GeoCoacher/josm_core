package iodb;

import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import static org.openstreetmap.josm.tools.I18n.marktr;

/**
 * A plugin to request and store imagery offsets in the centralized database.
 * 
 * @author Zverik
 * @license WTFPL
 */
public class ImageryOffsetPlugin extends Plugin {
    private GetImageryOffsetAction getAction;
    private StoreImageryOffsetAction storeAction;
    
    /**
     * Add both actions to their own menu. This creates
     * "Offset" menu, because "Imagery" is constantly rebuilt,
     * losing all changes, and other menus are either too long already,
     * or completely unsuitable for imagery offset actions.
     */
    public ImageryOffsetPlugin( PluginInformation info ) {
        super(info);
        
        getAction = new GetImageryOffsetAction();
        storeAction = new StoreImageryOffsetAction();
        
        JMenu offsetMenu = Main.main.menu.addMenu(marktr("Offset"), KeyEvent.VK_O, 6, "help");
        offsetMenu.add(getAction);
        offsetMenu.add(storeAction);
    }
}
