package controller;

import java.util.Random;

import crud.CrudTablero;

public class ControllerBot {
	
	int dificultad; // 0 - 100
	int [] puntosCol; // Tabla de 7 columnas que evalúa la fuerza de cada movimiento y le asigna un valor
	int [] puntosRival; // Tabla de la fuerza del rival
	ControllerTablero ct;
	CrudTablero c;
	CrudTablero cAux;
	public ControllerBot(int dificultad, ControllerTablero ct, CrudTablero c, CrudTablero cAux) {
		super();
		this.dificultad = dificultad;
		this.ct = ct;
		this.c = c;
		this.cAux=cAux;
		puntosCol=new int[7];
		puntosRival=new int[7];
	}
	public int getDificultad() {
		return dificultad;
	}
	public void setDificultad(int dificultad) {
		this.dificultad = dificultad;
	}
	public int[] getPuntosCol() {
		return puntosCol;
	}
	public void setPuntosCol(int[] puntosCol) {
		this.puntosCol = puntosCol;
	}
	public ControllerTablero getCt() {
		return ct;
	}
	public void setCt(ControllerTablero ct) {
		this.ct = ct;
	}
	
	
	public void inicializarPuntosCol() { // Inicializa el puntuaje de columnas a 0 en las 2 tabllas de puntos
		for(int i=0;i<puntosCol.length;i++) {
			puntosCol[i]=0;
		}
		for(int i=0;i<puntosRival.length;i++) {
			puntosRival[i]=0;
		}

	}
	
	public void copiarTablaAux() {
		
		cAux.setTabla(c.getTabla().clone());;
	}
	
	/* Método evaluarColumna
	 * Uso: Evalúa la fuerza de poner una ficha en la columna elegida
	 * Cómo funciona: 
	 * 		Primero mira si hay 3 fichas del mismo tipo colindantes (Si es el caso, asigna 10 y sale)
	 * 		Segundo mira la horizontal (mira si hay 0, 1 o 2 fichas colindantes seguido de espacios)
	 * 		Tercero mira la vertical (si abajo del espacio hay 1 o 2 fichas colindantes seguidos de espacios)
	 * 		Cuarto mira la diagonal (si en \ o en / hay 0, 1 o 2 fichas colindantes seguidos de espacios)
	 * 
	 * Qué elementos usa:
	 * 		Usa una copia auxiliar del tablero (tableroAux) a la que se le añade la ficha en esa columna para comprobar si
	 * 		es victoria o no. 
	 * 		- 10 si es victoria (FUNCIONA)
	 * 		- -1 si la columna está ocupada (FUNCIONA)
	 * 
	 * 
	 * */
	
