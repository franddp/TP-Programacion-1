package juego;


import java.awt.Color;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Isla[] islas;
	
	
	
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

            islas[i] = new Isla(x, y, 120, 40, (i == 14) ? Color.BLUE : Color.GREEN);
        }
		
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{
		//Dibujar las islas
		for (Isla isla : islas) {
            isla.dibujar(entorno);
        }
		
	}
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
