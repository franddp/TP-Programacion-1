package juego;

import java.awt.Color;

import entorno.Entorno;

public class Tortuga {
    private int x;
    private int y;
    private boolean viva;
    private int contadorReaparicion;
    private static final int TIEMPO_REAPARICION = 100; // Tiempo en ticks antes de reaparecer

    public Tortuga(int x, int y) {
        this.x = x;
        this.y = y;
        this.viva = true; // La tortuga comienza viva
        this.contadorReaparicion = 0;
    }

    public void actualizar(Isla[] islas, Casa casa) {
        if (!viva) {
            contadorReaparicion++;
            if (contadorReaparicion >= TIEMPO_REAPARICION) {
                reiniciar(); // Reaparece la tortuga
            }
        } else {
            // Lógica de movimiento de la tortuga cuando está viva
        }
    }

    public void dibujar(Entorno entorno) {
        if (viva) {
            // Dibuja la tortuga si está viva
            entorno.dibujarRectangulo(x, y, 20, 20, 0, Color.GREEN);
        }
    }

    public void morir() {
        viva = false; // La tortuga muere
        contadorReaparicion = 0; // Reinicia el contador de reaparición
    }

    private void reiniciar() {
        viva = true; // La tortuga vuelve a la vida
        contadorReaparicion = 0; // Reinicia el contador
        // Coloca la tortuga en su posición inicial o en una isla específica
        // Ejemplo: this.y = nuevaPosicionY;
    }
    
    // Getters y setters según sea necesario
}
