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
    private Disparo disparo;
    private String ultimaDir;
    private boolean sobreNave; // Nueva variable para verificar si está sobre la nave

    public Pep(int x, int y, int ancho, int alto, Color color) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.color = color;
        this.enSuelo = true;
        this.velocidadY = 0;
        this.ultimaDir = "der";
        this.sobreNave = false; // Inicialmente no está sobre la nave
    }

    // Métodos de movimiento
    public void moverDerecha(int limiteDerecho) {
        if (!sobreNave) { // Solo mover si no está sobre la nave
            if (x + ancho / 2 < limiteDerecho) {
                x += 4;
                ultimaDir = "der";
            }
        } else {
            // Si está sobre la nave, se mueve con ella
            x += 4; // Se mueve a la derecha junto con la nave
        }
    }

    public void moverIzquierda() {
        if (!sobreNave) { // Solo mover si no está sobre la nave
            if (x - ancho / 2 > 0) {
                x -= 4;
                ultimaDir = "izq";
            }
        } else {
            // Si está sobre la nave, se mueve con ella
            x -= 4; // Se mueve a la izquierda junto con la nave
        }
    }

    public void saltar() {
        if (enSuelo) {
            velocidadY = -8;
            enSuelo = false;
        }
    }

    public void caer() {
        if (!enSuelo) {
            velocidadY += gravedad;
            y += velocidadY;
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

    // Métodos de disparo
    public Disparo disparar() {
        disparo = new Disparo(this.x, this.y + (alto / 4), 10, 10, ultimaDir);
        return disparo;
    }

    // Métodos de estado
    public boolean estaSaltando() {
        return velocidadY < 0;
    }

    public boolean estaCayendo() {
        return velocidadY > 0;
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

    // Método para verificar colisión con una tortuga
    public boolean colisionConTortuga(Tortuga tortuga) {
        return this.x + this.ancho / 2 > tortuga.getX() - tortuga.getAncho() / 2 &&
               this.x - this.ancho / 2 < tortuga.getX() + tortuga.getAncho() / 2 &&
               this.y + this.alto / 2 > tortuga.getY() - tortuga.getAlto() / 2 &&
               this.y - this.alto / 2 < tortuga.getY() + tortuga.getAlto() / 2;
    }

    // Método para verificar colisión con la nave
    public void colisionConNave(NaveDeCombateAerea nave) {
        // Verificamos si Pep está sobre la nave (y dentro de los límites horizontales)
        if (this.x + this.ancho / 2 > nave.getX() - nave.getAncho() / 2 &&
            this.x - this.ancho / 2 < nave.getX() + nave.getAncho() / 2 &&
            this.y + this.alto / 2 >= nave.getY() - nave.getAlto() / 2 &&
            this.y + this.alto / 2 <= nave.getY() + nave.getAlto() / 2) {
            // Si Pep está sobre la nave, la variable 'sobreNave' es verdadera
            sobreNave = true;
            // La posición vertical de Pep se ajusta a la parte superior de la nave
            this.y = nave.getY() - this.alto / 2;
        } else {
            // Si Pep ya no está sobre la nave, lo dejamos de mover junto con ella
            sobreNave = false;
        }
    }
    
    

    // Método para que Pep deje de seguir la nave
    public void dejarDeSeguirNave() {
        sobreNave = false;
    }

    public void reaparecer(int nuevoX, int nuevoY) {
        this.x = nuevoX;
        this.y = nuevoY;
    }

    public Color getColor() {
        return color;
    }

    public boolean isEnSuelo() {
        return enSuelo;
    }

    public double getVelocidadY() {
        return velocidadY;
    }

    public double getGravedad() {
        return gravedad;
    }

    public Disparo getDisparo() {
        return disparo;
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

    public void setEnSuelo(boolean enSuelo) {
        this.enSuelo = enSuelo;
    }

    public void setVelocidadY(double velocidadY) {
        this.velocidadY = velocidadY;
    }

    public void setDisparo(Disparo disparo) {
        this.disparo = disparo;
    }

    public void setUltimaDir(String ultimaDir) {
        this.ultimaDir = ultimaDir;
    }
}
