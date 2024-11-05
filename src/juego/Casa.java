package juego;

import java.awt.Color;
import entorno.Entorno;

public class Casa {
    private int x; // Posición en X
    private int y; // Posición en Y
    private int ancho; // Ancho de la casa
    private int alto; // Alto de la casa
    private Color color; // Color de la casa

    // Constructor
    public Casa(int x, int y, int ancho, int alto, Color color) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.color = color;
    }

    // Método para dibujar la casa
    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(x, y, ancho, alto, 0, color);
    }

    // Métodos para obtener la posición y dimensiones 
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }
}
