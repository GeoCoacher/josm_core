package org.openstreetmap.josm.plugins.mapillary;

import static org.openstreetmap.josm.tools.I18n.tr;

import org.apache.commons.jcs.access.CacheAccess;
import org.openstreetmap.josm.plugins.mapillary.cache.MapillaryCache;
import org.openstreetmap.josm.plugins.mapillary.commands.CommandMoveImage;
import org.openstreetmap.josm.plugins.mapillary.commands.CommandTurnImage;
import org.openstreetmap.josm.plugins.mapillary.commands.MapillaryRecord;
import org.openstreetmap.josm.plugins.mapillary.downloads.MapillaryDownloader;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.MapView.EditLayerChangeListener;
import org.openstreetmap.josm.gui.layer.AbstractModifiableLayer;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.data.cache.BufferedImageCacheEntry;
import org.openstreetmap.josm.data.cache.JCSCacheManager;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.LayerListPopup;
import org.openstreetmap.josm.data.osm.event.PrimitivesAddedEvent;
import org.openstreetmap.josm.data.osm.event.PrimitivesRemovedEvent;
import org.openstreetmap.josm.data.osm.event.TagsChangedEvent;
import org.openstreetmap.josm.data.osm.event.NodeMovedEvent;
import org.openstreetmap.josm.data.osm.event.WayNodesChangedEvent;
import org.openstreetmap.josm.data.osm.event.RelationMembersChangedEvent;
import org.openstreetmap.josm.data.osm.event.AbstractDatasetChangedEvent;
import org.openstreetmap.josm.data.osm.event.DataChangedEvent;
import org.openstreetmap.josm.data.osm.event.DataSetListener;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.Action;
import javax.swing.Icon;

import java.util.List;
import java.util.ArrayList;

