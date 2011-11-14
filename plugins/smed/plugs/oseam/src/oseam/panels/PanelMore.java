package oseam.panels;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.*;

import oseam.Messages;
import oseam.dialogs.OSeaMAction;
import oseam.seamarks.SeaMark.*;

public class PanelMore extends JPanel {

	private OSeaMAction dlg;
	public JLabel infoLabel;
	public JTextField infoBox;
	private ActionListener alInfo = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			dlg.mark.setInfo(infoBox.getText());
		}
	};
	public JLabel sourceLabel;
	public JTextField sourceBox;
	private ActionListener alSource = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			dlg.mark.setSource(sourceBox.getText());
		}
	};
	public JLabel elevLabel;
	public JTextField elevBox;
	private ActionListener alElev = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			dlg.mark.setElevation(elevBox.getText());
		}
	};
	public JLabel heightLabel;
	public JTextField heightBox;
	private ActionListener alHeight = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			dlg.mark.setHeight(heightBox.getText());
		}
	};
	public JLabel statusLabel;
	public JComboBox statusBox;
	public EnumMap<Sts, Integer> statuses = new EnumMap<Sts, Integer>(Sts.class);
	private ActionListener alStatus = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			for (Sts sts : statuses.keySet()) {
				int idx = statuses.get(sts);
				if (dlg.mark != null && (idx == statusBox.getSelectedIndex()))
					dlg.mark.setStatus(sts);
			}
		}
	};
	public JLabel constrLabel;
	public JComboBox constrBox;
	public EnumMap<Cns, Integer> constructions = new EnumMap<Cns, Integer>(Cns.class);
	private ActionListener alConstr = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			for (Cns cns : constructions.keySet()) {
				int idx = constructions.get(cns);
				if (dlg.mark != null && (idx == constrBox.getSelectedIndex()))
					dlg.mark.setConstr(cns);
			}
		}
	};
	public JLabel conLabel;
	public JComboBox conBox;
	public EnumMap<Con, Integer> conspicuities = new EnumMap<Con, Integer>(Con.class);
	private ActionListener alCon = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			for (Con con : conspicuities.keySet()) {
				int idx = conspicuities.get(con);
				if (dlg.mark != null && (idx == conBox.getSelectedIndex()))
					dlg.mark.setConsp(con);
			}
		}
	};
	public JLabel reflLabel;
	public JComboBox reflBox;
	public EnumMap<Con, Integer> reflectivities = new EnumMap<Con, Integer>(Con.class);
	private ActionListener alRefl = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			for (Con con : reflectivities.keySet()) {
				int idx = reflectivities.get(con);
				if (dlg.mark != null && (idx == reflBox.getSelectedIndex()))
					dlg.mark.setRefl(con);
			}
		}
	};
	public PanelPat panelPat;
	private ButtonGroup regionButtons = new ButtonGroup();
	public JRadioButton regionAButton = new JRadioButton(new ImageIcon(getClass().getResource("/images/RegionAButton.png")));
	public JRadioButton regionBButton = new JRadioButton(new ImageIcon(getClass().getResource("/images/RegionBButton.png")));
	public JRadioButton regionCButton = new JRadioButton(new ImageIcon(getClass().getResource("/images/RegionCButton.png")));
	private ActionListener alRegion = new ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (regionAButton.isSelected()) {
				dlg.mark.setRegion(Reg.A);
				switch (dlg.mark.getCategory()) {
				case LAM_PORT:
					dlg.mark.setObjColour(Col.RED);
					dlg.mark.setObjPattern(Pat.NONE);
					break;
				case LAM_PPORT:
					dlg.mark.setObjColour(Col.RED);
					dlg.mark.addObjColour(Col.GREEN);
					dlg.mark.addObjColour(Col.RED);
					dlg.mark.setObjPattern(Pat.HORIZ);
					break;
				case LAM_STBD:
					dlg.mark.setObjColour(Col.GREEN);
					dlg.mark.setObjPattern(Pat.NONE);
					break;
				case LAM_PSTBD:
					dlg.mark.setObjColour(Col.GREEN);
					dlg.mark.addObjColour(Col.RED);
					dlg.mark.addObjColour(Col.GREEN);
					dlg.mark.setObjPattern(Pat.HORIZ);
					break;
				}
				regionAButton.setBorderPainted(true);
			} else {
				regionAButton.setBorderPainted(false);
			}
			if (regionBButton.isSelected()) {
				dlg.mark.setRegion(Reg.B);
				switch (dlg.mark.getCategory()) {
				case LAM_PORT:
					dlg.mark.setObjColour(Col.GREEN);
					dlg.mark.setObjPattern(Pat.NONE);
					break;
				case LAM_PPORT:
					dlg.mark.setObjColour(Col.GREEN);
					dlg.mark.addObjColour(Col.RED);
					dlg.mark.addObjColour(Col.GREEN);
					dlg.mark.setObjPattern(Pat.HORIZ);
					break;
				case LAM_STBD:
					dlg.mark.setObjColour(Col.RED);
					dlg.mark.setObjPattern(Pat.NONE);
					break;
				case LAM_PSTBD:
					dlg.mark.setObjColour(Col.RED);
					dlg.mark.addObjColour(Col.GREEN);
					dlg.mark.addObjColour(Col.RED);
					dlg.mark.setObjPattern(Pat.HORIZ);
					break;
				}
				regionBButton.setBorderPainted(true);
			} else {
				regionBButton.setBorderPainted(false);
			}
			if (regionCButton.isSelected()) {
				dlg.mark.setRegion(Reg.C);
				dlg.mark.setObjPattern(Pat.HORIZ);
				switch (dlg.mark.getCategory()) {
				case LAM_PORT:
					dlg.mark.setObjColour(Col.RED);
					dlg.mark.addObjColour(Col.WHITE);
					dlg.mark.addObjColour(Col.RED);
					dlg.mark.addObjColour(Col.WHITE);
					break;
				case LAM_PPORT:
				case LAM_PSTBD:
					dlg.mark.setObjColour(Col.RED);
					dlg.mark.addObjColour(Col.GREEN);
					dlg.mark.addObjColour(Col.RED);
					dlg.mark.addObjColour(Col.GREEN);
					break;
				case LAM_STBD:
					dlg.mark.setObjColour(Col.GREEN);
					dlg.mark.addObjColour(Col.WHITE);
					dlg.mark.addObjColour(Col.GREEN);
					dlg.mark.addObjColour(Col.WHITE);
					break;
				}
				regionCButton.setBorderPainted(true);
			} else {
				regionCButton.setBorderPainted(false);
			}
		}
	};

	public PanelMore(OSeaMAction dia) {
		dlg = dia;
		this.setLayout(null);
		panelPat = new PanelPat(dlg, Ent.BODY);
		panelPat.setBounds(new Rectangle(0, 0, 110, 160));
		this.add(panelPat, null);
		this.add(getRegionButton(regionAButton, 110, 0, 34, 30, "RegionA"), null);
		this.add(getRegionButton(regionBButton, 110, 32, 34, 30, "RegionB"), null);
		this.add(getRegionButton(regionCButton, 110, 64, 34, 30, "RegionC"), null);

		elevLabel = new JLabel(Messages.getString("Elevation"), SwingConstants.CENTER);
		elevLabel.setBounds(new Rectangle(140, 0, 90, 20));
		this.add(elevLabel, null);
		elevBox = new JTextField();
		elevBox.setBounds(new Rectangle(160, 20, 50, 20));
		elevBox.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(elevBox, null);
		elevBox.addActionListener(alElev);

		heightLabel = new JLabel(Messages.getString("Height"), SwingConstants.CENTER);
		heightLabel.setBounds(new Rectangle(140, 40, 90, 20));
		this.add(heightLabel, null);
		heightBox = new JTextField();
		heightBox.setBounds(new Rectangle(160, 60, 50, 20));
		heightBox.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(heightBox, null);
		heightBox.addActionListener(alHeight);

		sourceLabel = new JLabel(Messages.getString("Source"), SwingConstants.CENTER);
		sourceLabel.setBounds(new Rectangle(110, 80, 130, 20));
		this.add(sourceLabel, null);
		sourceBox = new JTextField();
		sourceBox.setBounds(new Rectangle(110, 100, 130, 20));
		sourceBox.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(sourceBox, null);
		sourceBox.addActionListener(alSource);

		infoLabel = new JLabel(Messages.getString("Information"), SwingConstants.CENTER);
		infoLabel.setBounds(new Rectangle(110, 120, 130, 20));
		this.add(infoLabel, null);
		infoBox = new JTextField();
		infoBox.setBounds(new Rectangle(110, 140, 130, 20));
		infoBox.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(infoBox, null);
		infoBox.addActionListener(alInfo);

		statusLabel = new JLabel(Messages.getString("Status"), SwingConstants.CENTER);
		statusLabel.setBounds(new Rectangle(250, 0, 100, 20));
		this.add(statusLabel, null);
		statusBox = new JComboBox();
		statusBox.setBounds(new Rectangle(250, 20, 100, 20));
		addStsItem("", Sts.UNKNOWN);
		addStsItem(Messages.getString("Permanent"), Sts.PERM);
		addStsItem(Messages.getString("Occasional"), Sts.OCC);
		addStsItem(Messages.getString("Recommended"), Sts.REC);
		addStsItem(Messages.getString("NotInUse"), Sts.NIU);
		addStsItem(Messages.getString("Intermittent"), Sts.INT);
		addStsItem(Messages.getString("Reserved"), Sts.RESV);
		addStsItem(Messages.getString("Temporary"), Sts.TEMP);
		addStsItem(Messages.getString("Private"), Sts.PRIV);
		addStsItem(Messages.getString("Mandatory"), Sts.MAND);
		addStsItem(Messages.getString("Destroyed"), Sts.DEST);
		addStsItem(Messages.getString("Extinguished"), Sts.EXT);
		addStsItem(Messages.getString("Illuminated"), Sts.ILLUM);
		addStsItem(Messages.getString("Historic"), Sts.HIST);
		addStsItem(Messages.getString("Public"), Sts.PUB);
		addStsItem(Messages.getString("Synchronized"), Sts.SYNC);
		addStsItem(Messages.getString("Watched"), Sts.WATCH);
		addStsItem(Messages.getString("UnWatched"), Sts.UNWAT);
		addStsItem(Messages.getString("Doubtful"), Sts.DOUBT);
		this.add(statusBox, null);
		statusBox.addActionListener(alStatus);

		constrLabel = new JLabel(Messages.getString("Construction"), SwingConstants.CENTER);
		constrLabel.setBounds(new Rectangle(250, 40, 100, 20));
		this.add(constrLabel, null);
		constrBox = new JComboBox();
		constrBox.setBounds(new Rectangle(250, 60, 100, 20));
		addCnsItem("", Cns.UNKNOWN);
		addCnsItem(Messages.getString("Masonry"), Cns.BRICK);
		addCnsItem(Messages.getString("Concreted"), Cns.CONC);
		addCnsItem(Messages.getString("Boulders"), Cns.BOULD);
		addCnsItem(Messages.getString("HardSurfaced"), Cns.HSURF);
		addCnsItem(Messages.getString("Unsurfaced"), Cns.USURF);
		addCnsItem(Messages.getString("Wooden"), Cns.WOOD);
		addCnsItem(Messages.getString("Metal"), Cns.METAL);
		addCnsItem(Messages.getString("GRP"), Cns.GRP);
		addCnsItem(Messages.getString("Painted"), Cns.PAINT);
		this.add(constrBox, null);
		constrBox.addActionListener(alConstr);

		conLabel = new JLabel(Messages.getString("Conspicuity"), SwingConstants.CENTER);
		conLabel.setBounds(new Rectangle(250, 80, 100, 20));
		this.add(conLabel, null);
		conBox = new JComboBox();
		conBox.setBounds(new Rectangle(250, 100, 100, 20));
		addConItem("", Con.UNKNOWN);
		addConItem(Messages.getString("Conspicuous"), Con.CONSP);
		addConItem(Messages.getString("NotConspicuous"), Con.NCONS);
		this.add(conBox, null);
		conBox.addActionListener(alCon);

		reflLabel = new JLabel(Messages.getString("Reflectivity"), SwingConstants.CENTER);
		reflLabel.setBounds(new Rectangle(250, 120, 100, 20));
		this.add(reflLabel, null);
		reflBox = new JComboBox();
		reflBox.setBounds(new Rectangle(250, 140, 100, 20));
		addReflItem("", Con.UNKNOWN);
		addReflItem(Messages.getString("Conspicuous"), Con.CONSP);
		addReflItem(Messages.getString("NotConspicuous"), Con.NCONS);
		addReflItem(Messages.getString("Reflector"), Con.REFL);
		this.add(reflBox, null);
		reflBox.addActionListener(alRefl);

	}

	public void syncPanel() {
		panelPat.syncPanel();
		regionAButton.setBorderPainted(dlg.mark.getRegion() == Reg.A);
		regionBButton.setBorderPainted(dlg.mark.getRegion() == Reg.B);
		regionCButton.setBorderPainted(dlg.mark.getRegion() == Reg.C);
		elevBox.setText(dlg.mark.getElevation());
		heightBox.setText(dlg.mark.getHeight());
		sourceBox.setText(dlg.mark.getSource());
		infoBox.setText(dlg.mark.getInfo());
		statusBox.setSelectedIndex(0);
		for (Sts sts : statuses.keySet()) {
			int item = statuses.get(sts);
			if (dlg.mark.getStatus() == sts)
				statusBox.setSelectedIndex(item);
		}
		constrBox.setSelectedIndex(0);
		for (Cns cns : constructions.keySet()) {
			int item = constructions.get(cns);
			if (dlg.mark.getConstr() == cns)
				constrBox.setSelectedIndex(item);
		}
		conBox.setSelectedIndex(0);
		for (Con con : conspicuities.keySet()) {
			int item = conspicuities.get(con);
			if (dlg.mark.getConsp() == con)
				conBox.setSelectedIndex(item);
		}
		reflBox.setSelectedIndex(0);
		for (Con con : reflectivities.keySet()) {
			int item = reflectivities.get(con);
			if (dlg.mark.getRefl() == con)
				reflBox.setSelectedIndex(item);
		}
	}

	private void addStsItem(String str, Sts sts) {
		statuses.put(sts, statusBox.getItemCount());
		statusBox.addItem(str);
	}

	private void addCnsItem(String str, Cns cns) {
		constructions.put(cns, constrBox.getItemCount());
		constrBox.addItem(str);
	}

	private void addConItem(String str, Con con) {
		conspicuities.put(con, conBox.getItemCount());
		conBox.addItem(str);
	}

	private void addReflItem(String str, Con con) {
		reflectivities.put(con, reflBox.getItemCount());
		reflBox.addItem(str);
	}

	private JRadioButton getRegionButton(JRadioButton button, int x, int y, int w, int h, String tip) {
		button.setBounds(new Rectangle(x, y, w, h));
		button.setBorder(BorderFactory.createLoweredBevelBorder());
		button.setToolTipText(Messages.getString(tip));
		button.addActionListener(alRegion);
		regionButtons.add(button);
		return button;
	}

}
