package juego;

import java.awt.Color;
import entorno.Entorno;

public class Isla {
    private int x, y; // Posición
    private int ancho, alto; // Dimensiones
    private Color color;

    public Isla(int x, int y, int ancho, int alto, Color color) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.color = color;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(x, y, ancho, alto, 0, color); // Dibuja la isla
    }

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
