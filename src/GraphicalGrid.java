/*
 * @(#) GraphicalGrid.java  0.1
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/**
 * Matriz grafica, permite interaccion con el mouse para agregar y eliminar
 * bacterias.
 *
 * @author Eduar C. Velilla
 */
public class GraphicalGrid extends JPanel implements ComponentListener, MouseListener, MouseMotionListener {

    /** Tamanio de una celda en pixeles */
    private int zoom = 1;
    /** Grid que se est'a visualizando */
    private Grid grid;
    /** */
    private Cell currentCell;
    /** */
    private Cell lastCell;

    /**
     *
     * @param initZoom
     * @param initGrid
     */
    public GraphicalGrid(int z, Grid g) {

		if(g == null) {
			throw new NullPointerException("Grid must be non-null");
		}
        addComponentListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        currentCell = new Cell(-1, 0);
        lastCell = new Cell(-1, 0);
        setGrid(g);
        setZoom(z);
    }

    /**
     *
     * @param z
     */
    public void setZoom(int z) {

        zoom = (z > 0) ? z : 1;
		resize();
		repaint();
    }

    /**
     *
     * @param g
     */
    public void setGrid(Grid g) {

        grid = g;
        resize();
        repaint();
    }

    /**
     *
     * @return
     */
    public int getZoom() {
        return zoom;
    }

    /**
     * 
     * @return
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     *
     */
    public void resize() {
        grid.resize(getWidth() / zoom, getHeight() / zoom);
    }

    /**
     *
     */
    private void getCurrentCell(int mX, int mY) {

		currentCell.x = -1;
        if (mX >= 0 && mX < grid.getWidth() * zoom
                && mY >= 0 && mY < grid.getHeight() * zoom) {
            currentCell.x = mX / zoom;
            currentCell.y = mY / zoom;
        }
    }

    /**
     * 
     * @param e
     */
    public void mouseClicked(MouseEvent e) {

        getCurrentCell(e.getX(), e.getY());

        if (currentCell.x != -1) {
            if (grid.contains(currentCell)) {
                grid.remove(currentCell);
            } else {
                grid.add(new Cell(currentCell));
            }
            repaint();
        }
    }

    /**
     *
     * @param e
     */
    public void mouseDragged(MouseEvent e) {

        getCurrentCell(e.getX(), e.getY());

        if (currentCell.x != -1) {
            if (!currentCell.equals(lastCell)) {
                if (!grid.contains(currentCell)) {
                    grid.add(new Cell(currentCell));
                    repaint();
                }
                lastCell.x = currentCell.x;
                lastCell.y = currentCell.y;
            }
        }
    }

    /* MOUSE. Sin Usar!!! */
    public void mouseMoved(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    /**
     * Actualiza el tamanio de la Matriz cuando es modificado en su tamanio
     *
     * @param e informacion relacionada al evento
     */
    public void componentResized(ComponentEvent e) {
        resize();
        repaint();
    }

    /* COMPONENT. Sin usar!!! */
    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    /**
     *
     * @return
     */
    public Dimension getPreferredSize() {
        return new Dimension(grid.getWidth() * zoom,
                grid.getHeight() * zoom);
    }

    /**
     * 
     * @param g
     */
    public synchronized void paint(Graphics g) {

        try {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.GREEN);
            
            for(Cell c : grid.getCells()) {
                g.fillRect(c.x * zoom,
                        c.y * zoom,
                        zoom - 1,
                        zoom - 1);
            }
        } catch (NullPointerException ex) {}
    }
}
