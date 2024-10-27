package juego;

import java.awt.Color;

import entorno.Entorno;

public class Disparo {

	private double x;
    private double y;
    private double ancho;
    private double alto;
    private String dir;
    

    public Disparo(double x, double y, double ancho, double alto, String dir) {
            this.x = x;
            this.y = y;
            this.ancho = ancho;
            this.alto = alto;
            this.setDir(dir);
             
           
    }

	public void dibujar(Entorno e) {
    	e.dibujarRectangulo(x, y, ancho, alto, 0, Color.YELLOW);
    }
    
    public void mover() {
    	if (dir.equals("der")) {
    		this.x+=9;
    	}
    	else {
    		this.x-=9;
    	}
    }

    
    public void setDir(String dir) {
		this.dir = dir;
	}

	
	public boolean colisionEntorno(Entorno entorno) {
		if(this.x+(this.ancho/2)>=entorno.ancho() || this.x-(this.ancho/2)<=0) {
			return true;
		}
		return false;
	}
}
