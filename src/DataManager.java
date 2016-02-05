/*
 * @(#) DataManager.java        0.1
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Eduar C. Velilla
 * @version 0.1
 */
public class DataManager {

    /** */
    private static final DataManager defaultManager = new DataManager();

    /**
     * 
     */
    private DataManager() {
        // Vac'io..!!!
    }

    /**
     * 
     * @return
     */
    public DataManager getDefaultManager() {
        return defaultManager;
    }

    /**
     *
     * @param f
     * @return
     */
    public static Grid load(File f) {

        Grid grid = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(f));

            String dim = reader.readLine();
            int i = dim.indexOf(',');
            int w = Integer.parseInt(dim.substring(0, i));
            int h = Integer.parseInt(dim.substring(i + 1, dim.lastIndexOf(',')));

            if (w < 1 || h < 1) {
                return grid;
            }
            grid = new Grid(w, h);

            for (int y = 0; y < h; ++y) {
                String rowValues = reader.readLine();
                for (int x = 0; x < w; ++x) {
                    if (rowValues.charAt(x) == Grid.LIVE) {
                        grid.add(new Cell(x, y));
                    }
                }
            }
        } catch (IOException ex) {
            grid = null;
        } catch (NumberFormatException ex1) {
            grid = null;
        } catch (IndexOutOfBoundsException ex2) {
            grid = null;
        }finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
            }
        }
        return grid;
    }

    /**
     *
     * @param f
     * @param g
     * @return
     */
    public static boolean save(File f, Grid g) {

        boolean success = true;
        FileWriter writer = null;
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            writer = new FileWriter(f);
            writer.write(g.toString());
        } catch (IOException ex) {
            success = false;
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
            }
        }
        return success;
    }
}
