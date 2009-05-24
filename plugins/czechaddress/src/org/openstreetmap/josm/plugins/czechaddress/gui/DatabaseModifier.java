/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DatabaseModifier.java
 *
 * Created on 24.5.2009, 18:03:35
 */

package org.openstreetmap.josm.plugins.czechaddress.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.ExtendedDialog;
import org.openstreetmap.josm.plugins.czechaddress.CzechAddressPlugin;
import org.openstreetmap.josm.plugins.czechaddress.addressdatabase.AddressElement;
import org.openstreetmap.josm.plugins.czechaddress.addressdatabase.Street;
import org.openstreetmap.josm.plugins.czechaddress.intelligence.Capitalizator;

/**
 *
 * @author radek
 */
public class DatabaseModifier extends ExtendedDialog {

    StreetModel<Street> streetModel = new StreetModel<Street>();

    /** Creates new form DatabaseModifier */
    public DatabaseModifier() {

        super(Main.parent, "Upravit databázi",
                                      new String[] { "OK", "Zrušit"}, true);
        initComponents();

        Capitalizator cap = new Capitalizator(
                                Main.ds.allPrimitives(),
                                CzechAddressPlugin.getLocation().getStreets());

        for (Street capStreet : cap.getCapitalised()) {
            assert cap.translate(capStreet).get("name") != null : capStreet;

            String elemName = capStreet.getName();
            String primName = cap.translate(capStreet).get("name");

            if (!elemName.equals(primName)) {
                streetModel.elems.add(capStreet);
                streetModel.names.add(primName);
            }
        }

        streetTable.setModel(streetModel);
        streetTable.setDefaultRenderer( Street.class,
                                        new StreetRenderer());

        // And finalize initializing the form.
        setupDialog(mainPanel, new String[] { "ok.png", "cancel.png" });
        setAlwaysOnTop(false);

        // TODO: Why does it always crash if the modality is set in constructor?
        setModal(false);
    }

    @Override
    protected void buttonAction(ActionEvent evt) {
        super.buttonAction(evt);
        setVisible(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        streetScrollPane = new javax.swing.JScrollPane();
        streetTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        streetTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Původní název", "Návrh z mapy"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        streetTable.setColumnSelectionAllowed(true);
        streetScrollPane.setViewportView(streetTable);
        streetTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        streetTable.getColumnModel().getColumn(1).setResizable(false);

        tabbedPane.addTab("Ulice", streetScrollPane);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        getContentPane().add(mainPanel);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane streetScrollPane;
    private javax.swing.JTable streetTable;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

    private class StreetRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component c =  super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            System.out.println("Tady som! " + value);

            if (value instanceof AddressElement)
                setText(((AddressElement) value).getName() );

            return c;
        }
    }

    private class StreetModel<Element> implements TableModel {

        List<Element> elems = new ArrayList<Element>();
        List<String>  names = new ArrayList<String>();

        public int getRowCount() {
            assert elems.size() == names.size();
            return elems.size();
        }

        public int getColumnCount() {
            return 2;
        }

        public String getColumnName(int columnIndex) {
            if (columnIndex == 0) return "Původní název";
            if (columnIndex == 1) return "Navržený název";
            assert false : columnIndex;
            return null;
        }

        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return AddressElement.class;
            if (columnIndex == 1) return String.class;
            assert false : columnIndex;
            return null;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) return elems.get(rowIndex);
            if (columnIndex == 1) return names.get(rowIndex);
            assert false : columnIndex;
            return null;
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            assert columnIndex == 1;
            names.set(rowIndex, (String) aValue);
        }

        public void addTableModelListener(TableModelListener l) {
            
        }

        public void removeTableModelListener(TableModelListener l) {
            
        }
    }
}