	public int evaluarColumna(int columna, int jugador) {
		int resul=0, fila;
		int rival;
		// Guarda la ficha rival
		if(jugador==1)
			rival=2;
		else {
			rival=1;
		}
		copiarTablaAux();
		/*COMPRUEBA SI SE PUEDE GANAR CON 1 TURNO*/
		fila=cAux.encontrarFilaLibre(columna);
		if(fila==-1) {
			resul= -2;
			
		}else {
			cAux.setTabla(fila, columna, jugador);
			if(ct.comprobarVictoria(jugador)) {
			resul=10;
			cAux.setTabla(fila, columna, 0);
		
			}else {
				// Comprueba si el rival va a hacer 4 en raya
				cAux.setTabla(fila, columna, rival);
				if(ct.comprobarVictoria(rival)) {
					resul=9;
					cAux.setTabla(fila, columna, 0);
				/*Comprueba el resto de casos*/
				}else {
					 // Cambia la tabla una vez más al estado inicial antes de que jugara el bot
					cAux.setTabla(fila, columna, 0);
					resul=comprobarHoriz(fila,jugador,columna);
					if(comprobarVert(jugador,columna)>resul)
						resul=comprobarVert(jugador,columna);
					// Comprueba si poner una ficha permite al oponente hacer 4 en raya antes no posible
					if(comprobarDiag(jugador,columna)&&resul!=10)
						resul=-1;
				
				}
			}
		}
		
		return resul;
	}
	
	
	/* comprobarHoriz
	 * Uso: Comprueba la fuerza horizontal de poner una ficha, y devuelve una puntuación en int.
	 * Cómo funciona:
	 * 		1. Encuentra la fila siguiente donde caerá la ficha
	 * 		2. Comprueba los espacios y las fichas a la izquierda 
	 * 		tal que la siguientes fichas en la columna izquierda sea en la misma fila
	 * 		(espacio=1pt, fichaIgual=2pts, fichaDistinta=stop)
	 * 		3. Comprueba los espacios y las fichas a la derecha	
	 * 		(espacio=1pt, fichaIgual=2pts, fichaDistinta=stop)
	 * 		4. Si el total de fichas y espacios consecutivos es 4 o más, hace la suma de esa puntuación y la devuelve.
	 * 		Si no, devuelve 0;
	 * 
	 * */
	public int comprobarHoriz(int fila, int jugador, int columna) {
		int puntos=0, ptosIzq=0, ptosDer=0, contador=0, fichaAux;
		boolean bloq=false, colindante=false;
		//Comprueba a la izquierda
		for(int i=columna-1;i>-1&&!bloq;i--) {
			//Es espacio jugable colindante (espacio jugable=en el siguiente turno se puede poner una ficha)?
			if(cAux.encontrarFilaLibre(i)==fila||cAux.comprobarEstado(fila, i)==jugador) {
				fichaAux=cAux.comprobarEstado(fila, i);
				switch(fichaAux){
					case 0:
						ptosIzq++;
						contador++;
						break;
					case 1:
						if(jugador==1) {
							ptosIzq+=2;
							contador++;
							if(i==columna-1)
								colindante=true;
						}else {
							bloq=true;
						}
						break;
					case 2:
						if(jugador==2) {
							ptosIzq+=2;
							contador++;
							if(i==columna-1)
								colindante=true;
						}else {
							bloq=true;
						}
						break;
				}
			}else {
				bloq=true;
			}
		}
		// Comprueba a la derecha
		bloq=false;
		for(int i=columna+1;i<7&&!bloq;i++) {
			//¿Es espacio jugable colindante (espacio jugable=en el siguiente turno se puede poner una ficha)?
			if(cAux.encontrarFilaLibre(i)==fila) {
				fichaAux=cAux.comprobarEstado(fila, i);
				switch(fichaAux){
					case 0:
						ptosDer++;
						contador++;
						break;
					case 1:
						if(jugador==1) {
							ptosDer+=2;
							contador++;
							if(i==columna-1)
								colindante=true;
						}else {
							bloq=true;
						}
						break;
					case 2:
						if(jugador==2) {
							ptosDer+=2;
							contador++;
							if(i==columna-1)
								colindante=true;
						}else {
							bloq=true;
						}
						break;
				}
			}else {
				bloq=true;
			}
		}
		// Cuenta si el total de espacios es suficiente para hacer un 4 en raya. Si no, devuelve 0.
		if (contador>=3) {
			puntos=ptosIzq+ptosDer;
			if(colindante)
				puntos++;
		}else
			puntos=0;
		return puntos;
	}
	
	/* comprobarVert
	 * Uso: Comprueba si es posible hacer 4 en raya en vertical, y asigna una puntuación según la fuerza
	 * Cómo funciona:
	 * 	1. Cuenta la cantidad de fichas seguidas que hay (0,1 o 2)
	 * 	2. Cuenta los espacios en blanco hacia arriba
	 * 	3. Si las fichas seguidas y los espacios en blanco son 4 o más, asigna puntuación.
	 * 	4. Si no lo son, es un punto muerto y la puntuación es 0
	 */
	
	public int comprobarVert(int jugador, int columna) {
		int puntos=0, filaInicial, contador=0;
		boolean bloq=false;
		filaInicial=cAux.encontrarFilaLibre(columna);
		
		// Comprueba cuántas fichas hacia abajo son iguales
		for(int i=filaInicial+1;i<6&&!bloq;i++) {
			if(cAux.comprobarEstado(i, columna)==jugador) {
				contador++;
				puntos+=2;
			}else
				bloq=true;
		}
		bloq=false;
		for(int i=filaInicial-1;i>=0&&!bloq;i--) {
			contador++;
		}
		if(contador>=3) {
			puntos+=2;
		}else
			puntos=0;
		return puntos;
	}
	
	// Devuelve true si poner una ficha permite al rival ganar, devuelve false si poner una ficha no perderá la partida. Esto
	// suele ocurrir con las diagonales, de ahí su nombre
	
	// WEEWOOWEEWOOO PROBLEMA SE PETA !!!! CUANDO NO QUEDAN MÁS COLUMNAS VACÍA
	public boolean comprobarDiag(int jugador, int columna) {
		boolean malo=false;
		int rival, fila;
		// Guarda la ficha rival
		if(jugador==1)
			rival=2;
		else {
			rival=1;
		}
		// Selecciona la fila de inmediatamente arriba. Si no hay más, sale y devuelve false
		fila=cAux.encontrarFilaLibre(columna);
		if(fila!=-1&&fila-1>=0) {
			cAux.setTabla(fila-1, columna, rival);
			// Comprueba si poner una ficha hará que pierdas la partida
			if(ct.comprobarVictoria(rival))
				malo=true;
			// Comprueba si poner una ficha permitirá al rival cortar un 4 en raya posible
			cAux.setTabla(fila-1, columna, jugador);
			if(ct.comprobarVictoria(jugador))
				malo=true;
			cAux.setTabla(fila-1, columna, 0);
		}
		
		return malo;
	}
	
