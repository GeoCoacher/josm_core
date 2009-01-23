package org.openstreetmap.josm.plugins.validator;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.GridBagLayout;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.preferences.PreferenceDialog;
import org.openstreetmap.josm.gui.preferences.PreferenceSetting;
import org.openstreetmap.josm.plugins.validator.util.Util;
import org.openstreetmap.josm.tools.GBC;

/**
 * Preference settings for the validator plugin
 * 
 * @author frsantos
 */
public class PreferenceEditor implements PreferenceSetting
{
    private OSMValidatorPlugin plugin;

    /** The preferences prefix */
    public static final String PREFIX = "validator";

    /** The preferences key for debug preferences */
    public static final String PREF_DEBUG = PREFIX + ".debug";

    /** The preferences key for debug preferences */
    public static final String PREF_LAYER = PREFIX + ".layer";

    /** The preferences key for enabled tests */
    public static final String PREF_TESTS = PREFIX + ".tests";

    /** The preferences key for enabled tests */
    public static final String PREF_USE_IGNORE = PREFIX + ".ignore";

    /** The preferences key for enabled tests before upload*/
    public static final String PREF_TESTS_BEFORE_UPLOAD = PREFIX + ".testsBeforeUpload";

    /**
     * The preferences key for enabling the permanent filtering
     * of the displayed errors in the tree regarding the current selection 
     */
    public static final String PREF_FILTER_BY_SELECTION = PREFIX + ".selectionFilter";

    private JCheckBox prefUseIgnore;
    private JCheckBox prefUseLayer;

    /** The list of all tests */
    private Collection<Test> allTests;

    public PreferenceEditor(OSMValidatorPlugin plugin) {
        this.plugin = plugin;
    }

    public void addGui(PreferenceDialog gui)
    {
        JPanel testPanel = new JPanel(new GridBagLayout());
        testPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        prefUseIgnore = new JCheckBox(tr("Use ignore list."), Main.pref.getBoolean(PREF_USE_IGNORE, true));
        prefUseIgnore.setToolTipText(tr("Use the ignore list to suppress warnings."));
        testPanel.add(prefUseIgnore, GBC.eol());

        prefUseLayer = new JCheckBox(tr("Use error layer."), Main.pref.getBoolean(PREF_LAYER, true));
        prefUseLayer.setToolTipText(tr("Use the error layer to display problematic elements."));
        testPanel.add(prefUseLayer, GBC.eol());

        GBC a = GBC.eol().insets(-5,0,0,0);
        a.anchor = GBC.EAST;
        testPanel.add( new JLabel(tr("On demand")), GBC.std() );
        testPanel.add( new JLabel(tr("On upload")), a );

        allTests = OSMValidatorPlugin.getTests();
        for(Test test: allTests)
        {
            test.addGui(testPanel);
        }

        JScrollPane testPane = new JScrollPane(testPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        testPane.setBorder(null);

        String description = tr("An OSM data validator that checks for common errors made by users and editor programs.");
        JPanel tab = gui.createPreferenceTab("validator", tr("Data validator"), description);
        tab.add(testPane, GBC.eol().fill(GBC.BOTH));
        tab.add(GBC.glue(0,10), a);
    }

    public boolean ok()
    {
        StringBuilder tests = new StringBuilder();
        StringBuilder testsBeforeUpload = new StringBuilder();
        Boolean res = false;

        for (Test test : allTests)
        {
            if(test.ok())
                res = false;
            String name = test.getClass().getSimpleName();
            tests.append( ',' ).append( name ).append( '=' ).append( test.enabled );
            testsBeforeUpload.append( ',' ).append( name ).append( '=' ).append( test.testBeforeUpload );
        }

        if (tests.length() > 0 ) tests = tests.deleteCharAt(0);
        if (testsBeforeUpload.length() > 0 ) testsBeforeUpload = testsBeforeUpload.deleteCharAt(0);

        plugin.initializeTests( allTests );

        Main.pref.put( PREF_TESTS, tests.toString());
        Main.pref.put( PREF_TESTS_BEFORE_UPLOAD, testsBeforeUpload.toString());
        Main.pref.put( PREF_USE_IGNORE, prefUseIgnore.isSelected());
        Main.pref.put( PREF_LAYER, prefUseLayer.isSelected());
        return false;
    }
}
