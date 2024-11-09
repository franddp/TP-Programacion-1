package juego;

import java.awt.Color;
import javax.sound.sampled.*;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	
	private boolean juegoTerminado = false;
	private boolean juegoGanado = false;
	private boolean mostrarPantallaCarga = true;// Variable para controlar la pantalla de carga
	//private boolean mostrarPantallaVictoria = true;
	//private boolean modoDificil = false;
    private Entorno entorno;
    private Isla[] islas;
    private NaveDeCombateAerea nave;
    private Pep pep;
    private Disparo disparo;
    private Gnomo[] gnomos;
    private Casa casa;
    private Tortuga[] tortugas;
    private int gnomosEnPantalla;
    private int maxGnomos;
    private int cantidadTortugas = 3;
    private int vidas = 3;
    private int tiempo = 3600; //3600 Esto es equivalente a 60 segundos si cada tick es 1/60 de segundo
    private int gnomosRescatados = 0;
    private int enemigosEliminados = 0;
    private java.awt.Image FondoFinal;
    private java.awt.Image FondoPantallaCarga;
    private java.awt.Image Fondo;
    private java.awt.Image FondoVictoria;
    private Clip clip;  // Variable para almacenar la musica de pantalla de carga
    private Clip clipJuego; // Variable para almacenar la musica de cuando se esta jugando
    private Clip clipDerrota; // Variable para almacenar la musica de cuando se pierda
    private Clip clipVictoria; // Variable para almacenar la musica de cuando se gane
    private boolean musicaPantallaCarga = true; // Para saber si la música está sonando


    public Juego() {
        this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
        FondoFinal=Herramientas.cargarImagen("Recursos/Imagenes/FondoFinal.jpeg");
        FondoPantallaCarga=Herramientas.cargarImagen("Recursos/Imagenes/FondoPantallaCarga.jpeg");
        Fondo=Herramientas.cargarImagen("Recursos/Imagenes/Fondo.jpeg");
        FondoVictoria=Herramientas.cargarImagen("Recursos/Imagenes/FondoVictoria.gif");
     // Redimensionar la imagen de la pantalla de carga para que se ajuste a la ventana
        FondoPantallaCarga = FondoPantallaCarga.getScaledInstance(entorno.ancho(), entorno.alto(), java.awt.Image.SCALE_SMOOTH);
        FondoFinal = FondoFinal.getScaledInstance(entorno.ancho(), entorno.alto(), java.awt.Image.SCALE_SMOOTH);
        Fondo = Fondo.getScaledInstance(entorno.ancho(), entorno.alto(), java.awt.Image.SCALE_SMOOTH);
        iniciarMusicaCarga("src/Recursos/Sonidos/MusicaPantallaCarga.wav");
        iniciarMusicaJugando("src/Recursos/Sonidos/Musica Jugando.wav");
        iniciarMusicaDerrota("src/Recursos/Sonidos/Musica Derrota.wav");
        iniciarMusicaVictoria("src/Recursos/Sonidos/Musica Victoria.wav");
    }

    private void iniciarMusicaCarga(String rutaArchivo) {
        try {
            // Cargar el archivo de audio
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new java.io.File(rutaArchivo));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);  // Reproducir en bucle

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar o reproducir la música.");
        }
        
    }
        private void iniciarMusicaJugando(String rutaArchivo) {
            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(new java.io.File(rutaArchivo));
                clipJuego = AudioSystem.getClip();
                clipJuego.open(audioStream);
                clipJuego.loop(Clip.LOOP_CONTINUOUSLY);  // Reproducir en bucle
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al cargar o reproducir la música del juego.");
            }
        } 
            private void iniciarMusicaDerrota(String rutaArchivo) {
                try {
                    // Cargar el archivo de audio
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(new java.io.File(rutaArchivo));
                    clipDerrota = AudioSystem.getClip();
                    clipDerrota.open(audioStream);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error al cargar o reproducir la música.");
                }
            }
                private void iniciarMusicaVictoria(String rutaArchivo) {
                    try {
                        // Cargar el archivo de audio
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new java.io.File(rutaArchivo));
                        clipVictoria = AudioSystem.getClip();
                        clipVictoria.open(audioStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Error al cargar o reproducir la música.");
                    }
            
        maxGnomos = 4;
        gnomosEnPantalla = 0;
        rellenarIslas();

        int xCasa = 400;
        int yCasa = 65;
        this.casa = new Casa(xCasa, yCasa, 60, 30, Color.RED);

        int posicionInicialPepX = islas[0].getX();
        int posicionInicialPepY = islas[0].getY() - 50 / 2;
        this.pep = new Pep(posicionInicialPepX, posicionInicialPepY, 25, 25, Color.RED);
        
        int posicionInicialNaveX = islas[0].getX();
        int posicionInicialNaveY = islas[0].getY() + 60;
        this.nave = new NaveDeCombateAerea(posicionInicialNaveX, posicionInicialNaveY, 100, 25, Color.ORANGE);

        gnomos = new Gnomo[maxGnomos];
        this.tortugas = new Tortuga[cantidadTortugas];
        for (int i = 0; i < cantidadTortugas; i++) {
            int posicionInicialY = 200;
            tortugas[i] = new Tortuga(0, posicionInicialY);
        }

        this.entorno.iniciar();
    }

    public void tick() {
    	
    	
//    	if (gnomosRescatados >10) {
//    		mostrarPantallaVictoria();
//    		new Juego();
//    		this.vidas=1;
//    		
//    		
//    	}
    	
    	
    	if (mostrarPantallaCarga) {
    		clipJuego.stop();
            mostrarPantallaCarga(); // Muestra la pantalla de carga
            return;
        }
    	entorno.dibujarImagen(Fondo, entorno.ancho() / 2, entorno.alto() / 2, 0);
    	
    	

    	if (!juegoTerminado && !juegoGanado) {
    		
    	int xVidas = entorno.ancho() - 800;
        int yVidas = 17;
        int xTiempo = entorno.ancho() - 800;
        int yTiempo = 57;
        int xGnomosRescatados = entorno.ancho() - 800;
        int yGnomosRescatados = 77;
        int xEnemigosDerrotados = entorno.ancho() - 800;
        int yEnemigosDerrotados = 37;

        // Color de borde
        Color colorBorde = Color.BLACK;

        // Dibujar el texto del borde desplazado (para el título)
        entorno.cambiarFont("Arial", 24, colorBorde);
        entorno.escribirTexto("Vidas: " + vidas, xVidas - 2, yVidas - 2);
        entorno.escribirTexto("Vidas: " + vidas, xVidas + 2, yVidas - 2);
        entorno.escribirTexto("Vidas: " + vidas, xVidas - 2, yVidas + 2);
        entorno.escribirTexto("Vidas: " + vidas, xVidas + 2, yVidas + 2);
        
        entorno.escribirTexto("Tiempo: " + (tiempo / 60), xTiempo - 2, yTiempo - 2);
        entorno.escribirTexto("Tiempo: " + (tiempo / 60), xTiempo + 2, yTiempo - 2);
        entorno.escribirTexto("Tiempo: " + (tiempo / 60), xTiempo - 2, yTiempo + 2);
        entorno.escribirTexto("Tiempo: " + (tiempo / 60), xTiempo + 2, yTiempo + 2);
        
        // Dibujar el texto del borde desplazado (para 'Salir')
        entorno.escribirTexto("EnemigosEliminados: " + enemigosEliminados, xEnemigosDerrotados - 2, yEnemigosDerrotados - 2);
        entorno.escribirTexto("EnemigosEliminados: " + enemigosEliminados, xEnemigosDerrotados + 2, yEnemigosDerrotados - 2);
        entorno.escribirTexto("EnemigosEliminados: " + enemigosEliminados, xEnemigosDerrotados - 2, yEnemigosDerrotados + 2);
        entorno.escribirTexto("EnemigosEliminados: " + enemigosEliminados, xEnemigosDerrotados + 2, yEnemigosDerrotados + 2);
    	
        // Dibujar el texto del borde desplazado (para 'Jugar')
        entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, xGnomosRescatados - 2, yGnomosRescatados - 2);
        entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, xGnomosRescatados + 2, yGnomosRescatados - 2);
        entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, xGnomosRescatados - 2, yGnomosRescatados + 2);
        entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, xGnomosRescatados + 2, yGnomosRescatados + 2);
    		entorno.cambiarFont("Arial", 24, Color.ORANGE);
            entorno.escribirTexto("Vidas: " + vidas, xVidas, yVidas);
            entorno.escribirTexto("Tiempo: " + (tiempo / 60), xTiempo, yTiempo); // Convertir ticks a segundos
            entorno.escribirTexto("EnemigosEliminados: " + enemigosEliminados , xEnemigosDerrotados, yEnemigosDerrotados);
            entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, xGnomosRescatados, yGnomosRescatados);
            
            if (musicaPantallaCarga && clip != null && clip.isRunning()) {
                clip.stop();  // Detener la música de la pantalla de carga
                clipJuego.start();
                musicaPantallaCarga = false;  // Marcar que la música de la pantalla de carga ya se detuvo
                if (musicaPantallaCarga = false) {
                	iniciarMusicaJugando("src/Recursos/Sonidos/MusicaJugando.wav"); // Iniciar la música de juego
                	
                }
                
            }

        tiempo--; //por cada tick baja el tiempo
        if (!pep.estaSaltando() && !pep.estaCayendo()) {
        	if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
                pep.moverDerecha(800);
            }
            if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
                pep.moverIzquierda();
            }
            if (entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
                pep.saltar();
            }
            if (entorno.estaPresionado(entorno.BOTON_DERECHO)) {
                nave.moverDerecha(800);
            }
            if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO)) {
                nave.moverIzquierda();
            }
        }
        
        pep.caer();

        boolean pepSobreIsla = false;

        for (Isla isla : islas) {
            if (pep.estaSaltando() &&
                pep.getY() - pep.getAlto() / 2 <= isla.getY() + isla.getAlto() / 2 &&
                pep.getY() > isla.getY() &&
                pep.getX() + pep.getAncho() / 2 > isla.getX() - isla.getAncho() / 2 &&
                pep.getX() - pep.getAncho() / 2 < isla.getX() + isla.getAncho() / 2) {
                
                
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
            	int posicionInicialPepX = islas[0].getX();
                int posicionInicialPepY = islas[0].getY() - 50 / 2;
            	pep = new Pep(posicionInicialPepX, posicionInicialPepY, 25, 25, Color.RED);
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
            
            //Colision entre nave y un gnomo
        

        for (int i = 0; i < gnomos.length; i++) {
            if (gnomos[i] != null) {
                if (nave.getY() > 300 &&
                    nave.getX() + nave.getAncho() / 2 > gnomos[i].getX() - gnomos[i].getAncho() / 2 &&
                    nave.getX() - nave.getAncho() / 2 < gnomos[i].getX() + gnomos[i].getAncho() / 2 &&
                    nave.getY() + nave.getAlto() / 2 > gnomos[i].getY() - gnomos[i].getAlto() / 2 &&
                    nave.getY() - nave.getAlto() / 2 < gnomos[i].getY() + gnomos[i].getAlto() / 2) {
                    
                    gnomos[i] = null;
                    gnomosEnPantalla--;
                    gnomosRescatados++;
                }
            } 
        }
        
        //Colision entre nave y pep
        	
            if (pep.estaSaltando() &&
                pep.getY() - pep.getAlto() / 2 <= nave.getY() + nave.getAlto() / 2 &&
                pep.getY() > nave.getY() &&
                pep.getX() + pep.getAncho() / 2 > nave.getX() - nave.getAncho() / 2 &&
                pep.getX() - pep.getAncho() / 2 < nave.getX() + nave.getAncho() / 2) {
                
                pep.caerInmediatamente();
            }

            if (pep.estaCayendo() &&
                pep.getY() + pep.getAlto() / 2 >= nave.getY() - nave.getAlto() / 2 &&
                pep.getY() < nave.getY() &&
                pep.getX() + pep.getAncho() / 2 > nave.getX() - nave.getAncho() / 2 &&
                pep.getX() - pep.getAncho() / 2 < nave.getX() + nave.getAncho() / 2) {
                
                pep.detenerSaltoEnIsla(nave.getY() - nave.getAlto() / 2);
                
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
        nave.dibujar(entorno);

        for (Gnomo gnomo : gnomos) {
            if (gnomo != null) {
                gnomo.dibujar(entorno);
            }
        }
        
        if (vidas == 0 || tiempo <= 0) {
            juegoTerminado = true;
            //mostrarPantallaVictoria();
        }

        if (juegoTerminado) {
        	mostrarPantallaFinal(); // Mostrar pantalla final
            detenerMusica();  // Detener la música cuando el juego termine
            clipJuego.stop();
            clipDerrota.start();
            return;
        }
        
        if (gnomosRescatados==10) {
        	juegoGanado = true;
        	mostrarPantallaVictoria(); // Mostrar pantalla final
            detenerMusica();  // Detener la música cuando el juego termine
            clipJuego.stop();
            clipVictoria.start();
            return;
        }
    }//aca termina el tick

    private void mostrarPantallaCarga() {
        entorno.cambiarFont("Arial", 24, Color.ORANGE);
        
        // Dibujar la imagen de fondo (pantalla de carga)
        entorno.dibujarImagen(FondoPantallaCarga, entorno.ancho()/2, entorno.alto()/2, 0);
        
        // Definir las posiciones de los textos
        int xTitulo = entorno.ancho() - 580;
        int yTitulo = 90;
        int xJugar = entorno.ancho() - 650;
        int yJugar = 550;
        int xSalir = entorno.ancho() - 350;
        int ySalir = 550;

        // Color de borde
        Color colorBorde = Color.BLACK;

        // Dibujar el texto del borde desplazado (para el título)
        entorno.cambiarFont("Arial", 30, colorBorde);
        entorno.escribirTexto("INVASION DE LOS ROBOTS", xTitulo - 2, yTitulo - 2);
        entorno.escribirTexto("INVASION DE LOS ROBOTS", xTitulo + 2, yTitulo - 2);
        entorno.escribirTexto("INVASION DE LOS ROBOTS", xTitulo - 2, yTitulo + 2);
        entorno.escribirTexto("INVASION DE LOS ROBOTS", xTitulo + 2, yTitulo + 2);

        // Dibujar el texto del borde desplazado (para 'Jugar')
        entorno.cambiarFont("Arial", 24, colorBorde);
        entorno.escribirTexto("Presiona 'J' para Jugar", xJugar - 2, yJugar - 2);
        entorno.escribirTexto("Presiona 'J' para Jugar", xJugar + 2, yJugar - 2);
        entorno.escribirTexto("Presiona 'J' para Jugar", xJugar - 2, yJugar + 2);
        entorno.escribirTexto("Presiona 'J' para Jugar", xJugar + 2, yJugar + 2);

        // Dibujar el texto del borde desplazado (para 'Salir')
        entorno.escribirTexto("Presiona 'E' para Salir", xSalir - 2, ySalir - 2);
        entorno.escribirTexto("Presiona 'E' para Salir", xSalir + 2, ySalir - 2);
        entorno.escribirTexto("Presiona 'E' para Salir", xSalir - 2, ySalir + 2);
        entorno.escribirTexto("Presiona 'E' para Salir", xSalir + 2, ySalir + 2);

        // Ahora dibujamos el texto principal (en color naranja) encima del borde
        entorno.cambiarFont("Arial", 30, Color.ORANGE);
        entorno.escribirTexto("INVASION DE LOS ROBOTS", xTitulo, yTitulo);
        entorno.cambiarFont("Arial", 24, Color.ORANGE);
        entorno.escribirTexto("Presiona 'J' para Jugar", xJugar, yJugar);
        entorno.escribirTexto("Presiona 'E' para Salir", xSalir, ySalir);

        // Comprobar entradas
        if (entorno.sePresiono('j')) {
            mostrarPantallaCarga = false; // Iniciar el juego
        } else if (entorno.sePresiono('e')) {
            System.exit(0); // Salir del juego
        }
    }


        private void mostrarPantallaFinal() {
        entorno.cambiarFont("Arial", 24, Color.ORANGE);
        
        // Dibujar la imagen de fondo (pantalla de carga)
        entorno.dibujarImagen(FondoFinal, entorno.ancho()/2, entorno.alto()/2, 0);        
        // Definir las posiciones de los textos
        int xTitulo = entorno.ancho() - 580;
        int yTitulo = 90;
        int xSubTitulo = entorno.ancho() - 580;
        int ySubTitulo = 150;
        int xGnomosRescatados = entorno.ancho() - 750;
        int yGnomosRescatados = 400;
        int xEnemigosDerrotados = entorno.ancho() - 750;
        int yEnemigosDerrotados = 450;
        int xJugar = entorno.ancho() - 750;
        int yJugar = 500;
        int xSalir = entorno.ancho() - 750;
        int ySalir = 550;

        // Color de borde
        Color colorBorde = Color.BLACK;

        // Dibujar el texto del borde desplazado (para el título)
        entorno.cambiarFont("Arial", 30, colorBorde);
        entorno.escribirTexto("¡DERROTA!", xTitulo - 2, yTitulo - 2);
        entorno.escribirTexto("¡DERROTA!", xTitulo + 2, yTitulo - 2);
        entorno.escribirTexto("¡DERROTA!", xTitulo - 2, yTitulo + 2);
        entorno.escribirTexto("¡DERROTA!", xTitulo + 2, yTitulo + 2);
        
        entorno.escribirTexto("GANARON LOS ROBOTS", xSubTitulo - 2, ySubTitulo - 2);
        entorno.escribirTexto("GANARON LOS ROBOTS", xSubTitulo + 2, ySubTitulo - 2);
        entorno.escribirTexto("GANARON LOS ROBOTS", xSubTitulo - 2, ySubTitulo + 2);
        entorno.escribirTexto("GANARON LOS ROBOTS", xSubTitulo + 2, ySubTitulo + 2);

        // Dibujar el texto del borde desplazado (para 'Jugar')
        entorno.cambiarFont("Arial", 24, colorBorde);
        entorno.escribirTexto("Presiona 'J' para Jugar otra vez", xJugar - 2, yJugar - 2);
        entorno.escribirTexto("Presiona 'J' para Jugar otra vez", xJugar + 2, yJugar - 2);
        entorno.escribirTexto("Presiona 'J' para Jugar otra vez", xJugar - 2, yJugar + 2);
        entorno.escribirTexto("Presiona 'J' para Jugar otra vez", xJugar + 2, yJugar + 2);

        // Dibujar el texto del borde desplazado (para 'Salir')
        entorno.escribirTexto("Presiona 'E' para Salir", xSalir - 2, ySalir - 2);
        entorno.escribirTexto("Presiona 'E' para Salir", xSalir + 2, ySalir - 2);
        entorno.escribirTexto("Presiona 'E' para Salir", xSalir - 2, ySalir + 2);
        entorno.escribirTexto("Presiona 'E' para Salir", xSalir + 2, ySalir + 2);
        
        entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, xGnomosRescatados - 2, yGnomosRescatados - 2);
        entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, xGnomosRescatados + 2, yGnomosRescatados - 2);
        entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, xGnomosRescatados - 2, yGnomosRescatados + 2);
        entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, xGnomosRescatados + 2, yGnomosRescatados + 2);
        
        entorno.escribirTexto("Enemigos Eliminados: " + enemigosEliminados, xEnemigosDerrotados - 2, yEnemigosDerrotados - 2);
        entorno.escribirTexto("Enemigos Eliminados: " + enemigosEliminados, xEnemigosDerrotados + 2, yEnemigosDerrotados - 2);
        entorno.escribirTexto("Enemigos Eliminados: " + enemigosEliminados, xEnemigosDerrotados - 2, yEnemigosDerrotados + 2);
        entorno.escribirTexto("Enemigos Eliminados: " + enemigosEliminados, xEnemigosDerrotados + 2, yEnemigosDerrotados + 2);

        // Ahora dibujamos el texto principal (en color naranja) encima del borde
        entorno.cambiarFont("Arial", 30, Color.ORANGE);
        entorno.escribirTexto("¡DERROTA!", xTitulo, yTitulo);
        entorno.escribirTexto("GANARON LOS ROBOTS", xSubTitulo, ySubTitulo);
        entorno.cambiarFont("Arial", 24, Color.ORANGE); 
        entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, xGnomosRescatados, yGnomosRescatados);
        entorno.escribirTexto("Enemigos Eliminados: " + enemigosEliminados, xEnemigosDerrotados, yEnemigosDerrotados);
        entorno.escribirTexto("Presiona 'J' para Jugar otra vez", xJugar, yJugar);
        entorno.escribirTexto("Presiona 'E' para Salir", xSalir, ySalir);
        
            // Comprobar entradas
            if (entorno.sePresiono('j') ) {
                new Juego(); // Iniciar el juego
                detenerMusica();
            	} if (entorno.sePresiono('e')) {
                System.exit(0); // Salir del juego
            		}
        }
        
        private void mostrarPantallaVictoria() {
            entorno.cambiarFont("Arial", 24, Color.ORANGE);
            
            // Dibujar la imagen de fondo (pantalla de carga)
            entorno.dibujarImagen(FondoVictoria, entorno.ancho()/2, entorno.alto()/2, 0);        
            // Definir las posiciones de los textos
            int xTitulo = entorno.ancho() - 580;
            int yTitulo = 90;
            int xSubTitulo = entorno.ancho() - 580;
            int ySubTitulo = 150;
            int xGnomosRescatados = entorno.ancho() - 550;
            int yGnomosRescatados = 400;
            int xEnemigosDerrotados = entorno.ancho() - 550;
            int yEnemigosDerrotados = 450;
            int xJugar = entorno.ancho() - 550;
            int yJugar = 500;
            int xSalir = entorno.ancho() - 550;
            int ySalir = 550;

            // Color de borde
            Color colorBorde = Color.BLACK;

            // Dibujar el texto del borde desplazado (para el título)
            entorno.cambiarFont("Arial", 30, colorBorde);
            entorno.escribirTexto("¡VICTORIA!", xTitulo - 2, yTitulo - 2);
            entorno.escribirTexto("¡VICTORIA!", xTitulo + 2, yTitulo - 2);
            entorno.escribirTexto("¡VICTORIA!", xTitulo - 2, yTitulo + 2);
            entorno.escribirTexto("¡VICTORIA!", xTitulo + 2, yTitulo + 2);
            
            entorno.escribirTexto("FELICIDADES", xSubTitulo - 2, ySubTitulo - 2);
            entorno.escribirTexto("FELICIDADES", xSubTitulo + 2, ySubTitulo - 2);
            entorno.escribirTexto("FELICIDADES", xSubTitulo - 2, ySubTitulo + 2);
            entorno.escribirTexto("FELICIDADES", xSubTitulo + 2, ySubTitulo + 2);

            // Dibujar el texto del borde desplazado (para 'Jugar')
            entorno.cambiarFont("Arial", 24, colorBorde);
            entorno.escribirTexto("Presiona 'J' para Jugar otra vez", xJugar - 2, yJugar - 2);
            entorno.escribirTexto("Presiona 'J' para Jugar otra vez", xJugar + 2, yJugar - 2);
            entorno.escribirTexto("Presiona 'J' para Jugar otra vez", xJugar - 2, yJugar + 2);
            entorno.escribirTexto("Presiona 'J' para Jugar otra vez", xJugar + 2, yJugar + 2);

            // Dibujar el texto del borde desplazado (para 'Salir')
            entorno.escribirTexto("Presiona 'E' para Salir", xSalir - 2, ySalir - 2);
            entorno.escribirTexto("Presiona 'E' para Salir", xSalir + 2, ySalir - 2);
            entorno.escribirTexto("Presiona 'E' para Salir", xSalir - 2, ySalir + 2);
            entorno.escribirTexto("Presiona 'E' para Salir", xSalir + 2, ySalir + 2);
            
            entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, xGnomosRescatados - 2, yGnomosRescatados - 2);
            entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, xGnomosRescatados + 2, yGnomosRescatados - 2);
            entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, xGnomosRescatados - 2, yGnomosRescatados + 2);
            entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, xGnomosRescatados + 2, yGnomosRescatados + 2);
            
            entorno.escribirTexto("Enemigos Eliminados: " + enemigosEliminados, xEnemigosDerrotados - 2, yEnemigosDerrotados - 2);
            entorno.escribirTexto("Enemigos Eliminados: " + enemigosEliminados, xEnemigosDerrotados + 2, yEnemigosDerrotados - 2);
            entorno.escribirTexto("Enemigos Eliminados: " + enemigosEliminados, xEnemigosDerrotados - 2, yEnemigosDerrotados + 2);
            entorno.escribirTexto("Enemigos Eliminados: " + enemigosEliminados, xEnemigosDerrotados + 2, yEnemigosDerrotados + 2);

            // Ahora dibujamos el texto principal (en color naranja) encima del borde
            entorno.cambiarFont("Arial", 30, Color.ORANGE);
            entorno.escribirTexto("¡VICTORIA!", xTitulo, yTitulo);
            entorno.escribirTexto("FELICIDADES", xSubTitulo, ySubTitulo);
            entorno.cambiarFont("Arial", 24, Color.ORANGE); 
            entorno.escribirTexto("Gnomos Rescatados: " + gnomosRescatados, xGnomosRescatados, yGnomosRescatados);
            entorno.escribirTexto("Enemigos Eliminados: " + enemigosEliminados, xEnemigosDerrotados, yEnemigosDerrotados);
            entorno.escribirTexto("Presiona 'J' para Jugar otra vez", xJugar, yJugar);
            entorno.escribirTexto("Presiona 'E' para Salir", xSalir, ySalir);
            
                // Comprobar entradas
                if (entorno.sePresiono('j') ) {
                    new Juego(); // Iniciar el juego
                    detenerMusica();
                	} if (entorno.sePresiono('e')) {
                    System.exit(0); // Salir del juego
                		}
            }
        
     // Llamar a este método para detener la música cuando el juego comienza o la pantalla de carga termine
        private void detenerMusica() {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
            if (clipJuego != null && clipJuego.isRunning()) {
                clipJuego.stop();  // Detener la música de juego
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