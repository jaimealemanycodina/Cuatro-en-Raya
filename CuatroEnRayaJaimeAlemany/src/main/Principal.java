package main;

import controller.ControllerBot;

//Realizado por: Jaime Alemany, Fernando Guerrero y Roberto Montañés
//Todos hemos realizado cambios constantes en el código principal.

import controller.ControllerTablero;
import crud.CrudTablero;
import datos.DatosIniciales;
import model.Jugador;
import utilidades.Leer;
import vista.ImprimirMensajes;
import vista.ImprimirTablero;

public class Principal {

	public static void main(String[] args) {

		Jugador j1;
		Jugador j2;
		Jugador jaux;
		String nombre;
		int columna, turno = 0, ficha;
		
		/*Inicialización de clases del juego*/
		
		CrudTablero t = new CrudTablero(6, 7);
		ImprimirTablero it = new ImprimirTablero();
		ControllerTablero ct = new ControllerTablero(t, it); 
		DatosIniciales d = new DatosIniciales();
		ImprimirMensajes im = new ImprimirMensajes();
		
		/*Inicialización de clases del bot*/
		
		CrudTablero tAux=new CrudTablero(6,7);
		ControllerTablero ctAux=new ControllerTablero(tAux,it);
		ControllerBot ctb=new ControllerBot(0,ctAux,t,tAux);
		int dificultad;
		
		boolean jugando;
		int opcion, opcTurnos;
		
		

		im.imprimirBienvenida();
		do {
			
			opcion = Leer.datoInt();
			switch (opcion) {
				case 0:
					break;
				/*2 JUGADORES*/
				case 1:
					jugando = true;
	
					/* INICIO DE JUGADORES */
					System.out.print("Introduzca el nombre del 1er Jugador\n➽ ");
					nombre = Leer.dato();
					j1 = new Jugador(nombre, d.getFICHA_JUGADOR1());
					System.out.print("Introduzca el nombre del 2do Jugador\n➽ ");
					nombre = Leer.dato();
					j2 = new Jugador(nombre, d.getFICHA_JUGADOR2());
					System.out.println();
					t.inicializarTablero();
					turno = 0;
					/* INICIO DEL JUEGO */
					while (jugando) {
						if (turno % 2 == 0) { // Jugador 1 juega en turno par
							jaux = j1;
							ficha = 1;
						} else { // Jugador2 juega en turno impar
							jaux = j2;
							ficha = 2;
						}
						it.dibujarTablero(t, d.getFILAS_TABLERO(), d.getCOLUMNAS_TABLERO());
						System.out.println("Turno de " + jaux.getNombre() + " " + "(" + jaux.getFicha() + ")"
								+ " Seleccione la columna: ");
						columna = Leer.datoInt();
						while (!t.anadirFicha(columna, ficha)) {
							System.out.println("ERROR: Columna llena / no existente. Escoja otra columna: ");
							columna = Leer.datoInt();
						}
						// Imprimir ---> Se le pasa: Jugador, tablero
						if (ct.comprobarVictoria(ficha)) {
							jugando = false;
	
							try {
								ct.animacionGanadoras();
							} catch (InterruptedException e) {
							}
							System.out.println("GANADOR: " + jaux.getNombre());
							System.out.println("\n➽ Pulsa 0. para cerrar el programa");
							System.out.println("➽ Pulsa 1. para jugar");
						} else if (ct.comprobarEmpate()) {
							jugando = false;
							it.dibujarTablero(t, d.getFILAS_TABLERO(), d.getCOLUMNAS_TABLERO());
							im.imprimirEmpate();
							
						}
						turno++;
					}
					break;
					
				/*JUGADOR CONTRA BOT*/
				case 2:
					jugando = true;
					System.out.print("Introduzca la dificultad del bot contra el que vas a jugar (0 - 100): ");
					dificultad=Leer.datoInt();
					ctb.setDificultad(dificultad);
					ctb.inicializarPuntosCol();
					System.out.print("➽ Introduzca el nombre del Jugador: ");
					nombre = Leer.dato();
					do {
						System.out.print("\n¿Quién jugará primero?\n  1. Jugador\n  2. BOT\n➽ ");
						opcTurnos=Leer.datoInt();
						if(opcTurnos!=1&&opcTurnos!=2)
							System.out.println("ERROR: Escoja una opción válida.");
					}while (opcTurnos!=1&&opcTurnos!=2);
					if(opcTurnos==1) {
						j1 = new Jugador(nombre, d.getFICHA_JUGADOR1());
						j2 = new Jugador("BOT Marvin", d.getFICHA_JUGADOR2());
					}else {
						j2 = new Jugador(nombre, d.getFICHA_JUGADOR2());
						j1 = new Jugador("BOT Marvin", d.getFICHA_JUGADOR1());
					}
					
					
					System.out.println();
					t.inicializarTablero();
					turno = 0;
					while (jugando) {
						if (turno % 2 == 0) { // Jugador 1 juega en turno par
							jaux = j1;
							ficha = 1;
						} else { // Jugador2 juega en turno impar
							jaux = j2;
							ficha = 2;
						}
						it.dibujarTablero(t, d.getFILAS_TABLERO(), d.getCOLUMNAS_TABLERO());
						System.out.println("Turno de " + jaux.getNombre() + " " + "(" + jaux.getFicha() + ")"
								+ " Seleccione la columna: ");
						if(jaux.getNombre().equals("BOT Marvin")) {
							columna=ctb.elegirMovimiento(ficha);
						}else {
							columna = Leer.datoInt();
						}
						while (!t.anadirFicha(columna, ficha)) {
							System.out.println("ERROR: Columna llena / no existente. Escoja otra columna: ");
							if(jaux.getNombre().equals("BOT Marvin")) {
								columna=ctb.elegirMovimiento(ficha);
							}else {
								columna = Leer.datoInt();
							}
						}
						// Imprimir ---> Se le pasa: Jugador, tablero
						if (ct.comprobarVictoria(ficha)) {
							jugando = false;
	
							try {
								ct.animacionGanadoras();
							} catch (InterruptedException e) {
							}
							System.out.println("GANADOR: " + jaux.getNombre());
							System.out.println("\n➽ Pulsa 0. para cerrar el programa");
							System.out.println("➽ Pulsa 1. para jugar (2 jugadores)");
							System.out.println("➽ Pulsa 2. para jugar (Jugador contra BOT)");
						} else if (ct.comprobarEmpate()) {
							jugando = false;
							it.dibujarTablero(t, d.getFILAS_TABLERO(), d.getCOLUMNAS_TABLERO());
							im.imprimirEmpate();
							
						}
						turno++;
					}
					
					break;
	
				default:
					System.out.println("Número Inválido");
					System.out.println("\n➽ Pulsa 0. para cerrar el programa");
					System.out.println("➽ Pulsa 1. para jugar");
					break;
			}
			
		} while (opcion != 0);
		im.imprimirGraciasPorJugar();
	}
}
