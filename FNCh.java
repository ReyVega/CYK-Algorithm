import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;

public class FNCh {
	private ArrayList<Character> simbolosNoTerminales;
	private ArrayList<Character> simbolosTerminales;
	private LinkedHashMap<Character, ArrayList<String>> producciones;
	private ArrayList<Character> simbolosChomsky = new ArrayList<Character>();

	public FNCh(ArrayList<Character> simbolosNoTerminales, ArrayList<Character> simbolosTerminales,
			LinkedHashMap<Character, ArrayList<String>> producciones) {
		this.simbolosNoTerminales = simbolosNoTerminales;
		this.simbolosTerminales = simbolosTerminales;
		this.producciones = producciones;

		// Símbolos para utilizar en la conversión de GLC a Chomsky
		for (Character ch = 'A'; ch <= 'Z'; ch++) {
			this.simbolosChomsky.add(ch);
		}
	}

	public void convertirChomsky() {
		LinkedHashSet<Character> terminalesVisitados = new LinkedHashSet<Character>();

		// Recorremos y buscamos posibles simbolos terminales para reemplazar
		for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
			for (int j = 0; j < me.getValue().size(); j++) {
				for (int k = 0; k < me.getValue().get(j).length(); k++) {
					if (this.simbolosTerminales.contains(me.getValue().get(j).charAt(k))) {
						terminalesVisitados.add(me.getValue().get(j).charAt(k));
					}
				}
			}
		}

		// Añadimos nuevas producciones y vemos por cuales simbolos reemplazaremos los
		// simbolos terminales
		LinkedHashMap<Character, Character> reemplazador = new LinkedHashMap<Character, Character>();
		this.simbolosChomsky.removeAll(this.simbolosNoTerminales);

		for (Character ch : terminalesVisitados) {
			this.producciones.put(this.simbolosChomsky.get(0),
					new ArrayList<String>(Arrays.asList(Character.toString(ch))));
			reemplazador.put(ch, this.simbolosChomsky.get(0));
			this.simbolosChomsky.remove(0);
		}

		// Reemplazamos simbolos por nuevas producciones
		for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
			for (int j = 0; j < me.getValue().size(); j++) {
				if (me.getValue().get(j).length() > 1) {
					for (int k = 0; k < me.getValue().get(j).length(); k++) {
						if (reemplazador.containsKey(me.getValue().get(j).charAt(k))) {
							String word = this.producciones.get(me.getKey()).get(j);
							word = word.substring(0, k) + reemplazador.get(me.getValue().get(j).charAt(k))
									+ word.substring(k + 1);
							this.producciones.get(me.getKey()).set(j, word);
						}
					}
				}
			}
		}

		// Generamos más producciones para que las producciones actuales solo sean
		// concatenaciones de 2
		for (int i = 0; i < this.simbolosNoTerminales.size(); i++) {
			for (int j = 0; j < this.producciones.get(this.simbolosNoTerminales.get(i)).size(); j++) {
				while (this.producciones.get(this.simbolosNoTerminales.get(i)).get(j).length() > 2) {
					String word = this.producciones.get(this.simbolosNoTerminales.get(i)).get(j);
					word = word.substring(1, 3);
					this.producciones.put(this.simbolosChomsky.get(0), new ArrayList<String>(Arrays.asList(word)));

					word = this.producciones.get(this.simbolosNoTerminales.get(i)).get(j);
					word = word.substring(0, 1) + this.simbolosChomsky.get(0) + word.substring(3);
					this.producciones.get(this.simbolosNoTerminales.get(i)).set(j, word);
					this.simbolosChomsky.remove(0);
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
