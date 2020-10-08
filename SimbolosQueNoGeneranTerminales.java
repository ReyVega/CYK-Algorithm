import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;

public class SimbolosQueNoGeneranTerminales {
	private ArrayList<Character> simbolosNoTerminales;
	private ArrayList<Character> simbolosTerminales;
	private LinkedHashMap<Character, ArrayList<String>> producciones;

	public SimbolosQueNoGeneranTerminales(ArrayList<Character> simbolosNoTerminales,
			ArrayList<Character> simbolosTerminales, LinkedHashMap<Character, ArrayList<String>> producciones) {
		this.simbolosNoTerminales = simbolosNoTerminales;
		this.simbolosTerminales = simbolosTerminales;
		this.producciones = producciones;
	}

	public void eliminarProduccionesQueNoGeneranTerminales() {
		boolean prueba = false;
		LinkedHashSet<Character> N1 = new LinkedHashSet<Character>();
		LinkedHashSet<Character> N1USigma = new LinkedHashSet<Character>();
		N1USigma.addAll(this.simbolosTerminales);
		N1USigma.add('0');

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