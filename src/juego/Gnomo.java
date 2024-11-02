package juego;

import java.awt.Color;
import entorno.Entorno;

public class Gnomo {
    private float x, y;
    private int ancho, alto;
    private Color color;
    private boolean enIsla; // Para saber si está en una isla
    private float direccion; // 1 = derecha, -1 = izquierda
    private int contadorEspera; // Contador para el tiempo de espera antes de reaparecer
    private final int tiempoEspera = 300; // Tiempo de espera en ticks (ajústalo según la velocidad del juego)

    // Constructor
    public Gnomo(int x, int y, int ancho, int alto, Color color) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.color = color;
        this.enIsla = true;
        this.direccion = (Math.random() < 0.5) ? 1 : -1; // Dirección aleatoria
        this.contadorEspera = 0; // Inicializa el contador de espera
    }

    public int getAncho() {
		return ancho;
	}

	public int getAlto() {
		return alto;
	}

	public int getTiempoEspera() {
		return tiempoEspera;
	}

	public void mover() {
        // Mover en la dirección actual (reducir la velocidad a 1)
        if (contadorEspera <= 0) { // Solo se mueve si no está en espera
            x += direccion * 0.5;

            // Verificar si el gnomo se sale de los límites 
            if (x < 0 || x > 800 - ancho) {
                direccion *= -1; // Cambia de dirección
            }
        }
    }

    public void caer() {
        if (!enIsla && contadorEspera <= 0) { // Solo cae si no está en una isla y no está en espera
            y += 2; // Caer más lentamente
            // Si cae fuera de la pantalla, iniciar espera
            if (y > 600) {
                iniciarEspera();
            }
        }
    }

    public void iniciarEspera() {
        // Inicia el tiempo de espera antes de reaparecer
        contadorEspera = tiempoEspera;
        
    }

    private void reiniciarEnCasa() {
    	iniciarEspera();
        y = 0; // Reiniciar en la parte superior
        x = 400; // Ajustar a la posición de la casa
        enIsla = true; // Reiniciar estado en isla
    }

    public void actualizar(Isla[] islas) {
        // Disminuye el contador de espera cada tick
        if (contadorEspera > 0) {
            contadorEspera--; // Decrementa el contador de espera
            if (contadorEspera == 0) {
                reiniciarEnCasa(); // Reaparece el gnomo en la casa al finalizar el tiempo de espera
            }
        } else {
            // Verificar si el gnomo está en una isla
            boolean estabaEnIsla = enIsla; // Para saber si estaba en una isla antes de este frame
            enIsla = false;

            for (Isla isla : islas) {
                if (x + ancho / 2 > isla.getX() - isla.getAncho() / 2 &&
                    x - ancho / 2 < isla.getX() + isla.getAncho() / 2 &&
                    y + alto / 2 >= isla.getY() - isla.getAlto() / 2 &&
                    y + alto / 2 <= isla.getY() + isla.getAlto() / 2) {
                    enIsla = true; // Está en la isla
                    y = isla.getY() - (isla.getAlto() / 2 + alto / 2); // Colocarlo justo sobre la isla

                    // Si no estaba en una isla en el frame anterior, asignar nueva dirección aleatoria
                    if (!estabaEnIsla) {
                        direccion = (Math.random() < 0.5) ? 1 : -1;
                    }
                    break;
                }
            }

            // Si no está en una isla, hacer que caiga
            if (!enIsla) {
                caer();
            }
        }
    }

    public void dibujar(Entorno entorno) {
        if (contadorEspera <= 0) { // Dibuja solo si no está en tiempo de espera
            entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, color); // Color y forma del gnomo
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}