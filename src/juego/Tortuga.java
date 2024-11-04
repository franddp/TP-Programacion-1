package juego;

import java.awt.Color;
import java.util.Random;
import entorno.Entorno;

public class Tortuga {
    private double x;
    private double y;
    private double ancho;
    private double alto;
    private double velocidadX;
    private boolean enIsla;
    private Random random;
    private Color color;
    private Isla islaActual;

    // Constructor con valores fijos para ancho y alto
    public Tortuga(double limiteCasaGnomosX, double limiteCasaGnomosAncho) {
        this.ancho = 25; // Valor fijo para evitar tamaños excesivos
        this.alto = 25;  // Valor fijo para evitar tamaños excesivos
        this.y = 0; // Aparece en la parte superior de la pantalla
        this.velocidadX = 0.5;
        this.enIsla = false;
        this.color = Color.PINK;
        this.random = new Random();
        generarPosicionAleatoria(limiteCasaGnomosX, limiteCasaGnomosAncho);
        
    }

    // Método para generar una posición aleatoria en X, evitando el área de la casa de los gnomos
    private void generarPosicionAleatoria(double limiteCasaGnomosX, double limiteCasaGnomosAncho) {
        do {
            this.x = random.nextInt(800 - (int) ancho);
        } while (this.x >= limiteCasaGnomosX && this.x <= (limiteCasaGnomosX + limiteCasaGnomosAncho));
    }


    // Método para verificar si la isla es la que tiene la casa de los gnomos
    private boolean islaEsCasaGnomos(Isla isla, Casa casa) {
        return casa != null && casa.getX() == isla.getX() && casa.getY() == isla.getY();
    }

    // Método para mover en la isla de borde a borde
    private void moverEnIsla() {
        this.x += velocidadX;

        // Revisar los límites de la isla sin Math
        double limiteIzquierdo = islaActual.getX() - islaActual.getAncho() / 2 + ancho / 2;
        double limiteDerecho = islaActual.getX() + islaActual.getAncho() / 2 - ancho / 2;

        // Comprobar si la tortuga se sale de los límites
        if (x <= limiteIzquierdo) {
            x = limiteIzquierdo; // Colocar la tortuga en el límite izquierdo
            velocidadX = -velocidadX; // Cambiar dirección al llegar al borde de la isla
        } else if (x >= limiteDerecho) {
            x = limiteDerecho; // Colocar la tortuga en el límite derecho
            velocidadX = -velocidadX; // Cambiar dirección al llegar al borde de la isla
        }
    }

    // Verificar si hay colisión con la isla
    private boolean colisionConIsla(Isla isla) {
        return x + ancho / 2 > isla.getX() - isla.getAncho() / 2 &&
               x - ancho / 2 < isla.getX() + isla.getAncho() / 2 &&
               y + alto / 2 >= isla.getY() - isla.getAlto() / 2 &&
               y + alto / 2 <= isla.getY() + isla.getAlto() / 2;
    }

    // Verificar si la isla tiene la casa de los gnomos
    private boolean islaTieneCasa(Isla isla, Casa casa) {
        return casa != null && casa.getX() == isla.getX() && casa.getY() == isla.getY();
    }

    // Verificar si hay otra tortuga en la misma isla
    private boolean islaOcupada(Isla[] islas, Isla isla) {
        for (Isla i : islas) {
            if (i == isla && i == islaActual) return true;
        }
        return false;
    }

 // Método para actualizar el estado de la tortuga
    public void actualizar(Isla[] islas, Casa casa) {
        if (!enIsla) {
            caer(islas, casa); // La tortuga sigue cayendo hasta que llegue a una isla
        } else {
            moverEnIsla(); // Una vez en la isla, se mueve de un lado a otro
        }
    }
 // Método para verificar si la tortuga debe caer o quedarse en la isla
    private void caer(Isla[] islas, Casa casa) {
        for (Isla isla : islas) {
            // Verificamos si la isla es la que tiene la casa de los gnomos
            if (colisionConIsla(isla) && !islaTieneCasa(isla, casa) && !islaOcupada(islas, isla) && !islaEsCasaGnomos(isla, casa)) {
                enIsla = true;
                y = isla.getY() - (isla.getAlto() / 2 + alto / 2); // Colocarlo justo sobre la isla
                islaActual = isla;
                velocidadX = (random.nextBoolean() ? 1 : -1) * 0.5;
                return;
            }
        }
        this.y += 1; // Si no colisiona con ninguna isla, cae
    }
    // Método para dibujar la tortuga
    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, color);
    }

    // Métodos getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getAncho() { return ancho; }
    public double getAlto() { return alto; }
}
