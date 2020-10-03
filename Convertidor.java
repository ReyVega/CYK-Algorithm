import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Convertidor {
	private ArrayList<Character> simbolosNoTerminales = new ArrayList<Character>();
	private ArrayList<Character> simbolosTerminales = new ArrayList<Character>();
	private LinkedHashMap<Character, ArrayList<String>> producciones = new LinkedHashMap<Character, ArrayList<String>>();
	private String simboloUnitario = null;

	public void eliminarProduccionesQueNoGeneranTerminales() {
		boolean prueba = false;
		LinkedHashSet<Character> N1 = new LinkedHashSet<Character>();
		LinkedHashSet<Character> N1USigma = new LinkedHashSet<Character>();
		N1USigma.addAll(this.simbolosTerminales);

		// Algoritmo para obtener N1
		while (true) {
			LinkedHashSet<Character> tmp = (LinkedHashSet<Character>) N1.clone();

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

		// BFS (Buscar nodos que son alcanzables)
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
		this.actualizarSimbolosNoTerminales();
	}

	public void generarCerraduraDeProducciones() {
		while (true) {
			LinkedHashMap<Character, ArrayList<String>> tmp = clone(this.producciones);
			// Checar si tiene Epsilon Producciones
			for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
				for (int j = 0; j < me.getValue().size(); j++) {
					for (int k = 0; k < me.getValue().get(j).length(); k++) {
						if (this.simbolosNoTerminales.contains(me.getValue().get(j).charAt(k))) {
							if (this.checarSiTieneEpsilonProduccion(me.getValue().get(j).charAt(k))) {
								String word = this.producciones.get(me.getKey()).get(j);
								word = word.substring(0, k) + "" + word.substring(k + 1);
								if (word.equals("")) {
									word = "0";
								}
								if (!me.getValue().contains(word)) {
									this.producciones.get(me.getKey()).add(word);
								}
							}
						}
					}
				}
			}

			// Checar si tiene producciones unitarias
			for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
				for (int j = 0; j < me.getValue().size(); j++) {
					if (me.getValue().get(j).length() == 1) {
						if (this.simbolosNoTerminales.contains(me.getValue().get(j).charAt(0))) {
							if (this.checarSiTieneProduccionUnitaria(me.getValue().get(j))) {
								if (!me.getValue().contains(this.simboloUnitario)) {
									this.producciones.get(me.getKey()).add(this.simboloUnitario);
								}
							}
						}
					}
				}
			}

			if (tmp.equals(this.producciones)) {
				break;
			}
		}

		// Limpiar producciones epsilon
		for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
			for (int j = 0; j < me.getValue().size(); j++) {
				if (me.getValue().get(j).equals("0")) {
					me.getValue().remove(j);
					j--;
				}
			}
		}

		// Limpiar producciones unitarias
		LinkedHashSet<Character> simbolosABorrar = new LinkedHashSet<Character>();
		for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
			if (me.getValue().size() == 1) {
				if (me.getValue().get(0).length() == 1 || me.getValue().get(0).length() == 0) {
					simbolosABorrar.add(me.getKey());
				}
			}
		}

		for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
			for (int j = 0; j < me.getValue().size(); j++) {
				if (simbolosABorrar.contains(me.getValue().get(j).charAt(0))) {
					me.getValue().remove(j);
					j--;
				}
			}
		}

		for (Character ch : simbolosABorrar) {
			this.producciones.remove(ch);
		}

		// Actualizar conjunto de simbolos no terminales
		this.actualizarSimbolosNoTerminales();
	}

	public boolean checarSiTieneEpsilonProduccion(Character ch) {
		for (String m : this.producciones.get(ch)) {
			if (m.equals("0")) {
				return true;
			}
		}
		return false;
	}

	public boolean checarSiTieneProduccionUnitaria(String ch) {
		for (String m : this.producciones.get(ch.charAt(0))) {
			if (m.length() == 1 && this.simbolosTerminales.contains(m.charAt(0))) {
				this.simboloUnitario = Character.toString(m.charAt(0));
				return true;
			}
		}
		return false;
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
		for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
			System.out.print(me.getKey() + "->");
			for (int j = 0; j < me.getValue().size(); j++) {
				if (j + 1 >= me.getValue().size()) {
					System.out.print(me.getValue().get(j));
				} else {
					System.out.print(me.getValue().get(j) + ",");
				}
			}
			System.out.println();
		}
	}

	public void actualizarSimbolosNoTerminales() {
		this.simbolosNoTerminales.clear();
		for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
			this.simbolosNoTerminales.add(me.getKey());
		}
	}

	public LinkedHashMap<Character, ArrayList<String>> clone(LinkedHashMap<Character, ArrayList<String>> hm) {
		LinkedHashMap<Character, ArrayList<String>> clon = new LinkedHashMap<Character, ArrayList<String>>();
		for (int i = 0; i < this.simbolosNoTerminales.size(); i++) {
			clon.put(this.simbolosNoTerminales.get(i),
					(ArrayList<String>) hm.get(this.simbolosNoTerminales.get(i)).clone());
		}
		return clon;
	}
}
