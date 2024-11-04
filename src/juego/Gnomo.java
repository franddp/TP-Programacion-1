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
    private final int tiempoEspera = 120; // Tiempo de espera en ticks (ajústalo según la velocidad del juego)
    private boolean enEspera; // Variable para controlar si el gnomo está en espera
    
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
        this.enEspera = false;
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

	public boolean estaEnEspera() {
        return enEspera;
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

    private void iniciarEspera() {
        // Inicia el tiempo de espera antes de reaparecer
        contadorEspera = tiempoEspera;
        enEspera = true; // Marca el gnomo como en espera
        
    }

    private void reiniciarEnCasa() {
    	iniciarEspera();
        y = 0; // Reiniciar en la parte superior
        x = 400; // Ajustar a la posición de la casa
        enIsla = true; // Reiniciar estado en isla
        enEspera = false; // Sale del estado de espera
    }
    

    public void actualizar(Isla[] islas) {
    	
    	if (contadorEspera > 0) {
            contadorEspera--;
            if (contadorEspera == 0) {
                reiniciarEnCasa(); // Solo reaparece cuando el contador llega a 0
            }
        } else {
            boolean estabaEnIsla = enIsla;
            enIsla = false;
            for (Isla isla : islas) {
                if (x + ancho / 2 > isla.getX() - isla.getAncho() / 2 &&
                    x - ancho / 2 < isla.getX() + isla.getAncho() / 2 &&
                    y + alto / 2 >= isla.getY() - isla.getAlto() / 2 &&
                    y + alto / 2 <= isla.getY() + isla.getAlto() / 2) {
                    enIsla = true;
                    y = isla.getY() - (isla.getAlto() / 2 + alto / 2);
                    if (!estabaEnIsla) {
                        direccion = (Math.random() < 0.5) ? 1 : -1;
                    }
                    break;
                }
            }
            if (!enIsla) {
                caer();
            }
        }
    }

    public void dibujar(Entorno entorno) {
    	if (!enEspera) {
            entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, color);
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}