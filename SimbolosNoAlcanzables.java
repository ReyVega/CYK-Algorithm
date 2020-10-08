import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

public class SimbolosNoAlcanzables {
	private ArrayList<Character> simbolosNoTerminales;
	private ArrayList<Character> simbolosTerminales;
	private LinkedHashMap<Character, ArrayList<String>> producciones;

	public SimbolosNoAlcanzables(ArrayList<Character> simbolosNoTerminales, ArrayList<Character> simbolosTerminales,
			LinkedHashMap<Character, ArrayList<String>> producciones) {
		this.simbolosNoTerminales = simbolosNoTerminales;
		this.simbolosTerminales = simbolosTerminales;
		this.producciones = producciones;
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
	}

	// Getters
	public ArrayList<Character> getSimbolosNoTerminales() {
		return this.simbolosNoTerminales;
	}

	public ArrayList<Character> getSimbolosTerminales() {
		return this.simbolosTerminales;
	}

	public LinkedHashMap<Character, ArrayList<String>> getProducciones() {
		return this.producciones;
	}
}
