package controller;

//Realizado por: Jaime Alemany 

import crud.CrudTablero;
import vista.ImprimirTablero;

public class ControllerTablero {
	private CrudTablero tablero;
	private ImprimirTablero it;
	private int posicionesGanadoras [];
	
	public ControllerTablero(CrudTablero tablero, ImprimirTablero it) {
		super();
		this.tablero = tablero;
		this.it=it;
		this.posicionesGanadoras = new int[8]; // Contiene 4 fichas [Fila , Columna]*4
	}

	
	public boolean comprobarVictoria(int jugador) { //Realizado por: Jaime Alemany
		
		int filas = tablero.getFilas();
		int columnas = tablero.getColumnas();
		// Comprueba las filas horizontales
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas - 3; j++) {
				if (tablero.comprobarEstado(i, j) == jugador && tablero.comprobarEstado(i, j + 1) == jugador
						&& tablero.comprobarEstado(i, j + 2) == jugador
						&& tablero.comprobarEstado(i, j + 3) == jugador) {
					// Guarda las posiciones en la tabla de las fichas ganadoras, para así hacer una pequeña animación de victoria
					int contador=0;
					for(int k=0;k<=6;k+=2) {
						posicionesGanadoras[k]=i; // La fila siempre es la misma
						posicionesGanadoras[k+1]=j+contador; // La columna cambia de 1 en 1
						contador++;
					}
					return true;
				}
			}
		}
		// Comprueba las filas verticales
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas - 3; j++) {
				if (tablero.comprobarEstado(j, i) == jugador && tablero.comprobarEstado(j + 1, i) == jugador
						&& tablero.comprobarEstado(j + 2, i) == jugador
						&& tablero.comprobarEstado(j + 3, i) == jugador) {
					int contador=0;
					
					for(int k=0;k<=6;k+=2) { 
						posicionesGanadoras[k]=j+contador; // La fila cambia de 1 en 1
						posicionesGanadoras[k+1]=i; // La columna siempre es la misma
						contador++;
					}
					return true;
				}
			}
		}
		// Comprueba las filas diagonales izq - der (\)
		for (int i = 0; i < filas - 3; i++) {
			for (int j = 0; j < columnas - 3; j++) {
				if (tablero.comprobarEstado(i, j) == jugador && tablero.comprobarEstado(i + 1, j + 1) == jugador
						&& tablero.comprobarEstado(i + 2, j + 2) == jugador
						&& tablero.comprobarEstado(i + 3, j + 3) == jugador) {
					int contador=0;
					
					for(int k=0;k<=6;k+=2) {
						posicionesGanadoras[k]=i+contador; // La fila cambia 
						posicionesGanadoras[k+1]=j+contador; // La columna cambia también
						contador++;
					}
					return true;
				}
			}
		}
		// Comprueba las filas diagonales der - izq (/)
		for (int i = 3; i < filas; i++) {
			for (int j = 0; j < columnas - 3; j++) {
				if (tablero.comprobarEstado(i, j) == jugador && tablero.comprobarEstado(i - 1, j + 1) == jugador
						&& tablero.comprobarEstado(i - 2, j + 2) == jugador
						&& tablero.comprobarEstado(i - 3, j + 3) == jugador) {
					int contador=0;
					for(int k=0;k<=6;k+=2) {
						posicionesGanadoras[k]=i-contador; // La fila cambia en negativo
						posicionesGanadoras[k+1]=j+contador; // La columna cambia también
						contador++;
					}
					return true;
				}
			}
		}

		return false;
	}
	
	public void animacionGanadoras () throws InterruptedException {  //Realizado por: Jaime Alemany
		int ficha=tablero.comprobarEstado(posicionesGanadoras[0], posicionesGanadoras[1]);
		int contador=0;
		while(contador<=4) {
		for (int i = 0; i < posicionesGanadoras.length-1; i+=2) {
			tablero.setTabla(posicionesGanadoras[i], posicionesGanadoras[i+1], 3);
		}
		it.dibujarTablero(tablero, 6, 7);
		Thread.sleep(600);
		for (int i = 0; i < posicionesGanadoras.length-1; i+=2) {
			tablero.setTabla(posicionesGanadoras[i], posicionesGanadoras[i+1],ficha);
			it.dibujarTablero(tablero, 6, 7);
			contador++;
			Thread.sleep(600);
			}
		}
	}
	
	public boolean comprobarEmpate() { //Realizado por: Fernando Guerrero
		int filas = tablero.getFilas();
		int columnas = tablero.getColumnas();
		// Comprueba si todas las posiciones del tablero están llenas
		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				if (tablero.comprobarEstado(i, j) == 0) { 
					return false;
				}
			}
		}
		return true;
	}
}
