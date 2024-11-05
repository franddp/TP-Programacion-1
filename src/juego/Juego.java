package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
    // Estado del juego
    private boolean juegoTerminado = false; // Indica si el juego ha terminado
    private boolean mostrarPantallaCarga = true; // Controla la visualizacion de la pantalla de carga
    private Entorno entorno; // Entorno de juego
    private Isla[] islas; // Array de islas
    private Pep pep; // Personaje principal
    private Disparo disparo; // Disparo del personaje 
    private Gnomo[] gnomos; // Array de gnomos
    private Casa casa; // Casa donde se llevan los gnomos
    private int gnomosEnPantalla; // Contador de gnomos visibles
    private int maxGnomos; // Maximo numero de gnomos permitidos en pantalla
    private Tortuga[] tortugas; // Array de tortugas
    private int cantidadTortugas = 3; // Cantidad de tortugas en el juego
    private int vidas = 3; // Numero de vidas del jugador
    private int tiempo = 3600; // Tiempo total del juego en ticks
    private int gnomosRescatados = 0; // Contador de gnomos rescatados
    private int enemigosEliminados = 0; // Contador de enemigos eliminados

    // Constructor del juego
    public Juego() {
        // Inicializa el entorno del juego
        this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);

        // Configuracion inicial
        maxGnomos = 4;
        gnomosEnPantalla = 0;
        rellenarIslas(); // Llenar el array de islas

        // Inicializacion de la casa
        int xCasa = 400;
        int yCasa = 65;
        this.casa = new Casa(xCasa, yCasa, 60, 30, Color.RED); // Casa de color rojo

        // Posicion inicial de Pep
        int posicionInicialPepX = islas[0].getX();
        int posicionInicialPepY = islas[0].getY() - 50 / 2; // Ajuste de altura
        this.pep = new Pep(posicionInicialPepX, posicionInicialPepY, 25, 25, Color.RED); // Personaje Pep

        // Inicializacion de gnomos
        gnomos = new Gnomo[maxGnomos];

        // Inicializacion de tortugas
        this.tortugas = new Tortuga[cantidadTortugas];
        for (int i = 0; i < cantidadTortugas; i++) {
            int posicionInicialX = 30 + i * 250; // Posicion inicial de cada tortuga
            int posicionInicialY = islas[i + 1].getY() - 20; // Ajuste en la posicion vertical
            tortugas[i] = new Tortuga(posicionInicialX, posicionInicialY); // Crear tortuga
        }

        this.entorno.iniciar(); // Inicia el entorno del juego
    }

    // Metodo principal que se ejecuta en cada tick del juego
    public void tick() {
        // Mostrar pantalla de carga si corresponde
        if (mostrarPantallaCarga) {
            mostrarPantallaCarga(); // Muestra la pantalla de carga
            return;
        }
        
        // Actualizar estado del juego si no ha terminado
        if (!juegoTerminado) {
            // Mostrar informacion en pantalla
            entorno.cambiarFont("Arial", 15, Color.CYAN);
            entorno.escribirTexto("Vidas: " + vidas, 10, 15);
            entorno.escribirTexto("Tiempo: " + (tiempo / 60), 10, 55); // Convertir ticks a segundos
            entorno.escribirTexto("Enemigos Eliminados: " + enemigosEliminados, 10, 35);
            entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, 10, 75);

            tiempo--; // Decrementar el tiempo por cada tick
            
            // Control de movimiento de Pep
            if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
                pep.moverDerecha(800); // Mover a la derecha
            }
            if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
                pep.moverIzquierda(); // Mover a la izquierda
            }
            if (entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
                pep.saltar(); // Saltar
            }

            pep.caer(); // Aplicar gravedad a Pep

            boolean pepSobreIsla = false; // Flag para verificar si Pep esta sobre una isla

            // Verificar colisiones de Pep con las islas
            for (Isla isla : islas) {
                // Comprobar si Pep esta saltando y colisiona con la isla
                if (pep.estaSaltando() &&
                    pep.getY() - pep.getAlto() / 2 <= isla.getY() + isla.getAlto() / 2 &&
                    pep.getY() > isla.getY() &&
                    pep.getX() + pep.getAncho() / 2 > isla.getX() - isla.getAncho() / 2 &&
                    pep.getX() - pep.getAncho() / 2 < isla.getX() + isla.getAncho() / 2) {
                    
                    pep.caerInmediatamente(); // Pep cae inmediatamente si colisiona al saltar
                }

                // Comprobar si Pep esta cayendo y colisiona con la isla
                if (pep.estaCayendo() &&
                    pep.getY() + pep.getAlto() / 2 >= isla.getY() - isla.getAlto() / 2 &&
                    pep.getY() < isla.getY() &&
                    pep.getX() + pep.getAncho() / 2 > isla.getX() - isla.getAncho() / 2 &&
                    pep.getX() - pep.getAncho() / 2 < isla.getX() + isla.getAncho() / 2) {
                    
                    pep.detenerSaltoEnIsla(isla.getY() - isla.getAlto() / 2); // Detener el salto en la isla
                    pepSobreIsla = true; // Pep esta sobre la isla
                }
            }

            // Si Pep no esta sobre ninguna isla, actualizar su estado
            if (!pepSobreIsla) {
                pep.salirDeIsla(); // Pep sale de la isla si no esta sobre ella
            }

            // Si Pep no esta sobre una isla y esta cayendo, aplicar caida
            if (!pepSobreIsla && (pep.estaCayendo() || pep.getY() < 500)) {
                pep.caer(); // Pep continua cayendo
            }
     // Verificar si Pep cae fuera de la pantalla
        if (pep.getY() > 600) {
            vidas--; // Pierde una vida
            if (vidas == 0) {
                juegoTerminado = true; // Termina el juego si no quedan vidas
            } else {
                // Reaparece en la posición inicial
                pep.reaparecer(islas[0].getX(), islas[0].getY() - 50 / 2);
            }
        }

        if (entorno.sePresiono('c') && disparo == null) {
            disparo = pep.disparar();
        }
        
        if (disparo != null) {
            disparo.dibujar(entorno);
            disparo.mover();

            // Verificar si el disparo choca con el borde del entorno
            if (disparo.colisionEntorno(entorno)) {
                disparo = null;
            } else {
                // Verificar colisión con islas
                for (int i = 0; i < islas.length; i++) {
                    if (disparo == null) break;  // Salir si disparo es null

                    if (islas[i] != null &&
                        islas[i].getX() + islas[i].getAncho() / 2 > disparo.getX() - disparo.getAncho() / 2 &&
                        islas[i].getX() - islas[i].getAncho() / 2 < disparo.getX() + disparo.getAncho() / 2 &&
                        islas[i].getY() + islas[i].getAlto() / 2 > disparo.getY() - disparo.getAlto() / 2 &&
                        islas[i].getY() - islas[i].getAlto() / 2 < disparo.getY() + disparo.getAlto() / 2) {

                        disparo = null; // Eliminar el disparo tras impactar con una isla
                        break; // Salir del bucle tras impactar con una isla
                    }
                }

                // Verificar colisión con gnomos
                for (int i = 0; i < gnomos.length; i++) {
                	if (pep.getY() > islas[2].getY()) {
                		if (disparo == null) break;  // Salir si disparo es null
                        
                        if (gnomos[i] != null &&
                            gnomos[i].getX() + gnomos[i].getAncho() / 2 > disparo.getX() - disparo.getAncho() / 2 &&
                            gnomos[i].getX() - gnomos[i].getAncho() / 2 < disparo.getX() + disparo.getAncho() / 2 &&
                            gnomos[i].getY() + gnomos[i].getAlto() / 2 > disparo.getY() - disparo.getAlto() / 2 &&
                            gnomos[i].getY() - gnomos[i].getAlto() / 2 < disparo.getY() + disparo.getAlto() / 2 ) {
                            
                            gnomos[i] = null; // Eliminar al gnomo
                            gnomosRescatados++; // Aumentar el puntaje
                            disparo = null; // Eliminar el disparo tras impactar
                            break; // Salir del bucle tras eliminar un gnomo
                        }
                    }
                	}
                    

                // Verificar colisión con tortugas
                for (int i = 0; i < tortugas.length; i++) {
                    if (disparo == null) break;  // Salir si disparo es null
                    
                    if (tortugas[i] != null &&
                        tortugas[i].getX() + tortugas[i].getAncho() / 2 > disparo.getX() - disparo.getAncho() / 2 &&
                        tortugas[i].getX() - tortugas[i].getAncho() / 2 < disparo.getX() + disparo.getAncho() / 2 &&
                        tortugas[i].getY() + tortugas[i].getAlto() / 2 > disparo.getY() - disparo.getAlto() / 2 &&
                        tortugas[i].getY() - tortugas[i].getAlto() / 2 < disparo.getY() + disparo.getAlto() / 2) {
                        
                        tortugas[i] = null; // Eliminar a la tortuga
                        enemigosEliminados++; // Aumentar el contador de enemigos eliminados
                        disparo = null; // Eliminar el disparo tras impactar
                        break; // Salir del bucle tras eliminar una tortuga
                    }
                }
            }
        }


        for (int i = 0; i < gnomos.length; i++) {
            if (gnomos[i] != null) {
                gnomos[i].mover();
                gnomos[i].actualizar(islas);

                if (gnomos[i].getY() > 600) {
                    gnomos[i] = null;
                    gnomosEnPantalla--;
                }
            }
        }

        for (int i = 0; i < gnomos.length; i++) {
            if (gnomos[i] != null) {
                if (pep.getY() > 300 &&
                    pep.getX() + pep.getAncho() / 2 > gnomos[i].getX() - gnomos[i].getAncho() / 2 &&
                    pep.getX() - pep.getAncho() / 2 < gnomos[i].getX() + gnomos[i].getAncho() / 2 &&
                    pep.getY() + pep.getAlto() / 2 > gnomos[i].getY() - gnomos[i].getAlto() / 2 &&
                    pep.getY() - pep.getAlto() / 2 < gnomos[i].getY() + gnomos[i].getAlto() / 2) {
                    
                    gnomos[i] = null;
                    gnomosEnPantalla--;
                    gnomosRescatados++;
                }
            }
        }

        if (gnomosEnPantalla < maxGnomos) {
            crearGnomoDesdeCasa();
        }

        for (int i = 0; i < tortugas.length; i++) {
            Tortuga tortuga = tortugas[i];
            if (tortuga != null) {
                tortuga.actualizar(islas, casa);
                tortuga.dibujar(entorno);

                // Colisión entre Pep y una tortuga
                if (pep != null &&
                    pep.getX() + pep.getAncho() / 2 > tortuga.getX() - tortuga.getAncho() / 2 &&
                    pep.getX() - pep.getAncho() / 2 < tortuga.getX() + tortuga.getAncho() / 2 &&
                    pep.getY() + pep.getAlto() / 2 > tortuga.getY() - tortuga.getAlto() / 2 &&
                    pep.getY() - pep.getAlto() / 2 < tortuga.getY() + tortuga.getAlto() / 2) {
                    
                    vidas--;
                    if (vidas > 0) {
                        int posicionInicialPepX = islas[0].getX();
                        int posicionInicialPepY = islas[0].getY() - 50 / 2;
                        pep = new Pep(posicionInicialPepX, posicionInicialPepY, 25, 25, Color.RED);
                    } else {
                        juegoTerminado = true;
                    }
                    tortugas[i] = null;
                }

                // Colisión entre Gnomo y una tortuga
                for (int j = 0; j < gnomos.length; j++) {
                    if (gnomos[j] != null &&
                        gnomos[j].getX() + gnomos[j].getAncho() / 2 > tortuga.getX() - tortuga.getAncho() / 2 &&
                        gnomos[j].getX() - gnomos[j].getAncho() / 2 < tortuga.getX() + tortuga.getAncho() / 2 &&
                        gnomos[j].getY() + gnomos[j].getAlto() / 2 > tortuga.getY() - tortuga.getAlto() / 2 &&
                        gnomos[j].getY() - gnomos[j].getAlto() / 2 < tortuga.getY() + tortuga.getAlto() / 2) {
                        
                        gnomos[j] = null;
                        gnomosEnPantalla--;
                    }
                }
            }
        }
        
     // Reaparición de gnomos al ser eliminados por el disparo o caer fuera de pantalla
        if (gnomosEnPantalla < maxGnomos) {
            for (int i = 0; i < gnomos.length; i++) {
                if (gnomos[i] == null) {
                    // Crear un nuevo gnomo en la posición de la casa o en otra posición inicial
                    gnomos[i] = new Gnomo(casa.getX() + casa.getAncho() / 2, casa.getY(), 25, 25, Color.BLUE);
                    gnomosEnPantalla++; // Incrementa el conteo solo cuando un gnomo reaparece
                    break; // Solo reaparece un gnomo por tick si falta
                }
            }
        }

        // Actualizar el conteo cuando un gnomo es eliminado
        for (int i = 0; i < gnomos.length; i++) {
            if (gnomos[i] == null) {
                gnomosEnPantalla--; // Reducir el conteo cuando un gnomo es eliminado
            }
        }


        // Reaparición de tortugas en el arreglo al ser eliminadas
        for (int i = 0; i < tortugas.length; i++) {
            if (tortugas[i] == null) {
                // Reaparecer una tortuga en una posición inicial, por ejemplo, cerca de la isla
                int posicionInicialX = 30 + i * 250;
                int posicionInicialY = islas[i + 1].getY() - 20;
                tortugas[i] = new Tortuga(posicionInicialX, posicionInicialY);
                break; // Solo reaparece una tortuga por tick si falta
            }
        }

        
        
        //Dibujar las islas
        for (Isla isla : islas) {
            isla.dibujar(entorno);
        }
        //Dibujar la casa
        casa.dibujar(entorno);
        
        //Dibujar a Pep
        pep.dibujar(entorno);
        
        //Dibujar los gnomos
        for (Gnomo gnomo : gnomos) {
            if (gnomo != null) {
                gnomo.dibujar(entorno);
            }
        }
        
        if (vidas == 0 || tiempo <= 0) {
            juegoTerminado = true;
        }

