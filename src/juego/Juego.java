package juego;


import java.awt.Color;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	private Entorno entorno;
	private Isla[] islas;
	private Pep pep;
	private Disparo disparo;
	
	
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		
		// Crear las islas
        islas = new Isla[15];
        for (int i = 0; i < islas.length; i++) {
            int x, y;
            
            if (i < 5) {
                x = 50 + i * 170;
                y = 500;
            } else if (i < 9) {
                x = 100 + (i - 5) * 180;
                y = 400;
            } else if (i < 12) {
                x = 150 + (i - 9) * 250;
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
     // Crear a Pep y situarlo en la isla más baja
        int posicionInicialPepX = islas[0].getX(); // Misma X que la primera isla
        int posicionInicialPepY = islas[0].getY() - 50 / 2; // Justo sobre la isla más baja
        this.pep = new Pep(posicionInicialPepX, posicionInicialPepY, 25, 25, Color.RED);
		
		this.entorno.iniciar();
	}

	
	public void tick()
	{
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
            // Verificar si Pep está tocando la isla desde abajo (para bloquear subir desde abajo)
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
        if (entorno.sePresiono('c') & disparo == null) {
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

        // Dibujar las islas
        for (Isla isla : islas) {
            isla.dibujar(entorno);
        }
        pep.dibujar(entorno);
    }
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
