package juego;

import java.awt.Color;
import entorno.Entorno;

public class Gnomo {
    private int x, y, ancho, alto;
    private Color color;
    private boolean enIsla; // Para saber si está en una isla
    private int direccion; // 1 = derecha, -1 = izquierda

    // Constructor
    public Gnomo(int x, int y, int ancho, int alto, Color color) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.color = color;
        this.enIsla = true;
        this.direccion = (Math.random() < 0.5) ? 1 : -1; // Dirección aleatoria
    }

    public void mover() {
        // Mover en la dirección actual (reducir la velocidad a 1)
        x += direccion * 1;

        // Verificar si el gnomo se sale de los límites 
        if (x < 0 || x > 800 - ancho) {
            direccion *= -1; // Cambia de dirección
        }
    }

    public void caer() {
        if (!enIsla) {
            y += 2; // Caer más lentamente
            // Si cae fuera de la pantalla, reiniciar en la casa
            if (y > 600) {
                reiniciarEnCasa();
            }
        }
    }

    private void reiniciarEnCasa() {
        y = 0; // Reiniciar en la parte superior
        x = 400; // Ajustar a la posición de la casa
    }

    public void actualizar(Isla[] islas) {
        // Verificar si el gnomo está en una isla
        enIsla = false;
        for (Isla isla : islas) {
            if (x + ancho / 2 > isla.getX() - isla.getAncho() / 2 &&
                x - ancho / 2 < isla.getX() + isla.getAncho() / 2 &&
                y + alto / 2 >= isla.getY() - isla.getAlto() / 2 &&
                y + alto / 2 <= isla.getY() + isla.getAlto() / 2) {
                enIsla = true; // Está en la isla
                // Colocarlo justo sobre la isla
                y = isla.getY() - (isla.getAlto() / 2 + alto / 2); // Asegúrate de que esté justo arriba
                break;
            }
        }

        // Si no está en una isla, hacer que caiga
        if (!enIsla) {
            caer();
        }
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, color); // Color y forma del gnomo
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