//        if (juegoTerminado) {
//            mostrarPantallaFinal(); // Mostrar pantalla final
//            return;
//        }
//    

    
      
    }

    }//aca termina el tick

    private void mostrarPantallaCarga() {
        entorno.cambiarFont("Arial", 24, Color.BLUE);
        
        // Escribir el texto centrado
        entorno.escribirTexto("Pantalla de Carga", entorno.ancho() / 2, 100); // Título
        entorno.escribirTexto("Presiona 'J' para Jugar", entorno.ancho() / 2, 150); // Opción Jugar
        entorno.escribirTexto("Presiona 'E' para Salir", entorno.ancho() / 2, 200); // Opción Salir
        
        // Comprobar entradas
        if (entorno.sePresiono('j')) {
            mostrarPantallaCarga = false; // Iniciar el juego
        } else if (entorno.sePresiono('e')) {
            System.exit(0); // Salir del juego
        }
    }

    //Metodo para crear gnomos
    private void crearGnomoDesdeCasa() {
        if (gnomosEnPantalla < maxGnomos) {
            int xGnomo = casa.getX() + casa.getAncho() / 2 - 12;

            Isla islaMasAlta = islas[0];
            for (Isla isla : islas) {
                if (isla.getY() < islaMasAlta.getY()) {
                    islaMasAlta = isla;
                }
            }
            
            int yGnomo = islaMasAlta.getY() - 40;

            for (int i = 0; i < gnomos.length; i++) {
                if (gnomos[i] == null) {
                    gnomos[i] = new Gnomo(xGnomo, yGnomo, 25, 25, Color.BLUE);
                    gnomosEnPantalla++;
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        new Juego();
    }


public void rellenarIslas() {
	islas = new Isla[15];
    for (int i = 0; i < islas.length; i++) {
        int x, y;

        if (i < 5) { 
            x = 60 + i * 170;
            y = 500;
        } else if (i < 9) {
            x = 100 + (i - 5) * 180;
            y = 400;
        } else if (i < 12) {
            x = 190 + (i - 9) * 180;
            y = 300;
        } else if (i < 14) {
            x = 305 + (i - 12) * 170;
            y = 200;
        } else {
            x = 400;
            y = 100;
        }

        islas[i] = new Isla(x, y, 120, 40, Color.GREEN);
    }
}
}