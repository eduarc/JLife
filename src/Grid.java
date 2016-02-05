/*
 * @(#) Grid.java   0.1
 */

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Matriz en la cual viven las bacterias. Implementa el algoritmo de "Conway
 * Game of Life".
 *
 * Quitando los modificadores "synchronized" de los metodos de esta clase se
 * obtiene mejor rendimiento, pero se observan algunas desmejoras en el
 * dibujado.
 *
 * @author Eduar C. Velilla
 * @version 0.1
 */
public class Grid {

  public static final char EMPTY = '.';
  public static final char LIVE = '*';
  /**
   *    */
  private int width;
  /**
   *    */
  private int height;
  /**
   *    */
  private boolean grid[][];
  /**
   *    */
  private Hashtable<Cell, Cell> data;

  /**
   *
   */
  public Grid() {
    this(1, 1);
  }

  /**
   *
   * @param w
   * @param h
   */
  public Grid(int w, int h) {
    initDataMembers(w, h, 0);
  }

  /**
   *
   * @param g
   */
  public Grid(Grid g) {
    initDataMembers(g.width,
            g.height,
            g.data.size());
    Enumeration keys = g.data.elements();
    while (keys.hasMoreElements()) {
      add(new Cell((Cell) keys.nextElement()));
    }
  }

  /**
   *
   * @param w
   * @param h
   * @param popuSize
   */
  private void initDataMembers(int w, int h, int popuSize) {

    width = (w > 0) ? w : 1;
    height = (h > 0) ? h : 1;
    grid = new boolean[height][width];
    data = new Hashtable(popuSize);
  }

  /**
   *
   * @param c
   */
  public synchronized void add(Cell c) {

    grid[c.y][c.x] = true;
    data.put(c, c);
  }

  /**
   *
   * @param c
   */
  public synchronized void remove(Cell c) {

    if (data.remove(c) != null) {
      grid[c.y][c.x] = false;
    }
  }

  /**
   *
   */
  public synchronized void next() {

    int n;	// cuenta de vecinos
    Hashtable<Cell, Cell> changes = new Hashtable();
    /*
     * Calcular muertas y los potenciales nacimientos. Los potenciales
     * nacimientos se van calculando internamente a medida que se obtiene la
     * cuenta de vecinos de una bacteria viva en particular
     */
    for (Cell c : data.values()) {
      n = getNeighbors(c, changes); // obtener cuenta de vecinos
      if (n != 2 && n != 3) {
        changes.put(c, c);
      }
    }
    /*
     * Actualizar datos, agregar nacimientos y eliminar muertes
     */
    for (Cell c : changes.values()) {
      n = c.neighbors;
      if (n == 0) {
        remove(c);
      } else if (n == 3) {
        c.neighbors = 0;
        add(c);
      }
    }
  }

  /**
   *
   * @param c
   * @param noNeighbor
   * @return
   */
  private int getNeighbors(Cell c, Hashtable noNeighbor) {

    int count = 0;     // contador de vecinos
    int x, y;
    int dx[] = {-1, 1, 0, 0, -1, -1, 1, 1};
    int dy[] = {0, 0, -1, 1, -1, 1, -1, 1};

    for (int i = 0; i < 8; ++i) {
      x = c.x + dx[i];
      y = c.y + dy[i];
      if (x < 0 || x >= width
              || y < 0 || y >= height) {
        continue;
      }
      // Esta condicional resulta mejor que
      // if (data.containsKey(target))
      if (grid[y][x]) {
        ++count;
        continue;
      }
      Cell target = new Cell(x, y);
      Cell ne = (Cell) noNeighbor.get(target);
      if (ne != null) {
        ++ne.neighbors;
      } else {
        ne = new Cell(target);
        ne.neighbors = 1;
        noNeighbor.put(ne, ne);
      }
    }
    return count;
  }

  /**
   *
   */
  public synchronized void clear() {

    data.clear();
    for (int y = height - 1; y >= 0; --y) {
      for (int x = width - 1; x >= 0; --x) {
        grid[y][x] = false;
      }
    }
  }

  /**
   *
   * @param nw
   * @param nh
   */
  public synchronized void resize(int w, int h) {

    if (w == width && h == height) {
      return;
    }
    int readX = 0;
    int readY = 0;
    int centerX = 0;
    int centerY = 0;
    int lengthX = width;
    int lengthY = height;

    if (w < width) {
      readX = (width - w) / 2;
      lengthX = w;
    } else {
      centerX = (w - width) / 2;
    }

    if (h < height) {
      readY = (height - h) / 2;
      lengthY = h;
    } else {
      centerY = (h - height) / 2;
    }

    width = w;
    height = h;
    int limitY = readY + lengthY;
    int limitX = readX + lengthX;

    data.clear();
    System.gc(); // sugerir recoleccion antigua data

    boolean oldGrid[][] = grid;
    grid = new boolean[h][w];

    for (int ny = 0, y = readY; y < limitY; ++y, ++ny) {
      for (int nx = 0, x = readX; x < limitX; ++x, ++nx) {
        if (oldGrid[y][x]) {
          add(new Cell(nx + centerX, ny + centerY));
        }
      }
    }
    oldGrid = null;
    System.gc(); // sugerir recoleccion de oldGrid
  }

  /**
   *
   * @return
   */
  public int getPopulation() {
    return data.size();
  }

  /**
   *
   * @return
   */
  public int getWidth() {
    return width;
  }

  /**
   *
   * @return
   */
  public int getHeight() {
    return height;
  }

  /**
   *
   * @param c
   * @return
   */
  public synchronized boolean contains(Cell c) {
    return grid[c.y][c.x];
  }

  /**
   *
   * @param c
   * @return
   */
  public synchronized boolean contains(int x, int y) {
    return grid[y][x];
  }

  /**
   *
   * @return
   */
  public synchronized Collection<Cell> getCells() {
    return data.values();
  }

  /**
   *
   * @return
   */
  public String toString() {

    //Tamanio aproximado del buffer
    int bSize = (width * height)
            + height + 10;
    StringBuffer buff = new StringBuffer(bSize);

    buff.append(width + "," + height + ",\n");
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        if (grid[y][x]) {
          buff.append(LIVE);
        } else {
          buff.append(EMPTY);
        }
      }
      buff.append("\n");
    }
    return buff.toString();
  }
}
