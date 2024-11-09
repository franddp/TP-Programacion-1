package juego;

import java.awt.Color;
import entorno.Entorno;

public class NaveDeCombateAerea {
    private int x, y;
    private int ancho, alto;
    private Color color;
    private double velocidadY;
    private String ultimaDir;

    public NaveDeCombateAerea(int x, int y, int ancho, int alto, Color color) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.color = color;
        this.velocidadY = 0;
        this.ultimaDir = "der";
    }

    // Métodos de movimiento
    public void moverDerecha(int limiteDerecho) {
        if (x + ancho / 2 < limiteDerecho) {
            x += 4;
            ultimaDir = "der";
        }
    }

    public void moverIzquierda() {
        if (x - ancho / 2 > 0) {
            x -= 4;
            ultimaDir = "izq";
        }
    }

    

    // Métodos de dibujo
    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(x, y, ancho, alto, 0, color);
    }

    // Métodos getters
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

public Color getColor() {
	return color;
}

public double getVelocidadY() {
	return velocidadY;
}

public String getUltimaDir() {
	return ultimaDir;
}

public void setX(int x) {
	this.x = x;
}

public void setY(int y) {
	this.y = y;
}

public void setAncho(int ancho) {
	this.ancho = ancho;
}

public void setAlto(int alto) {
	this.alto = alto;
}

public void setColor(Color color) {
	this.color = color;
}


public void setVelocidadY(double velocidadY) {
	this.velocidadY = velocidadY;
}


public void setUltimaDir(String ultimaDir) {
	this.ultimaDir = ultimaDir;
}
}