package juego;

import java.awt.Color;
import java.util.Random;
import entorno.Entorno;

public class Tortuga {
    private float x, y;
    private int ancho, alto;
    private float direccion; // 1 para derecha, -1 para izquierda
    private boolean enIsla; // Indica si está en una isla
    private int contadorEspera; // Contador para el tiempo de espera
    private int tiempoEspera = 120; // Tiempo de espera en ticks
    private Random random;

    public Tortuga(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        this.random = new Random();
        this.x = generarPosicionXInicial(); // Genera una posición aleatoria en X
        this.y = 0; // Comienza cayendo desde la parte superior
        this.direccion = (random.nextFloat() < 0.5) ? 1 : -1; // Dirección inicial aleatoria
        this.enIsla = false;
        this.contadorEspera = 0;
    }

    // Genera una posición inicial en X, evitando el área de la casa de los gnomos
    private float generarPosicionXInicial() {
        float posicionX = random.nextInt(800 - ancho);
        if (posicionX > 300 && posicionX < 500) { // Ajusta estos valores según la posición de la casa
            posicionX = 100; // Alternativa en caso de que esté en el área de la casa
        }
        return posicionX;
    }

    // Método para la caída
    public void caer() {
        if (!enIsla && contadorEspera <= 0) {
            y += 2; // Velocidad de caída
            if (y > 600) { // Si la tortuga cae fuera de la pantalla, inicia la espera
                iniciarEspera();
            }
        }
    }

    // Inicia el tiempo de espera antes de reaparecer
    private void iniciarEspera() {
        contadorEspera = tiempoEspera;
    }

    // Reinicia la tortuga en la parte superior de la pantalla con nueva posición X
    private void reiniciar() {
        iniciarEspera();
        x = generarPosicionXInicial();
        y = 0;
        enIsla = false;
    }

    // Actualiza la posición y colisiones con las islas
    public void actualizar(Isla[] islas) {
        if (contadorEspera > 0) {
            contadorEspera--;
            if (contadorEspera == 0) {
                reiniciar();
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
                        direccion = (random.nextFloat() < 0.5) ? 1 : -1;
                    }
                    break;
                }
            }

            if (!enIsla) {
                caer();
            } else {
                mover();
            }
        }
    }

    // Mueve la tortuga en la dirección actual si está en una isla
    public void mover() {
        x += direccion * 0.5;
        if (x < 0 || x > 800 - ancho) {
            direccion *= -1; // Cambia de dirección al llegar a los bordes
        }
    }

    // Dibuja la tortuga en pantalla si no está en tiempo de espera
    public void dibujar(Entorno entorno) {
        if (contadorEspera <= 0) {
            entorno.dibujarRectangulo(x + ancho / 2, y + alto / 2, ancho, alto, 0, Color.WHITE);
        }
    }

    // Getters
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
