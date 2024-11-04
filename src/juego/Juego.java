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
    private Casa casa;
    private int gnomosEnPantalla;
    private int maxGnomos;
    private Tortuga[] tortugas;
    private int cantidadTortugas = 3;


    public Juego() {
        this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);


        maxGnomos = 4;
        gnomosEnPantalla = 0;

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

        if (entorno.sePresiono('c') && disparo == null) {
            disparo = pep.disparar();
        }
        
        if (disparo != null) {
            disparo.dibujar(entorno);
            disparo.mover();
            if (disparo.colisionEntorno(entorno)) {
                disparo = null;
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
                }
            }
        }

        if (gnomosEnPantalla < maxGnomos) {
            crearGnomoDesdeCasa();
        }

        for (Tortuga tortuga : tortugas) {
            tortuga.actualizar(islas, casa);
            tortuga.dibujar(entorno);
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
}
