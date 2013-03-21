package iodb;

import java.awt.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.openstreetmap.josm.gui.layer.ImageryLayer;
import org.openstreetmap.josm.tools.ImageProvider;

/**
 * A button which shows offset information.
 * 
 * @author zverik
 */
public class OffsetDialogButton extends JButton {
    
    private ImageryOffsetBase offset;
    private double offsetLength;
    private double distance;
    private double direction;

    public OffsetDialogButton( ImageryOffsetBase offset ) {
        super();
        this.offset = offset;
//        setMinimumSize(new Dimension(500, 10));
//        setMaximumSize(new Dimension(500, 150));
        setRelevantText();
        setIcon(new OffsetIcon(offset));

        offsetLength = offset instanceof ImageryOffset ? getLengthAndDirection((ImageryOffset)offset)[0] : 0.0;
        // todo: layout, info, map distance and direction
        // http://stackoverflow.com/questions/1048224/get-height-of-multi-line-text-with-fixed-width-to-make-dialog-resize-properly
        // http://docs.oracle.com/javase/tutorial/uiswing/layout/box.html#size
        // http://stackoverflow.com/questions/8012646/setting-size-of-jbutton-inside-jpanel-with-boxlayout-doesnt-work-as-expected
        // http://blog.nobel-joergensen.com/2009/01/18/changing-preferred-size-of-a-html-jlabel/
        // http://thebadprogrammer.com/swing-layout-manager-sizing/
        // http://stackoverflow.com/questions/3692987/why-will-boxlayout-not-allow-me-to-change-the-width-of-a-jbutton-but-let-me-chan

        // http://stackoverflow.com/questions/2158/creating-a-custom-button-in-java
    }

    /**
     * Update text on the button. This method is to be deleted by release.
     */
    public void setRelevantText() {
        setText("<html><div style=\"width: 400px;\">"
                + ImageryOffsetTools.formatDistance(offset.getPosition().greatCircleDistance(ImageryOffsetTools.getMapCenter()))
                + ": " + offset.getDescription() + "</div></html>");
    }

    public ImageryOffsetBase getOffset() {
        return offset;
    }

/*    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width = 500;
        size.height = 70;
        return size;
    }*/

    /**
     * Update arrow for the offset location.
     */
    public void updateLocation() {
        // map was moved, update arrow.
        setRelevantText();
    }

    private double[] getLengthAndDirection( ImageryOffset offset ) {
        ImageryLayer layer = ImageryOffsetTools.getTopImageryLayer();
        double[] dxy = layer == null ? new double[] {0.0, 0.0} : new double[] {layer.getDx(), layer.getDy()};
        return ImageryOffsetTools.getLengthAndDirection((ImageryOffset)offset, dxy[0], dxy[1]);
    }

    class OffsetIcon implements Icon {
        private boolean isDeprecated;
        private boolean isCalibration;
        private double direction = -1.0;
        private double length;
        private ImageIcon background;

        public OffsetIcon( ImageryOffsetBase offset ) {
            isDeprecated = offset.isDeprecated();
            isCalibration = offset instanceof CalibrationObject;
            if( offset instanceof ImageryOffset ) {
                background = ImageProvider.get("offset");
                ImageryLayer layer = ImageryOffsetTools.getTopImageryLayer();
                double[] dxy = layer == null ? new double[] {0.0, 0.0} : new double[] { layer.getDx(), layer.getDy() };
                double[] ld = ImageryOffsetTools.getLengthAndDirection((ImageryOffset)offset, dxy[0], dxy[1]);
                length = ld[0];
                direction = ld[1];
            } else {
                background = ImageProvider.get("calibration");
            }
        }

        public void paintIcon( Component comp, Graphics g, int x, int y ) {
            background.paintIcon(comp, g, x, y);

            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if( direction >= 0 ) {
                g2.setColor(Color.black);
                Point c = new Point(x + getIconWidth() / 2, y + getIconHeight() / 2);
                if( length < 1e-2 ) {
                    // no offset
                    g2.fillOval(c.x - 3, c.y - 3, 7, 7);
                } else {
                    // draw an arrow
                    double arrowLength = length < 5 ? getIconWidth() / 2 - 1 : getIconWidth() - 4;
                    g2.setStroke(new BasicStroke(2));
                    int dx = (int)Math.round(Math.sin(direction) * arrowLength / 2);
                    int dy = (int)Math.round(Math.cos(direction) * arrowLength / 2);
                    g2.drawLine(c.x - dx, c.y - dy, c.x + dx, c.y + dy);
                    double wingLength = arrowLength / 3;
                    double d1 = direction - Math.PI / 6;
                    int dx1 = (int)Math.round(Math.sin(d1) * wingLength);
                    int dy1 = (int)Math.round(Math.cos(d1) * wingLength);
                    g2.drawLine(c.x + dx, c.y + dy, c.x + dx - dx1, c.y + dy - dy1);
                    double d2 = direction + Math.PI / 6;
                    int dx2 = (int)Math.round(Math.sin(d2) * wingLength);
                    int dy2 = (int)Math.round(Math.cos(d2) * wingLength);
                    g2.drawLine(c.x + dx, c.y + dy, c.x + dx - dx2, c.y + dy - dy2);
                }
            }
            if( isDeprecated ) {
                // big red X
                g2.setColor(Color.red);
                g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                g2.drawLine(x + 2, y + 2, x + getIconWidth() - 2, y + getIconHeight() - 2);
                g2.drawLine(x + 2, y + getIconHeight() - 2, x + getIconWidth() - 2, y + 2);
            }
        }

        public int getIconWidth() {
            return background.getIconWidth();
        }

        public int getIconHeight() {
            return background.getIconHeight();
        }
    }
}
