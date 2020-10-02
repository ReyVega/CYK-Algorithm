import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Convertidor {
	private ArrayList<Character> simbolosNoTerminales = new ArrayList<Character>();
	private ArrayList<Character> simbolosTerminales = new ArrayList<Character>();
	private LinkedHashMap<Character, ArrayList<String>> producciones = new LinkedHashMap<Character, ArrayList<String>>();

	public void eliminarProduccionesQueNoGeneranTerminales() {
		boolean prueba = false;
		LinkedHashSet<Character> N1 = new LinkedHashSet<Character>();
		LinkedHashSet<Character> N1USigma = new LinkedHashSet<Character>();
		N1USigma.addAll(this.simbolosTerminales);

		// Algoritmo para obtener N1
		while (true) {
			LinkedHashSet<Character> tmp = new LinkedHashSet<Character>(N1);

			// Recorrer todas las producciones
			for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {

				// Recorrer lista de producciones
				for (int j = 0; j < me.getValue().size(); j++) {
					// Verificar si la producción pertenece a {N1 U Sigma}* donde Sigma es el
					// alfabeto de entrada (Símbolos no terminales inicialmente)
					for (int k = 0; k < me.getValue().get(j).length(); k++) {
						if (!N1USigma.contains(me.getValue().get(j).charAt(k))) {
							prueba = false;
							break;
						} else {
							prueba = true;
						}
					}

					if (prueba) {
						N1.add(me.getKey());
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
		LinkedHashSet<Character> simbolosNoT = new LinkedHashSet<Character>();
		simbolosNoT.addAll(this.simbolosNoTerminales);
		simbolosNoT.removeAll(N1);

		for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
			for (int j = 0; j < me.getValue().size(); j++) {
				for (int k = 0; k < me.getValue().get(j).length(); k++) {
					if (simbolosNoT.contains(me.getValue().get(j).charAt(k))) {
						this.producciones.get(me.getKey()).remove(j);
						j--;
						break;
					}
				}
			}
		}
	}

	public void eliminarProduccionesNoAlcanzables() {
		// Grafo de dependencia
		LinkedHashMap<Character, ArrayList<String>> grafo = new LinkedHashMap<Character, ArrayList<String>>();

		// Generacion de grafo
		for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
			grafo.put(me.getKey(), new ArrayList<String>());
			for (int j = 0; j < me.getValue().size(); j++) {
				for (int k = 0; k < me.getValue().get(j).length(); k++) {
					if (this.simbolosNoTerminales.contains(me.getValue().get(j).charAt(k))) {
						if (!grafo.get(me.getKey()).contains(Character.toString(me.getValue().get(j).charAt(k)))) {
							grafo.get(me.getKey()).add(Character.toString(me.getValue().get(j).charAt(k)));
						}
					}
				}
			}
		}

		// BFS (Busca nodos que son alcanzables)
		LinkedList<Character> queue = new LinkedList<Character>();
		LinkedHashSet<Character> visited = new LinkedHashSet<Character>();
		queue.add(this.simbolosNoTerminales.get(0));

		while (!queue.isEmpty()) {
			Character current = queue.poll();
			visited.add(current);
			ArrayList<String> vecinos = grafo.get(current);
			for (String m : vecinos) {
				if (!visited.contains(m.charAt(0))) {
					visited.add(m.charAt(0));
					queue.add(m.charAt(0));
				}
			}
		}

		// Eliminar símbolos no alcanzables
		LinkedHashSet<Character> simbolos = new LinkedHashSet<Character>();
		simbolos.addAll(this.simbolosNoTerminales);
		simbolos.removeAll(visited);

		for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
			if (simbolos.contains(me.getKey())) {
				this.producciones.remove(me.getKey());
			}
		}

		// Actualizar conjunto de simbolos no terminales
		this.simbolosNoTerminales.clear();
		for(Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
			this.simbolosNoTerminales.add(me.getKey());
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
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) != ',') {
					this.simbolosNoTerminales.add(line.charAt(i));
				}
			}

			line = br.readLine();
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) != ',') {
					this.simbolosTerminales.add(line.charAt(i));
				}
			}

			while ((line = br.readLine()) != null) {
				tmp = new ArrayList<String>(Arrays.asList(line.substring(3).split(",")));
				this.producciones.put(line.charAt(0), tmp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void imprimirGramatica() {
		for (int i = 0; i < this.simbolosNoTerminales.size(); i++) {
			if (this.producciones.containsKey(this.simbolosNoTerminales.get(i))) {
				System.out.print(this.simbolosNoTerminales.get(i) + "->");
				for (int j = 0; j < this.producciones.get(this.simbolosNoTerminales.get(i)).size(); j++) {
					if (j + 1 >= this.producciones.get(this.simbolosNoTerminales.get(i)).size()) {
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
