
import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

/*
 * @(#) Cell.java   0.1
 */
/**
 * Implementacion de una bacteria en una posicion coordenada bidimensional (x,y)
 * que mantiene su cuenta de vecinos.
 *
 * @author Eduar C. Velilla
 * @version 0.1
 */
public class Cell {
    /** Coordenada X */
    public int x;
    /** Coordenada Y */
    public int y;
    /**
     * Cantidad de objetos <code>Cell</code> adyacentes a este en la matriz de
     * residencia.
     */
    int neighbors;

    /**
     * Crea una nueva instancia de <code>Cell</code> en la posicion
     * dada.
     *
     * @param x coordenada X
     * @param y coordenada Y
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        neighbors = 0;
    }

    /**
     * Crea una nueva instancia de <code>Cell</code> apartir de uno ya exitstente.
     * NOTE: No es un constructor de copia propiamente. Solo son copiadas las
     * coordenadas del objeto. el atributo <code>neighbors</code> se inicializa a 0.
     *
     * @param c <code>Gell</code> a copiar
     */
    public Cell(Cell c) {
        this(c.x, c.y);
    }

    /**
     * Compara este <code>Cell</code> con el objeto pasado como
     * parametro.
     * El atributo <code>neighbors</code> es descartado en la comparacion.
     *
     * @param o objeto a comparar
     * @return <code>true</code> si son iguales;
     *         <code>false</code> en caso contrario
     */
    public boolean equals(Object o) {

        if (o instanceof Cell) {
            Cell c = (Cell) o;
            return x == c.x && y == c.y;
        }
        return false;
    }

    /**
     * Retorna el codigo hash de este <code>Cell</code>.
     * El codigo hash es representado como un desplazamiento unico en la matriz
     * de residencia. Este valor es utilizado por <code>Hashtable</code>
     * en el proceso de indexacion.
     *
     * @return codigo hash de este <code>Cell</code>
     */
    public int hashCode() {
        return 4000 * y + x; // El numero 4000 es aleatorio puede ser cualquier valor > 1
                             // pero debemos tomarlo como una hipotetica dimension
                             // maxima para la malla.
    }
}
