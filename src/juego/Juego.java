package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
    private Entorno entorno;
    private Isla[] islas;
    private Pep pep;
    private Disparo disparo;
    private Gnomo[] gnomos;
    private Casa casa; // Clase Casa
    private int gnomosEnPantalla; // Contador de gnomos en pantalla
    private int maxGnomos; // Número máximo de gnomos en pantalla
    private Tortuga[] tortugas;
    private int cantidadTortugas = 3; // Número de tortugas que deseas en el juego

    // Constructor de la clase Juego
    public Juego() {
        this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
        
        // Inicializar variables
        maxGnomos = 4; // Establecer el número máximo de gnomos
        gnomosEnPantalla = 0; // Inicialmente no hay gnomos

     // Crear las islas
        islas = new Isla[15];
        for (int i = 0; i < islas.length; i++) {
            int x, y;

            if (i < 5) { // Fila inferior (5 islas)
                x = 60 + i * 170; // Espaciado original
                y = 500; // Altura de la fila inferior
            } else if (i < 9) { // Fila media (4 islas)
                x = 100 + (i - 5) * 180; // Espaciado original
                y = 400; // Altura de la fila media
            } else if (i < 12) { // Fila superior (3 islas)
                x = 190 + (i - 9) * 180; 
                y = 300; // Altura de la fila superior
            } else if (i < 14) { // Segunda fila desde arriba (2 islas)
                x = 305 + (i - 12) * 170; // Espaciado original
                y = 200; // Altura de la segunda fila desde arriba
            } else { // Isla superior (1 isla)
                x = 400; // Centro
                y = 100; // Altura de la isla superior
            }

            islas[i] = new Isla(x, y, 120, 40, Color.GREEN);
        }

        // Crear la casa en la isla más alta
        int xCasa = 400; // Ajustar según la posición de la isla
        int yCasa = 65; // La casa se sitúa encima de la isla
        this.casa = new Casa(xCasa, yCasa, 60, 30, Color.RED); // Crear la casa

        // Crear a Pep y situarlo en la isla más baja
        int posicionInicialPepX = islas[0].getX(); // Misma X que la primera isla
        int posicionInicialPepY = islas[0].getY() - 50 / 2; // Justo sobre la isla más baja
        this.pep = new Pep(posicionInicialPepX, posicionInicialPepY, 25, 25, Color.RED);

        // Inicializar gnomos
        gnomos = new Gnomo[maxGnomos]; // Puedes ajustar la cantidad de gnomos
        
     // Crear tortugas
        this.tortugas = new Tortuga[cantidadTortugas];
        for (int i = 0; i < cantidadTortugas; i++) {
            tortugas[i] = new Tortuga(30, 20); // Especifica ancho y alto de cada tortuga
        }

        this.entorno.iniciar();
    }

 // Método que se llama en cada tick del juego
    public void tick() {
        // Mover Pep
        if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
            pep.moverDerecha(800); // Limitar al borde derecho
        }
        if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
            pep.moverIzquierda(); // Limitar al borde izquierdo
        }
        if (entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
            pep.saltar();
        }

        pep.caer(); // Hacer que caiga si no está en el suelo

        // Variable para verificar si Pep está sobre alguna isla
        boolean pepSobreIsla = false;

        // Detectar colisiones y detener la caída si Pep está cayendo
        for (Isla isla : islas) {
            // Verificar si Pep está tocando la isla desde abajo
            if (pep.estaSaltando() && 
                pep.getY() - pep.getAlto() / 2 <= isla.getY() + isla.getAlto() / 2 && // Parte superior de Pep toca la parte inferior de la isla
                pep.getY() > isla.getY() && // Pep está debajo de la isla
                pep.getX() + pep.getAncho() / 2 > isla.getX() - isla.getAncho() / 2 && // Pep está dentro del rango horizontal de la isla
                pep.getX() - pep.getAncho() / 2 < isla.getX() + isla.getAncho() / 2) {
                
                // Si Pep toca la isla desde abajo, lo empujamos hacia abajo
                pep.caerInmediatamente();
            }

            // Si Pep está cayendo desde arriba y toca la parte superior de la isla, se detiene
            if (pep.estaCayendo() && 
                pep.getY() + pep.getAlto() / 2 >= isla.getY() - isla.getAlto() / 2 && // Parte inferior de Pep toca la parte superior de la isla
                pep.getY() < isla.getY() && // Pep debe estar sobre la isla
                pep.getX() + pep.getAncho() / 2 > isla.getX() - isla.getAncho() / 2 && // Pep está dentro del rango horizontal de la isla
                pep.getX() - pep.getAncho() / 2 < isla.getX() + isla.getAncho() / 2) {
                
                // Detener a Pep solo si está cayendo desde arriba
                pep.detenerSaltoEnIsla(isla.getY() - isla.getAlto() / 2);
                pepSobreIsla = true; // Pep está sobre una isla
            }
        }

        // Si Pep no está sobre ninguna isla, debe caer
        if (!pepSobreIsla) {
            pep.salirDeIsla(); // Desactivar la condición de "en suelo" para que caiga
        }

        // Ajuste para caer si se mueve rápido
        if (!pepSobreIsla && (pep.estaCayendo() || pep.getY() < 500)) {
            pep.caer();
        }

        // Manejo de disparos
        if (entorno.sePresiono('c') && disparo == null) {
            disparo = pep.disparar();
        }
        
        // Mover y dibujar el disparo si existe
        if (disparo != null) {
            disparo.dibujar(entorno);
            disparo.mover();
            // Verificar si el disparo colisiona con el entorno
            if (disparo.colisionEntorno(entorno)) {
                disparo = null;
            } 
        }

        // Mover y gestionar gnomos
        for (int i = 0; i < gnomos.length; i++) {
            if (gnomos[i] != null) {
                gnomos[i].mover(); // Mover cada gnomo
                gnomos[i].actualizar(islas); // Actualizar posición del gnomo

                // Verificar si el gnomo ha caído fuera de la pantalla
                if (gnomos[i].getY() > 600) {
                    gnomos[i] = null; // Eliminar el gnomo
                    gnomosEnPantalla--; // Disminuir el contador de gnomos
                }
            }
        }

        // Aquí es donde agregamos la detección de colisión entre Pep y los gnomos
        for (int i = 0; i < gnomos.length; i++) {
            if (gnomos[i] != null) {
                // Verificar colisión entre Pep y el gnomo
                if (pep.getY() > 300 && // Comprobar que Pep esté en la parte inferior (filas de 5 o 4 islas)
                    pep.getX() + pep.getAncho() / 2 > gnomos[i].getX() - gnomos[i].getAncho() / 2 &&
                    pep.getX() - pep.getAncho() / 2 < gnomos[i].getX() + gnomos[i].getAncho() / 2 &&
                    pep.getY() + pep.getAlto() / 2 > gnomos[i].getY() - gnomos[i].getAlto() / 2 &&
                    pep.getY() - pep.getAlto() / 2 < gnomos[i].getY() + gnomos[i].getAlto() / 2) {
                    
                    // Si hay colisión, eliminar el gnomo
                    gnomos[i] = null; // Eliminar el gnomo
                    gnomosEnPantalla--; // Reducir el contador de gnomos en pantalla
                }
            }
        }

        // Generar nuevos gnomos si hay espacio
        if (gnomosEnPantalla < maxGnomos) {
            crearGnomoDesdeCasa();
        }
        for (Tortuga tortuga : tortugas) {
            tortuga.actualizar(islas); // Actualiza su posición y estado respecto a las islas
            tortuga.dibujar(entorno); // Dibuja la tortuga en el entorno
        }

        // Dibuja las islas, Pep, disparos y gnomos
        for (Isla isla : islas) {
            isla.dibujar(entorno);
        }
        casa.dibujar(entorno); // Dibuja la casa
        pep.dibujar(entorno); // Dibuja a Pep

        for (Gnomo gnomo : gnomos) {
            if (gnomo != null) {
                gnomo.dibujar(entorno);
            }
        }
    }


 // Método que crea un gnomo en la posición de la casa
    private void crearGnomoDesdeCasa() {
        if (gnomosEnPantalla < maxGnomos) {
            // Ajustar la posición del gnomo a la casa
            int xGnomo = casa.getX() + casa.getAncho() / 2 - 12; // Centro de la casa

            // Obtener la isla más alta y su posición
            Isla islaMasAlta = islas[0]; // Inicializamos con la primera isla
            for (Isla isla : islas) {
                if (isla.getY() < islaMasAlta.getY()) { // Encontrar la isla más alta
                    islaMasAlta = isla;
                }
            }
            
            int yGnomo = islaMasAlta.getY() - 40; // Ajustar la posición del gnomo justo sobre la isla

            for (int i = 0; i < gnomos.length; i++) {
                if (gnomos[i] == null) { // Solo crear un gnomo si hay espacio
                    gnomos[i] = new Gnomo(xGnomo, yGnomo, 25, 25, Color.BLUE); // Crear el gnomo en la posición de la casa
                    gnomosEnPantalla++; // Incrementar el contador de gnomos en pantalla
                    break; // Salir del bucle una vez que se crea un gnomo
                }
            }
        }
    }

    public static void main(String[] args) {
        new Juego(); // Iniciar el juego
    }
}
