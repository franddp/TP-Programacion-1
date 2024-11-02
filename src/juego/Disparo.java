package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Disparo {
    private double x;
    private double y;
    private double ancho;
    private double alto;
    private String dir;
    private Image disparod;
    private Image disparoi;

    // Constructor con dirección
    public Disparo(double x, double y, double ancho, double alto, String dir) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.setDir(dir);
        
        disparod= Herramientas.cargarImagen("recursos/disparo/disparod.gif");
        disparoi= Herramientas.cargarImagen("recursos/disparo/disparoi.gif");
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
    
 // Método para dibujar el disparo
    public void dibujar(Entorno e) {
        Image img;
        if (dir.equals("der")) {
        	img = disparod;
        }else {
        	img = disparoi;
        }
        e.dibujarImagen(img, x, y, 0);
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
