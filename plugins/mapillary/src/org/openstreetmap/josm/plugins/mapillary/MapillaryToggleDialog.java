package org.openstreetmap.josm.plugins.mapillary;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.cache.CacheEntry;
import org.openstreetmap.josm.data.cache.CacheEntryAttributes;
import org.openstreetmap.josm.data.cache.ICachedLoaderListener;
import org.openstreetmap.josm.data.cache.JCSCacheManager;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.plugins.mapillary.cache.MapillaryCache;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.AbstractAction;
import javax.swing.JPanel;

/**
 * Toggle dialog that shows an image and some buttons.
 * 
 * @author nokutu
 *
 */
public class MapillaryToggleDialog extends ToggleDialog implements
		ICachedLoaderListener {

	public static MapillaryToggleDialog INSTANCE;

	public volatile MapillaryImage image;

	final SideButton nextButton = new SideButton(new nextPictureAction());
	final SideButton previousButton = new SideButton(
			new previousPictureAction());
	final SideButton redButton = new SideButton(new redAction());
	final SideButton blueButton = new SideButton(new blueAction());

	private JPanel buttonsPanel;
	private JPanel top;

	public MapillaryImageDisplay mapillaryImageDisplay;

	private MapillaryCache imageCache;
	private MapillaryCache thumbnailCache;

	public MapillaryToggleDialog() {
		super(tr("Mapillary image"), "mapillary.png",
				tr("Open Mapillary window"), null, 200);
		mapillaryImageDisplay = new MapillaryImageDisplay();

		// this.add(mapillaryImageDisplay);
		blueButton.setForeground(Color.BLUE);
		redButton.setForeground(Color.RED);

		this.setLayout(new BorderLayout());
		top = new JPanel();
		top.setLayout(new BorderLayout());
		top.add(titleBar, BorderLayout.NORTH);

		createLayout(
				mapillaryImageDisplay,
				Arrays.asList(new SideButton[] { blueButton, previousButton,
						nextButton, redButton }),
				Main.pref.getBoolean("mapillary.reverse-buttons"));
	}

	public static MapillaryToggleDialog getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MapillaryToggleDialog();
		}
		return INSTANCE;
	}

	public static void destroyInstance() {
		INSTANCE = null;
	}

	/**
	 * Downloads the image of the selected MapillaryImage and sets in the
	 * MapillaryImageDisplay object.
	 */
	public synchronized void updateImage() {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					updateImage();
				}
			});
		} else {
			if (MapillaryLayer.INSTANCED == false) {
				return;
			}
			if (this.image == null)
				return;
			this.nextButton.setEnabled(true);
			this.previousButton.setEnabled(true);
			if (this.image.next() == null)
				this.nextButton.setEnabled(false);
			if (this.image.previous() == null)
				this.previousButton.setEnabled(false);

			try {
				this.mapillaryImageDisplay.setImage(null);
				MapillaryPlugin.CACHE = JCSCacheManager.getCache("mapillary");
				if (thumbnailCache != null)
					thumbnailCache.cancelOutstandingTasks();
				thumbnailCache = new MapillaryCache(image.getKey(),
						MapillaryCache.Type.THUMBNAIL);
				thumbnailCache.submit(this, false);

				if (imageCache != null)
					imageCache.cancelOutstandingTasks();
				imageCache = new MapillaryCache(image.getKey(),
						MapillaryCache.Type.FULL_IMAGE);
				imageCache.submit(this, false);
			} catch (IOException e) {
				Main.error(e);
			}
		}
	}

	/**
	 * Sets a new MapillaryImage to be shown.
	 * 
	 * @param image
	 */
	public synchronized void setImage(MapillaryImage image) {
		this.image = image;
	}

	/**
	 * Returns the MapillaryImage objects which is being shown.
	 * 
	 * @return
	 */
	public synchronized MapillaryImage getImage() {
		return this.image;
	}

	/**
	 * Action class form the next image button.
	 * 
	 * @author Jorge
	 *
	 */
	class nextPictureAction extends AbstractAction {
		public nextPictureAction() {
			putValue(NAME, tr("Next picture"));
			putValue(SHORT_DESCRIPTION,
					tr("Shows the next picture in the sequence"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (MapillaryToggleDialog.getInstance().getImage() != null) {
				MapillaryData.getInstance().selectNext();
				if (MapillaryData.getInstance().getSelectedImage() != null)
					Main.map.mapView.zoomTo(MapillaryData.getInstance()
							.getSelectedImage().getLatLon());
			}
		}
	}

	/**
	 * Action class for the previous image button.
	 * 
	 * @author Jorge
	 *
	 */
	class previousPictureAction extends AbstractAction {
		public previousPictureAction() {
			putValue(NAME, tr("Previous picture"));
			putValue(SHORT_DESCRIPTION,
					tr("Shows the previous picture in the sequence"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (MapillaryToggleDialog.getInstance().getImage() != null) {
				MapillaryData.getInstance().selectPrevious();
				if (MapillaryData.getInstance().getSelectedImage() != null)
					Main.map.mapView.zoomTo(MapillaryData.getInstance()
							.getSelectedImage().getLatLon());
			}
		}
	}

	class redAction extends AbstractAction {
		public redAction() {
			putValue(NAME, "Jump to red");
			putValue(SHORT_DESCRIPTION,
					tr("Shows the previous picture in the sequence"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (MapillaryToggleDialog.getInstance().getImage() != null) {
				MapillaryData.getInstance()
						.setSelectedImage(MapillaryLayer.RED);
				MapillaryToggleDialog.getInstance()
						.setImage(MapillaryLayer.RED);
				MapillaryToggleDialog.getInstance().updateImage();
				Main.map.mapView.zoomTo(MapillaryData.getInstance()
						.getSelectedImage().getLatLon());
			}
		}
	}

	class blueAction extends AbstractAction {
		public blueAction() {
			putValue(NAME, "Jump to blue");
			putValue(SHORT_DESCRIPTION,
					tr("Shows the previous picture in the sequence"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (MapillaryToggleDialog.getInstance().getImage() != null) {
				MapillaryData.getInstance().setSelectedImage(
						MapillaryLayer.BLUE);
				MapillaryToggleDialog.getInstance().setImage(
						MapillaryLayer.BLUE);
				MapillaryToggleDialog.getInstance().updateImage();
				Main.map.mapView.zoomTo(MapillaryData.getInstance()
						.getSelectedImage().getLatLon());
			}
		}
	}

	/**
	 * When the pictures are returned from the cache, they are set in the
	 * {@link MapillaryImageDisplay} object.
	 */
	@Override
	public void loadingFinished(CacheEntry data,
			CacheEntryAttributes attributes, LoadResult result) {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					updateImage();
				}
			});
		} else if (data != null) {
			try {
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(data
						.getContent()));
				if (this.mapillaryImageDisplay.getImage() == null)
					mapillaryImageDisplay.setImage(img);
				else if (img.getHeight() > this.mapillaryImageDisplay
						.getImage().getHeight()) {
					mapillaryImageDisplay.setImage(img);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates the layout of the dialog.
	 * 
	 * @param data The content of the dialog
	 * @param buttons The buttons where you can click
	 * @param reverse {@code true} if the buttons should go at the top; {@code false} otherwise.
	 */
	public void createLayout(Component data, List<SideButton> buttons,
			boolean reverse) {
		add(data, BorderLayout.CENTER);
		if (!buttons.isEmpty() && buttons.get(0) != null) {
			buttonsPanel = new JPanel(new GridLayout(1, 1));
			final JPanel buttonRowPanel = new JPanel(Main.pref.getBoolean(
					"dialog.align.left", false) ? new FlowLayout(
					FlowLayout.LEFT) : new GridLayout(1, buttons.size()));
			buttonsPanel.add(buttonRowPanel);
			for (SideButton button : buttons) {
				buttonRowPanel.add(button);
			}
			if (reverse)
				top.add(buttonsPanel, BorderLayout.SOUTH);
			else
				add(buttonsPanel, BorderLayout.SOUTH);
		}
		add(top, BorderLayout.NORTH);
	}
}
