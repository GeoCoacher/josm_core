package org.openstreetmap.josm.plugins.czechaddress.gui.databaseeditors;

import javax.swing.DefaultComboBoxModel;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.ExtendedDialog;
import org.openstreetmap.josm.plugins.czechaddress.addressdatabase.AddressElement;
import org.openstreetmap.josm.plugins.czechaddress.addressdatabase.House;
import org.openstreetmap.josm.plugins.czechaddress.addressdatabase.Street;
import org.openstreetmap.josm.plugins.czechaddress.gui.utils.UniversalListRenderer;
import org.openstreetmap.josm.tools.ImageProvider;

/**
 * Dialog for editing a {@link Street}
 *
 * @author Radomír Černoch radomir.cernoch@gmail.com
 */
public class StreetEditor extends ExtendedDialog {

    Street street = null;
    AddressElement parent = null;

    public StreetEditor(Street street) {
        super(Main.parent, "Upravit ulici", new String[] {"OK", "Zrušit"}, true);
        initComponents();

        this.street = street;
        this.parent = street.getParent();
        if (parent != null)
            parentField.setText(parent.getName());
        else
            parentField.setEnabled(false);

        parentEditButton.setIcon(ImageProvider.get("actions", "edit.png"));
        parentEditButton.setText("");
        parentEditButton.setEnabled(EditorFactory.isEditable(parent));

        nameField.setText(street.getName());
        houseList.setModel(new DefaultComboBoxModel(street.getHouses().toArray()));
        houseList.setCellRenderer(new UniversalListRenderer());

        houseEditButton.setIcon(ImageProvider.get("actions", "edit.png"));
        houseEditButton.setText("");
        houseListChanged(null);

        // And finalize initializing the form.
        setContent(mainPanel);
        this.setButtonIcons(new String[] {"ok.png", "cancel.png"});
        setDefaultButton(1);
        setCancelButton(2);
        setupDialog();
    }

    public String getStreetName() {
        return nameField.getText();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        parentField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        houseList = new javax.swing.JList();
        parentEditButton = new javax.swing.JButton();
        houseEditButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jLabel1.setText("Rodič:");

        jTextField1.setText("jTextField1");

        parentField.setEditable(false);

        jLabel2.setText("Jméno:");

        jLabel3.setText("Domy:");

        houseList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                houseListChanged(evt);
            }
        });
        jScrollPane1.setViewportView(houseList);

        parentEditButton.setText("    ");
        parentEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parentEditButtonActionPerformed(evt);
            }
        });

        houseEditButton.setText("    ");
        houseEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                houseEditButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(nameField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                        .addComponent(parentField, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(parentEditButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(houseEditButton)
                        .addGap(2, 2, 2))))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(parentField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(parentEditButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(houseEditButton)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(mainPanel);
    }// </editor-fold>//GEN-END:initComponents

    private House selectedHouse = null;

    private void houseListChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_houseListChanged
        selectedHouse = (House) houseList.getSelectedValue();
        houseEditButton.setEnabled(EditorFactory.isEditable(selectedHouse));
    }//GEN-LAST:event_houseListChanged

    private void houseEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_houseEditButtonActionPerformed
        assert selectedHouse != null;
        if (EditorFactory.editHouse(selectedHouse))
            houseList.setModel(new DefaultComboBoxModel(street.getHouses().toArray()));
    }//GEN-LAST:event_houseEditButtonActionPerformed

    private void parentEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parentEditButtonActionPerformed
        assert parent != null;
        if (EditorFactory.edit(parent))
            parentField.setText(parent.getName());
    }//GEN-LAST:event_parentEditButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton houseEditButton;
    private javax.swing.JList houseList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField nameField;
    private javax.swing.JButton parentEditButton;
    private javax.swing.JTextField parentField;
    // End of variables declaration//GEN-END:variables

}
