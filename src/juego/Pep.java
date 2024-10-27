package juego;

import java.awt.Color;
import entorno.Entorno;

public class Pep {
    private int x, y;
    private int ancho, alto;
    private Color color;
    private boolean enSuelo;
    private double velocidadY;
    private final double gravedad = 0.4;

    public Pep(int x, int y, int ancho, int alto, Color color) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.color = color;
        this.enSuelo = true;
        this.velocidadY = 0;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(x, y, ancho, alto, 0, color);
    }

    public void moverDerecha(int limiteDerecho) {
        if (x + ancho / 2 < limiteDerecho) {
            x += 4; // Reduce la velocidad de movimiento a 3 píxeles
        }
    }

    public void moverIzquierda() {
        if (x - ancho / 2 > 0) {
            x -= 4; // Reduce la velocidad de movimiento a 3 píxeles
        }
    }

    public void saltar() {
        if (enSuelo) {
            velocidadY = -11; // Aumenta la velocidad de salto para que suba más
            enSuelo = false;
        }
    }

    public void caer() {
        if (!enSuelo) {
            velocidadY += gravedad; // Aumenta la gravedad para que caiga más lentamente
            y += velocidadY;

            if (y >= 500) {
                y = 500;
                enSuelo = true;
                velocidadY = 0;
            }
        }
    }

    public void detenerSaltoEnIsla(int yIsla) {
        this.y = yIsla - alto / 2;
        this.enSuelo = true;
        this.velocidadY = 0;
    }

    public void salirDeIsla() {
        enSuelo = false;
    }

    public void caerInmediatamente() {
        velocidadY = gravedad;
        enSuelo = false;
    }

    public boolean estaSaltando() {
        return velocidadY < 0;
    }

    public boolean estaCayendo() {
        return velocidadY > 0;
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
