package juego;

import java.awt.Color;
import entorno.Entorno;

public class Disparo {
    private double x;
    private double y;
    private double ancho;
    private double alto;
    private String dir;

    // Constructor con dirección
    public Disparo(double x, double y, double ancho, double alto, String dir) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.setDir(dir);
    }

    // Método para dibujar el disparo
    public void dibujar(Entorno e) {
        e.dibujarRectangulo(x, y, ancho, alto, 0, Color.YELLOW);
    }

    // Método para mover el disparo
    public void mover() {
        if (dir.equals("der")) {
            this.x += 9; // Mueve a la derecha
        } else {
            this.x -= 9; // Mueve a la izquierda
        }
    }

    // Establecer dirección del disparo
    public void setDir(String dir) {
        this.dir = dir;
    }

    // Método para verificar colisión con el entorno
    public boolean colisionEntorno(Entorno entorno) {
        return this.x + (this.ancho / 2) >= entorno.ancho() || this.x - (this.ancho / 2) <= 0;
    }
    
    public boolean colisionConTortuga(Tortuga tortuga) {
        return this.x + (this.ancho / 2) > tortuga.getX() - (tortuga.getAncho() / 2) &&
               this.x - (this.ancho / 2) < tortuga.getX() + (tortuga.getAncho() / 2) &&
               this.y + (this.alto / 2) > tortuga.getY() - (tortuga.getAlto() / 2) &&
               this.y - (this.alto / 2) < tortuga.getY() + (tortuga.getAlto() / 2);
    }
    
    public boolean colisionConIsla(Isla isla) {
        return this.x + (this.ancho / 2) > isla.getX() - (isla.getAncho() / 2) &&
               this.x - (this.ancho / 2) < isla.getX() + (isla.getAncho() / 2) &&
               this.y + (this.alto / 2) > isla.getY() - (isla.getAlto() / 2) &&
               this.y - (this.alto / 2) < isla.getY() + (isla.getAlto() / 2);
    }

    // Métodos getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAncho() {
        return ancho;
    }

    public double getAlto() {
        return alto;
    }

    public String getDir() {
        return dir;
    }
}
