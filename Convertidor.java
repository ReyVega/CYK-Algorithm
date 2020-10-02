import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Convertidor {
	private ArrayList<Character> simbolosNoTerminales = new ArrayList<Character>();
	private ArrayList<Character> simbolosTerminales = new ArrayList<Character>();
	private HashMap<Character, ArrayList<String>> producciones = new HashMap<Character, ArrayList<String>>();

	public void eliminarProduccionesQueNoGeneranTerminales() {
		boolean prueba = false;
		Set<Character> N1 = new HashSet<Character>();
		Set<Character> N1USigma = new HashSet<Character>();
		N1USigma.addAll(this.simbolosTerminales);

		// Algoritmo para obtener N1
		while (true) {
			Set<Character> tmp = new HashSet<Character>(N1);

			// Recorrer todas las producciones
			for (int i = 0; i < this.simbolosNoTerminales.size(); i++) {
				
				// Recorrer lista de producciones
				if(this.producciones.containsKey(this.simbolosNoTerminales.get(i))) {
					for (int j = 0; j < this.producciones.get(this.simbolosNoTerminales.get(i)).size(); j++) {
						// Verificar si la producción pertenece a {N1 U Sigma}* donde Sigma es el
						// alfabeto de entrada (Símbolos no terminales inicialmente)
						for (int k = 0; k < this.producciones.get(this.simbolosNoTerminales.get(i)).get(j).length(); k++) {
							if (!N1USigma
									.contains(this.producciones.get(this.simbolosNoTerminales.get(i)).get(j).charAt(k))) {
								prueba = false;
								break;
							} else {
								prueba = true;
							}
						}

						if (prueba) {
							N1.add(this.simbolosNoTerminales.get(i));
						}
					}
				}				
			}
			if (tmp.containsAll(N1)) {
				break;
			} else {
				N1USigma.addAll(N1);
			}
		}

		// Quitar producciones
		Set<Character> simbolosNoT = new HashSet<Character>();
		simbolosNoT.addAll(this.simbolosNoTerminales);
		simbolosNoT.removeAll(N1);
		
		for (int i = 0; i < this.simbolosNoTerminales.size(); i++) {
			if(this.producciones.containsKey(this.simbolosNoTerminales.get(i))) {
				for (int j = 0; j < this.producciones.get(this.simbolosNoTerminales.get(i)).size(); j++) {
					for (int k = 0; k < this.producciones.get(this.simbolosNoTerminales.get(i)).get(j).length(); k++) {
						if (simbolosNoT
								.contains(this.producciones.get(this.simbolosNoTerminales.get(i)).get(j).charAt(k))) {
							this.producciones.get(this.simbolosNoTerminales.get(i)).remove(j);
							j--;
							break;
						}
					}
				}
			}
		}
	}

	public void eliminarProduccionesNoAlcanzables() {
		// Grafo de dependencia
		ArrayList<Set<Character>> grafo = new ArrayList<Set<Character>>();

		// Generacion de grafo
		for (int i = 0; i < this.simbolosNoTerminales.size(); i++) {
			grafo.add(new HashSet<Character>());
			grafo.get(i).add(this.simbolosNoTerminales.get(i));
			if(this.producciones.containsKey(this.simbolosNoTerminales.get(i))) {
				for (int j = 0; j < this.producciones.get(this.simbolosNoTerminales.get(i)).size(); j++) {
					for (int k = 0; k < this.producciones.get(this.simbolosNoTerminales.get(i)).get(j).length(); k++) {
						if (this.simbolosNoTerminales
								.contains(this.producciones.get(this.simbolosNoTerminales.get(i)).get(j).charAt(k))) {
							grafo.get(i).add(this.producciones.get(this.simbolosNoTerminales.get(i)).get(j).charAt(k));
						}
					}
				}
			}
		}
		System.out.println(grafo);
		
		// Eliminar no alcanzables
		for (int i = 0; i < this.simbolosNoTerminales.size(); i++) {
			
		}
	}

	public void eliminarProduccionesEpsilon() {
		for (int i = 0; i < this.simbolosNoTerminales.size(); i++) {
			for (int j = 0; j < this.producciones.get(this.simbolosNoTerminales.get(i)).size(); j++) {
				for (int k = 0; k < this.producciones.get(this.simbolosNoTerminales.get(i)).get(j).length(); k++) {
					if (this.simbolosNoTerminales
							.contains(this.producciones.get(this.simbolosNoTerminales.get(i)).get(j).charAt(k))) {
						this.checarSiTieneEpsilonProduccion(
								this.producciones.get(this.simbolosNoTerminales.get(i)).get(j).charAt(k),
								this.simbolosNoTerminales.get(i), i, j);
					}
				}
			}
		}
	}

	public void checarSiTieneEpsilonProduccion(Character ch, Character proveniente, int indexI, int IndexJ) {
		for (int i = 0; i < this.producciones.get(ch).size(); i++) {
			for (int j = 0; j < this.producciones.get(ch).get(i).length(); j++) {
				if (this.producciones.get(ch).get(i).charAt(j) == '0') {
					this.producciones.get(ch).remove(i);
				}
			}
		}
	}

	public void eliminarProduccionesUnitarias() {

	}

	public void convertirChomsky() {

	}

	public void convertirGreibach() {

	}

	public void leerArchivo() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("input.txt"));
			String line; 
			ArrayList<String> tmp;
			
			line = br.readLine();
			for(int i = 0; i < line.length(); i++) {
				if(line.charAt(i) != ',') {
					this.simbolosNoTerminales.add(line.charAt(i));
				}
			}
			
			line = br.readLine();
			for(int i = 0; i < line.length(); i++) {
				if(line.charAt(i) != ',') {
					this.simbolosTerminales.add(line.charAt(i));
				}
			}
			
			while ((line = br.readLine()) != null) {
				tmp = new ArrayList<String>(Arrays.asList(line.substring(3).split(",")));
				this.producciones.put(line.charAt(0), tmp);
			} 
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void imprimirGramatica() {
		for(int i = 0; i < this.simbolosNoTerminales.size(); i++) {
			if(this.producciones.containsKey(this.simbolosNoTerminales.get(i))) {
				System.out.print(this.simbolosNoTerminales.get(i) + "->");
				for(int j = 0; j < this.producciones.get(this.simbolosNoTerminales.get(i)).size(); j++) {
					if(j + 1 >= this.producciones.get(this.simbolosNoTerminales.get(i)).size()) {
						System.out.print(this.producciones.get(this.simbolosNoTerminales.get(i)).get(j));
					} else {
						System.out.print(this.producciones.get(this.simbolosNoTerminales.get(i)).get(j) + ",");
					}
				}
				System.out.println();
			}
		}
	}
}