	/*TOMA DE DECISIONES*/
	public void asignarPuntos(int jugador) {
		int rival;
		if(jugador==1)
			rival=2;
		else {
			rival=1;
		}
		for(int i=0;i<puntosCol.length;i++) {
			puntosCol[i]=evaluarColumna(i,jugador);
		}
		/*DEV VIEW DE LOS PUNTOS Y LAS DECISIONES PROPIAS*/
		System.out.print("[");
		for(int i:puntosCol) {
			System.out.print(i+" ");
		}
		System.out.println("]");
		/*DEV VIEW*/
		for(int i=0;i<puntosRival.length;i++) {
			puntosRival[i]=evaluarColumna(i,rival);
		}
		/*DEV VIEW DE LOS PUNTOS Y LAS DECISIONES DEL RIVAL*/
		System.out.println("Puntos del rival: ");
		System.out.print("[");
		for(int i:puntosRival) {
			System.out.print(i+" ");
		}
		System.out.println("]");
		/*DEV VIEW*/
	}
	
	public int[] compararFuerzas() {
		int aux1=-2, aux2=-2;
		for(int i=0;i<puntosCol.length;i++) {
			if(puntosCol[i]>aux1) 
				aux1=puntosCol[i];
		}
			for(int i=0;i<puntosCol.length;i++) {
				if(puntosRival[i]>aux1) 
					aux2=puntosRival[i];
		}
		if (aux1>aux2)
			return puntosCol;
		else
			System.out.println("DEBUG: Fastidiando al rival.");
			return puntosRival;
	}
	/*elegirMovimiento
	 * Uso: Es la lógica detrás del movimiento del bot.Este algoritmo es el que toma la decisión final sobre qué movimiento es el 
	 * más adecuado, siguiendo factores como la puntuación, la puntuación del contrario para fastidiar al rival, y la dificultad
	 * para ver si hace movimientos malos o aleatorios, o buenos y lógicos.
	 * 
	 * Cómo funciona:
	 *  1. Crea la tabla de puntos según el tablero.
	 *  2. Escoje el movimiento con más fuerza, y si hay múltiples de la misma fuerza, escoge uno aleatorio.
	 *  3. Busca si fastidiar al rival tiene más fuerza o no, y escoge el mayor.
	 *  4. Decide si ignorarlo según la dificuldad (dif. alta = hace el mejor mov, baja = lo ignora).
	 *  5. Devuelve el movimiento final.
	 * */
	public int elegirMovimiento(int jugador) {
		int movFinal=0, aux=-2, contIgual=0, probIgnorar;
		boolean posible=false;
		int[] iguales= {-1,-1,-1,-1,-1,-1,-1};
		int [] tabla;
		
		// Crea la tabla de puntos propia y del rival
		asignarPuntos(jugador);
		
		// Compara qué tipo de movimiento tendría más fuerza (propia puntutación o la del rival)
		tabla=compararFuerzas();
		//Evalua qué movimiento hacer según fuerza
		for(int i=0;i<tabla.length;i++) {
			if(tabla[i]>aux) {
				movFinal=i;
				aux=tabla[i];
				contIgual=0;
				iguales[0]=i;
			}else if(tabla[i]==aux) {
				contIgual++;
				iguales[contIgual]=i;
			}
		}
		
		// DECIDE DE ENTRE TODAS LAS PUNTUACIONES IGUALES
		if(contIgual>0) {
			movFinal=iguales[generarAleatorio(0,contIgual)];
			System.out.println("DEBUG: Mov aleatorio entre "+(contIgual+1)+" con misma fuerza"); // DEV VIEW
		}
		
		// Posibilidad de que ignore el mejor movimiento basado en la dificultad
		probIgnorar=generarAleatorio(0,100);
		
		if(probIgnorar>dificultad) {
			do {
				movFinal=generarAleatorio(0,6);
				if(cAux.encontrarFilaLibre(movFinal)!=-1) {
					posible=true;
				}
			}while(!posible);
			System.out.println("DEBUG: Control de dificultad fallido (gen: "+probIgnorar+", necesario: <"+dificultad+"). ¡A lo loco!");
		}
		pensando();
		System.out.println("Movimiento escogido -> Col "+movFinal); /// DEV VIEW
		return movFinal;
	}
	
		public int generarAleatorio(int min, int max) { 
			Random rnd=new Random(System.nanoTime());
			int random=rnd.nextInt(max-min+1)+min;
			return random;
		}
		
		
		public void pensando() {
			System.out.print("Pensando ");
			try {
				Thread.sleep(600);
				System.out.print(". ");
				Thread.sleep(600);
				System.out.print(". ");
				Thread.sleep(600);
				System.out.print(". ");
				Thread.sleep(600);
				
			} catch (InterruptedException e) {
			}
		}
}
