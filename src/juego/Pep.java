package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Pep {
    private int x, y;
    private int ancho, alto;
    private Color color;
    private boolean enSuelo;
    private double velocidadY;
    private final double gravedad = 0.4;
    private Disparo disparo;
    private String ultimaDir;
    private boolean sobreNave;// Nueva variable para verificar si está sobre la nave
    private Image img;
    private Image correrd;
    private Image correri;
    private Image saltod;
    private Image saltoi;
    private Image quietod;
    private Image quietoi;
    private int ultimaX;
    private int ultimaY;

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
        
     // Cargar las imágenes solo una vez
        correrd = Herramientas.cargarImagen("recursos/imagenes/correrd.gif");
        correri = Herramientas.cargarImagen("recursos/imagenes/correri.gif");
        saltod = Herramientas.cargarImagen("recursos/imagenes/saltod.gif");
        saltoi = Herramientas.cargarImagen("recursos/imagenes/saltoi.gif");
        quietod = Herramientas.cargarImagen("recursos/imagenes/quietod.gif");
        quietoi=Herramientas.cargarImagen("recursos/imagenes/quietoi.gif");
        
        // Inicializar ultima posición
        actualizarPosicion();
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
    
    private void actualizarPosicion() {
        ultimaX = x;
        ultimaY = y;
    }
    
    // Comprueba si Pep no se movió desde la última posición registrada
    public boolean estaQuieto() {
        return x == ultimaX && y == ultimaY;
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

    public void dibujar(Entorno entorno) {
        Image img;
        // Si Pep está quieto, selecciona la imagen correspondiente
        if (estaQuieto()) {
            img = quietod; // Imagen de Pep quieto mirando a la derecha
        } 
        // Si Pep está quieto y la última dirección fue a la izquierda
        else if (ultimaDir.equals("izq")) {
            img = quietoi; // Imagen de Pep quieto mirando a la izquierda
        } 
        // Si Pep está saltando o cayendo
        else if (!enSuelo) {
            img = ultimaDir.equals("der") ? saltod : saltoi; // Selecciona la imagen de salto según la dirección
        } 
        // Si Pep se está moviendo en el suelo
        else {
            img = ultimaDir.equals("der") ? correrd : correri; // Selecciona la imagen de correr según la dirección
        }
        // Dibuja la imagen en la posición actual
       
        entorno.dibujarImagen(img, x, y, 0, 0.1);
        // Actualizar posición después de dibujar
        actualizarPosicion();
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
