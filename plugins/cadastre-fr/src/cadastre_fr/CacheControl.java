package cadastre_fr;
/**
 * This class handles the WMS layer cache mechanism. The design is oriented for a good performance (no
 * wait status on GUI, fast saving even in big file). A separate thread is created for each WMS
 * layer to not suspend the GUI until disk I/O is terminated (a file for the cache can take
 * several MB's). If the cache file already exists, new images are just appended to the file 
 * (performance). Since we use the ObjectStream methods, it is required to modify the standard 
 * ObjectOutputStream in order to have objects appended readable (otherwise a stream header 
 * is inserted before each append and an exception is raised at objects read).
 */

import static org.openstreetmap.josm.tools.I18n.tr;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;
import org.openstreetmap.josm.Main;

public class CacheControl implements Runnable {

    public class ObjectOutputStreamAppend extends ObjectOutputStream {
        public ObjectOutputStreamAppend(OutputStream out) throws IOException {
            super(out);
        }
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }

    public static boolean cacheEnabled = true;
    
    public static int cacheSize = 500;
    
    
    public WMSLayer wmsLayer = null;
    
    private ArrayList<GeorefImage> imagesToSave = new ArrayList<GeorefImage>();
    private Lock imagesLock = new ReentrantLock();
      
    public CacheControl(WMSLayer wmsLayer) {
        cacheEnabled = Main.pref.getBoolean("cadastrewms.enableCaching", true);
        this.wmsLayer = wmsLayer;
        try {
            cacheSize = Integer.parseInt(Main.pref.get("cadastrewms.cacheSize", String.valueOf(CadastrePreferenceSetting.DEFAULT_CACHE_SIZE)));
        } catch (NumberFormatException e) {
            cacheSize = CadastrePreferenceSetting.DEFAULT_CACHE_SIZE;
        }
        File path = new File(CadastrePlugin.cacheDir);
        if (!path.exists())
            path.mkdirs();
        else // check directory capacity
            checkDirSize(path);
        new Thread(this).start();
    }
    
    private void checkDirSize(File path) {
        long size = 0;
        long oldestFileDate = Long.MAX_VALUE;
        int oldestFile = 0;
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            size += files[i].length();
            if (files[i].lastModified() <  oldestFileDate) {
                oldestFile = i;
                oldestFileDate = files[i].lastModified();
            }
        }
        if (size > cacheSize*1024*1024) {
            System.out.println("Delete oldest file  \""+ files[oldestFile].getName() 
                    + "\" in cache dir to stay under the limit of " + cacheSize + " MB.");
            files[oldestFile].delete();
            checkDirSize(path);
        }
    }
    
    public boolean loadCacheIfExist() {
        try {
            File file = new File(CadastrePlugin.cacheDir + wmsLayer.name + "." + String.valueOf(wmsLayer.lambertZone+1));
            if (file.exists()) {
                int reply = JOptionPane.showConfirmDialog(null, 
                        "Location \""+wmsLayer.name+"\" found in cache.\n"+
                        "Load cache first ?\n"+
                        "(No = new cache)",
                        "Location in cache",
                        JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.OK_OPTION) {
                    return loadCache(file, wmsLayer.lambertZone);
                } else
                    file.delete();
            }            
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return false;
    }
    
    public void deleteCacheFile() {
        try {
            File file = new File(CadastrePlugin.cacheDir + wmsLayer.name + "." + String.valueOf(wmsLayer.lambertZone+1));
            if (file.exists())
                file.delete();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
    
    public boolean loadCache(File file, int currentLambertZone) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            if (wmsLayer.read(ois, currentLambertZone) == false)
                return false;
            ois.close();
            fis.close();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            JOptionPane
                    .showMessageDialog(Main.parent, tr("Error loading file"), tr("Error"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }


    public boolean existCache() {
        try {
            /*
            File pathname = new File(CadastrePlugin.cacheDir);
            String[] fileNames = pathname.list();
            for (String fileName : fileNames) {
                System.out.println("check file:"+fileName);
                //WMSLayer cached = new WMSLayer(new File(cacheDir+fileName));
            }*/
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return false;
    }
    
    public synchronized void saveCache(GeorefImage image) {
        imagesLock.lock();
        this.imagesToSave.add(image);
        this.notify();
        imagesLock.unlock();
    }

    /**
     * Thread saving the grabbed images in background.
     */
    public synchronized void run() {
        for (;;) {
            imagesLock.lock();
            // copy locally the images to save for better performance
            ArrayList<GeorefImage> images = new ArrayList<GeorefImage>(imagesToSave);
            imagesToSave.clear();
            imagesLock.unlock();
            if (images != null && !images.isEmpty()) {
                File file = new File(CadastrePlugin.cacheDir + wmsLayer.name + "." + String.valueOf((wmsLayer.lambertZone + 1)));
                try {
                    if (file.exists()) {
                        ObjectOutputStreamAppend oos = new ObjectOutputStreamAppend(
                                new BufferedOutputStream(new FileOutputStream(file, true)));
                        for (GeorefImage img : images) {
                            oos.writeObject(img);
                        }
                        oos.close();
                    } else {
                        ObjectOutputStream oos = new ObjectOutputStream(
                                new BufferedOutputStream(new FileOutputStream(file)));
                        wmsLayer.write(oos, images);
                        oos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
            }
            try {wait();} catch (InterruptedException e) {
                e.printStackTrace(System.out);
            }
        }
    }

}
