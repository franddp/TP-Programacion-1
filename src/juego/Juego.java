package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	private boolean juegoTerminado = false;
	 private boolean mostrarPantallaCarga = true; // Variable para controlar la pantalla de carga
    private Entorno entorno;
    private Isla[] islas;
    private Pep pep;
    private Disparo disparo;
    private Gnomo[] gnomos;
    private Casa casa;
    private int gnomosEnPantalla;
    private int maxGnomos;
    private Tortuga[] tortugas;
    private int cantidadTortugas = 3;
    private int vidas = 3;
    private int tiempo = 3600; //3600 Esto es equivalente a 60 segundos si cada tick es 1/60 de segundo
    private int gnomosRescatados = 0;
    private int enemigosEliminados = 0;


    public Juego() {
        this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);


        maxGnomos = 4;
        gnomosEnPantalla = 0;
        rellenarIslas();

        int xCasa = 400;
        int yCasa = 65;
        this.casa = new Casa(xCasa, yCasa, 60, 30, Color.RED);

        int posicionInicialPepX = islas[0].getX();
        int posicionInicialPepY = islas[0].getY() - 50 / 2;
        this.pep = new Pep(posicionInicialPepX, posicionInicialPepY, 25, 25, Color.RED);

        gnomos = new Gnomo[maxGnomos];

        this.tortugas = new Tortuga[cantidadTortugas];
        for (int i = 0; i < cantidadTortugas; i++) {
            int posicionInicialX = 30 + i * 250;
            int posicionInicialY = islas[i + 1].getY() - 20;
            tortugas[i] = new Tortuga(posicionInicialX, posicionInicialY);
        }

        this.entorno.iniciar();
    }

    public void tick() {
    	
    	if (mostrarPantallaCarga) {
            mostrarPantallaCarga(); // Muestra la pantalla de carga
            return;
        }
    	
    	if (!juegoTerminado) {
    		entorno.cambiarFont("Arial", 15, Color.CYAN);
            entorno.escribirTexto("Vidas: " + vidas, 10, 15);
            entorno.escribirTexto("Tiempo: " + (tiempo / 60), 10, 55); // Convertir ticks a segundos
            entorno.escribirTexto("EnemigosEliminados: " + enemigosEliminados , 10, 35);
            entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, 10, 75);

        tiempo--; //por cada tick baja el tiempo
        
        if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
            pep.moverDerecha(800);
        }
        if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
            pep.moverIzquierda();
        }
        if (entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
            pep.saltar();
        }

        pep.caer();

        boolean pepSobreIsla = false;

        for (Isla isla : islas) {
            if (pep.estaSaltando() &&
                pep.getY() - pep.getAlto() / 2 <= isla.getY() + isla.getAlto() / 2 &&
                pep.getY() > isla.getY() &&
                pep.getX() + pep.getAncho() / 2 > isla.getX() - isla.getAncho() / 2 &&
                pep.getX() - pep.getAncho() / 2 < isla.getX() + isla.getAncho() / 2) {
                
                pep.caerInmediatamente();
            }

            if (pep.estaCayendo() &&
                pep.getY() + pep.getAlto() / 2 >= isla.getY() - isla.getAlto() / 2 &&
                pep.getY() < isla.getY() &&
                pep.getX() + pep.getAncho() / 2 > isla.getX() - isla.getAncho() / 2 &&
                pep.getX() - pep.getAncho() / 2 < isla.getX() + isla.getAncho() / 2) {
                
                pep.detenerSaltoEnIsla(isla.getY() - isla.getAlto() / 2);
                pepSobreIsla = true;
            }
        }

        if (!pepSobreIsla) {
            pep.salirDeIsla();
        }

        if (!pepSobreIsla && (pep.estaCayendo() || pep.getY() < 500)) {
            pep.caer();
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

            if (disparo.colisionEntorno(entorno)) {
                disparo = null;
            } else {
                // Verificar colisión con gnomos
                for (int i = 0; i < gnomos.length; i++) {
                    if (disparo == null) break;  // Salir si disparo es null
                    
                    if (gnomos[i] != null &&
                        gnomos[i].getX() + gnomos[i].getAncho() / 2 > disparo.getX() - disparo.getAncho() / 2 &&
                        gnomos[i].getX() - gnomos[i].getAncho() / 2 < disparo.getX() + disparo.getAncho() / 2 &&
                        gnomos[i].getY() + gnomos[i].getAlto() / 2 > disparo.getY() - disparo.getAlto() / 2 &&
                        gnomos[i].getY() - gnomos[i].getAlto() / 2 < disparo.getY() + disparo.getAlto() / 2) {
                        
                        gnomos[i] = null; // Eliminar al gnomo
                        gnomosRescatados++; // Aumentar el puntaje
                        disparo = null; // Eliminar el disparo tras impactar
                        break; // Salir del bucle tras eliminar un gnomo
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

        
        

        for (Isla isla : islas) {
            isla.dibujar(entorno);
        }
        casa.dibujar(entorno);
        pep.dibujar(entorno);

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