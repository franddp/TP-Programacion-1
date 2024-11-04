package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Pep {
    private int x, y;
    private int ancho, alto;

    private boolean enSuelo;
    private double velocidadY;
    private final double gravedad = 0.4;
    private Disparo disparo;
    private String ultimaDir;
    @SuppressWarnings("unused")
	private Image img;
    private Image correrd;
    private Image correri;
    private Image saltod;
    private Image saltoi;
    private Image quietod;
    private Image quietoi;
    private int ultimaX;
    private int ultimaY;
	@SuppressWarnings("unused")
	private Color color;

    public Pep(int x, int y, int ancho, int alto, Color color) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.color = color;
        this.enSuelo = true;
        this.velocidadY = 0;
        this.ultimaDir = "";
        
        // Cargar las imágenes solo una vez
        correrd = Herramientas.cargarImagen("recursos/pep/correrd.gif");
        correri = Herramientas.cargarImagen("recursos/pep/correri.gif");
        saltod = Herramientas.cargarImagen("recursos/pep/saltod.gif");
        saltoi = Herramientas.cargarImagen("recursos/pep/saltoi.gif");
        quietod = Herramientas.cargarImagen("recursos/pep/quietod.gif");
        quietoi=Herramientas.cargarImagen("recursos/pep/quietoi.gif");
        
        // Inicializar ultima posición
        actualizarPosicion();
    }

    // Comprueba si Pep no se movió desde la última posición registrada
    public boolean estaQuieto() {
        return x == ultimaX && y == ultimaY;
    }

    // Actualiza última posición después de cada ciclo de dibujado
    private void actualizarPosicion() {
        ultimaX = x;
        ultimaY = y;
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

    public void saltar() {
        if (enSuelo) {
            velocidadY = -11;
            enSuelo = false;
        }
    }
 // Métodos de estado
    public boolean estaSaltando() {
        // Devuelve true si Pep se está moviendo hacia arriba (velocidadY negativa)
        return velocidadY < 0 && !enSuelo;
    }

    public boolean estaCayendo() {
        // Devuelve true si Pep se está moviendo hacia abajo (velocidadY positiva) y no está en el suelo
        return velocidadY > 0 && !enSuelo;
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

 // Métodos de dibujo
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
        entorno.dibujarImagen(img, x, y, 0);

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
}