public class MapillaryLayer extends AbstractModifiableLayer implements
		DataSetListener, EditLayerChangeListener {

	public final static int SEQUENCE_MAX_JUMP_DISTANCE = 100;

	public static Boolean INSTANCED = false;
	public static MapillaryLayer INSTANCE;
	public static CacheAccess<String, BufferedImageCacheEntry> CACHE;
	public static MapillaryImage BLUE;
	public static MapillaryImage RED;

	private final MapillaryData mapillaryData = MapillaryData.getInstance();
	private final MapillaryRecord record = MapillaryRecord.getInstance();

	private List<Bounds> bounds;
	private MapillaryToggleDialog tgd;

	private MouseAdapter mouseAdapter;

	public MapillaryLayer() {
		super(tr("Mapillary Images"));
		bounds = new ArrayList<>();
		init();
	}

	/**
	 * Initializes the Layer.
	 */
	private void init() {
		INSTANCED = true;
		MapillaryLayer.INSTANCE = this;
		startMouseAdapter();
		try {
			CACHE = JCSCacheManager.getCache("Mapillary");
		} catch (IOException e) {
			Main.error(e);
		}
		if (Main.map != null && Main.map.mapView != null) {
			Main.map.mapView.addMouseListener(mouseAdapter);
			Main.map.mapView.addMouseMotionListener(mouseAdapter);
			Main.map.mapView.addLayer(this);
			MapView.addEditLayerChangeListener(this, false);
			Main.map.mapView.getEditLayer().data.addDataSetListener(this);
			if (tgd == null) {
				if (MapillaryToggleDialog.INSTANCE == null) {
					tgd = MapillaryToggleDialog.getInstance();
					Main.map.addToggleDialog(tgd, false);
				} else
					tgd = MapillaryToggleDialog.getInstance();
			}
		}
		MapillaryPlugin.setMenuEnabled(MapillaryPlugin.EXPORT_MENU, true);
		download();
		Main.map.mapView.setActiveLayer(this);
	}

	public void startMouseAdapter() {
		mouseAdapter = new MouseAdapter() {

			private Point start;
			private int lastButton;
			private MapillaryImage closest;

			@Override
			public void mousePressed(MouseEvent e) {
				lastButton = e.getButton();
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				if (Main.map.mapView.getActiveLayer() != MapillaryLayer
						.getInstance())
					return;
				MapillaryImage closest = getClosest(e.getPoint());
				if (e.getClickCount() == 2
						&& mapillaryData.getSelectedImage() != null
						&& closest != null) {
					for (MapillaryImage img : closest.getSequence().getImages()) {
						mapillaryData.addMultiSelectedImage(img);
					}
				}
				this.start = e.getPoint();
				this.closest = closest;
				if (mapillaryData.getMultiSelectedImages().contains(closest))
					return;
				if (e.getModifiers() == (MouseEvent.BUTTON1_MASK | MouseEvent.CTRL_MASK)
						&& closest != null)
					mapillaryData.addMultiSelectedImage(closest);
				else
					mapillaryData.setSelectedImage(closest);
			}

			private MapillaryImage getClosest(Point clickPoint) {
				double snapDistance = 10;
				double minDistance = Double.MAX_VALUE;
				MapillaryImage closest = null;
				for (MapillaryImage image : mapillaryData.getImages()) {
					Point imagePoint = Main.map.mapView.getPoint(image
							.getLatLon());
					imagePoint
							.setLocation(imagePoint.getX(), imagePoint.getY());
					double dist = clickPoint.distanceSq(imagePoint);
					if (minDistance > dist
							&& clickPoint.distance(imagePoint) < snapDistance) {
						minDistance = dist;
						closest = image;
					}
				}
				return closest;
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (Main.map.mapView.getActiveLayer() != MapillaryLayer
						.getInstance())
					return;
				if (MapillaryData.getInstance().getSelectedImage() != null) {
					if (lastButton == MouseEvent.BUTTON1 && !e.isShiftDown()) {
						LatLon to = Main.map.mapView.getLatLon(e.getX(),
								e.getY());
						LatLon from = Main.map.mapView.getLatLon(start.getX(),
								start.getY());
						for (MapillaryImage img : MapillaryData.getInstance()
								.getMultiSelectedImages()) {
							img.move(to.getX() - from.getX(),
									to.getY() - from.getY());
						}
						Main.map.repaint();
					} else if (lastButton == MouseEvent.BUTTON1
							&& e.isShiftDown()) {
						this.closest.turn(Math.toDegrees(Math.atan2(
								(e.getX() - start.x), -(e.getY() - start.y)))
								- closest.getTempCa());
						for (MapillaryImage img : MapillaryData.getInstance()
								.getMultiSelectedImages()) {
							img.turn(Math.toDegrees(Math.atan2(
									(e.getX() - start.x), -(e.getY() - start.y)))
									- closest.getTempCa());
						}
						Main.map.repaint();
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (mapillaryData.getSelectedImage() == null)
					return;
				if (mapillaryData.getSelectedImage().getTempCa() != mapillaryData
						.getSelectedImage().getCa()) {
					double from = mapillaryData.getSelectedImage().getTempCa();
					double to = mapillaryData.getSelectedImage().getCa();
					record.addCommand(new CommandTurnImage(mapillaryData
							.getMultiSelectedImages(), to - from));
				} else if (mapillaryData.getSelectedImage().getTempLatLon() != mapillaryData
						.getSelectedImage().getLatLon()) {
					LatLon from = mapillaryData.getSelectedImage()
							.getTempLatLon();
					LatLon to = mapillaryData.getSelectedImage().getLatLon();
					record.addCommand(new CommandMoveImage(mapillaryData
							.getMultiSelectedImages(), to.getX() - from.getX(),
							to.getY() - from.getY()));
				}
				for (MapillaryImage img : mapillaryData
						.getMultiSelectedImages()) {
					if (img != null)
						img.stopMoving();
				}
			}
		};
	}

	public synchronized static MapillaryLayer getInstance() {
		if (MapillaryLayer.INSTANCE == null)
			MapillaryLayer.INSTANCE = new MapillaryLayer();
		return MapillaryLayer.INSTANCE;
	}

	/**
	 * Downloads all images of the area covered by the OSM data.
	 */
	protected void download() {
		for (Bounds bounds : Main.map.mapView.getEditLayer().data
				.getDataSourceBounds()) {
			if (!this.bounds.contains(bounds)) {
				this.bounds.add(bounds);
				new MapillaryDownloader().getImages(bounds.getMin(),
						bounds.getMax());
			}
		}
	}

	/**
	 * Returns the MapillaryData object, which acts as the database of the
	 * Layer.
	 * 
	 * @return
	 */
	public MapillaryData getMapillaryData() {
		return mapillaryData;
	}

	/**
	 * Method invoked when the layer is destroyed.
	 */
	@Override
	public void destroy() {
		MapillaryToggleDialog.getInstance().mapillaryImageDisplay
				.setImage(null);
		INSTANCED = false;
		MapillaryLayer.INSTANCE = null;
		MapillaryPlugin.setMenuEnabled(MapillaryPlugin.EXPORT_MENU, false);
		MapillaryData.INSTANCE = null;
		Main.map.mapView.removeMouseListener(mouseAdapter);
		MapView.removeEditLayerChangeListener(this);
		if (Main.map.mapView.getEditLayer() != null)
			Main.map.mapView.getEditLayer().data.removeDataSetListener(this);
		super.destroy();
	}

	/**
	 * Returns true any of the images from the database has been modified.
	 */
	@Override
	public boolean isModified() {
		for (MapillaryImage image : mapillaryData.getImages())
			if (image.isModified())
				return true;
		return false;
	}

	/**
	 * Paints the database in the map.
	 */
	@Override
	public void paint(Graphics2D g, MapView mv, Bounds box) {
		synchronized (this) {
			// Draw colored lines
			MapillaryLayer.BLUE = null;
			MapillaryLayer.RED = null;
			MapillaryToggleDialog.getInstance().blueButton.setEnabled(false);
			MapillaryToggleDialog.getInstance().redButton.setEnabled(false);
			if (mapillaryData.getSelectedImage() != null) {
				MapillaryImage[] closestImages = getClosestImagesFromDifferentSequences();
				Point selected = mv.getPoint(mapillaryData.getSelectedImage()
						.getLatLon());
				if (closestImages[0] != null) {
					MapillaryLayer.BLUE = closestImages[0];
					g.setColor(Color.BLUE);
					g.drawLine(mv.getPoint(closestImages[0].getLatLon()).x,
							mv.getPoint(closestImages[0].getLatLon()).y,
							selected.x, selected.y);
					MapillaryToggleDialog.getInstance().blueButton
							.setEnabled(true);
				}
				if (closestImages[1] != null) {
					MapillaryLayer.RED = closestImages[1];
					g.setColor(Color.RED);
					g.drawLine(mv.getPoint(closestImages[1].getLatLon()).x,
							mv.getPoint(closestImages[1].getLatLon()).y,
							selected.x, selected.y);
					MapillaryToggleDialog.getInstance().redButton
							.setEnabled(true);
				}
			}
			g.setColor(Color.WHITE);
			for (MapillaryImage image : mapillaryData.getImages()) {
				Point p = mv.getPoint(image.getLatLon());
				Point nextp;
				if (image.getSequence() != null
						&& image.getSequence().next(image) != null) {
					nextp = mv.getPoint(image.getSequence().next(image)
							.getLatLon());
					g.drawLine(p.x, p.y, nextp.x, nextp.y);
				}
				ImageIcon icon;
				if (!mapillaryData.getMultiSelectedImages().contains(image))
					icon = MapillaryPlugin.MAP_ICON;
				else
					icon = MapillaryPlugin.MAP_ICON_SELECTED;
				Image imagetemp = icon.getImage();
				BufferedImage bi = (BufferedImage) imagetemp;
				int width = icon.getIconWidth();
				int height = icon.getIconHeight();

				// Rotate the image
				double rotationRequired = Math.toRadians(image.getCa());
				double locationX = width / 2;
				double locationY = height / 2;
				AffineTransform tx = AffineTransform.getRotateInstance(
						rotationRequired, locationX, locationY);
				AffineTransformOp op = new AffineTransformOp(tx,
						AffineTransformOp.TYPE_BILINEAR);

				g.drawImage(op.filter(bi, null), p.x - (width / 2), p.y
						- (height / 2), Main.map.mapView);
			}
		}
	}

	@Override
	public Icon getIcon() {
		return MapillaryPlugin.ICON16;
	}

	@Override
	public boolean isMergable(Layer other) {
		return false;
	}

	@Override
	public void mergeFrom(Layer from) {
		throw new UnsupportedOperationException(
				"Notes layer does not support merging yet");
	}

	@Override
	public Action[] getMenuEntries() {
		List<Action> actions = new ArrayList<>();
		actions.add(LayerListDialog.getInstance().createShowHideLayerAction());
		actions.add(LayerListDialog.getInstance().createDeleteLayerAction());
		actions.add(new LayerListPopup.InfoAction(this));
		return actions.toArray(new Action[actions.size()]);
	}

	private MapillaryImage[] getClosestImagesFromDifferentSequences() {
		MapillaryImage[] ret = new MapillaryImage[2];
		double[] distances = { SEQUENCE_MAX_JUMP_DISTANCE,
				SEQUENCE_MAX_JUMP_DISTANCE };
		LatLon selectedCoords = mapillaryData.getSelectedImage().getLatLon();
		for (MapillaryImage image : mapillaryData.getImages()) {
			if (image.getLatLon().greatCircleDistance(selectedCoords) < SEQUENCE_MAX_JUMP_DISTANCE
					&& mapillaryData.getSelectedImage().getSequence() != image
							.getSequence()) {
				if ((ret[0] == null && ret[1] == null)
						|| (image.getLatLon().greatCircleDistance(
								selectedCoords) < distances[0] && (ret[1] == null || image
								.getSequence() != ret[1].getSequence()))) {
					ret[0] = image;
					distances[0] = image.getLatLon().greatCircleDistance(
							selectedCoords);
				} else if ((ret[1] == null || image.getLatLon()
						.greatCircleDistance(selectedCoords) < distances[1])
						&& image.getSequence() != ret[0].getSequence()) {
					ret[1] = image;
					distances[1] = image.getLatLon().greatCircleDistance(
							selectedCoords);
				}
			}
		}
		// Predownloads the thumbnails
		if (ret[0] != null)
			new MapillaryCache(ret[0].getKey(), MapillaryCache.Type.THUMBNAIL)
					.submit(MapillaryData.getInstance(), false);
		if (ret[1] != null)
			new MapillaryCache(ret[1].getKey(), MapillaryCache.Type.THUMBNAIL)
					.submit(MapillaryData.getInstance(), false);
		return ret;
	}

	@Override
	public Object getInfoComponent() {
		StringBuilder sb = new StringBuilder();
		sb.append(tr("Mapillary layer"));
		sb.append("\n");
		sb.append(tr("Total images:"));
		sb.append(" ");
		sb.append(this.size());
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public String getToolTipText() {
		return this.size() + " " + tr("images");
	}

	private int size() {
		return mapillaryData.getImages().size();
	}

	// EditDataLayerChanged
	@Override
	public void editLayerChanged(OsmDataLayer oldLayer, OsmDataLayer newLayer) {
	}

	/**
	 * When more data is downloaded, a delayed update is thrown, in order to
	 * wait for the data bounds to be set.
	 * 
	 * @param event
	 */
	@Override
	public void dataChanged(DataChangedEvent event) {
		Main.worker.submit(new delayedDownload());
	}

	private class delayedDownload extends Thread {

		@Override
		public void run() {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				Main.error(e);
			}
			MapillaryLayer.getInstance().download();
		}

	}

	@Override
	public void primitivesAdded(PrimitivesAddedEvent event) {
	}

	@Override
	public void primitivesRemoved(PrimitivesRemovedEvent event) {
	}

	@Override
	public void tagsChanged(TagsChangedEvent event) {
	}

	@Override
	public void nodeMoved(NodeMovedEvent event) {
	}

	@Override
	public void wayNodesChanged(WayNodesChangedEvent event) {
	}

	@Override
	public void relationMembersChanged(RelationMembersChangedEvent event) {
	}

	@Override
	public void otherDatasetChange(AbstractDatasetChangedEvent event) {
	}

	@Override
	public void visitBoundingBox(BoundingXYVisitor v) {
	}
}
