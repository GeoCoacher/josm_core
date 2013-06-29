// License: GPL. Copyright 2007 by Immanuel Scholz and others
package org.openstreetmap.josm.plugins.taggingpresettester;

import java.awt.Component;
import java.awt.Image;

import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;

import org.openstreetmap.josm.gui.tagging.TaggingPreset;
import org.openstreetmap.josm.tools.ImageProvider;

final public class TaggingCellRenderer extends DefaultListCellRenderer {
    @Override public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        TaggingPreset a = null;
        if (value instanceof TaggingPreset)
            a = (TaggingPreset)value;
        String name = a == null ? null : (String)a.getValue(Action.NAME);
        if (name == null)
            return super.getListCellRendererComponent(list, "", index, false, false);
        JComponent c = (JComponent)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        JLabel l = new JLabel(name);
        l.setForeground(c.getForeground());
        l.setBackground(c.getBackground());
        l.setFont(c.getFont());
        l.setBorder(c.getBorder());
        ImageIcon icon = (ImageIcon)a.getValue(Action.SMALL_ICON);
        if (icon != null)
            l.setIcon(new ImageIcon(icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        else {
            if (a.types == null)
                icon = ImageProvider.getIfAvailable("data", "empty");
            else if (a.types.size() != 1)
                icon = ImageProvider.getIfAvailable("data", "object");
            else
                icon = ImageProvider.getIfAvailable(a.types.iterator().next().getIconName());
            if (icon != null)
                l.setIcon(icon);
        }
        l.setOpaque(true);
        return l;
    }
}
