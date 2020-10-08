import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class CerraduraDeProducciones {
	private ArrayList<Character> simbolosNoTerminales;
	private ArrayList<Character> simbolosTerminales;
	private LinkedHashMap<Character, ArrayList<String>> producciones;
	private String simboloUnitario;

	public CerraduraDeProducciones(ArrayList<Character> simbolosNoTerminales, ArrayList<Character> simbolosTerminales,
			LinkedHashMap<Character, ArrayList<String>> producciones) {
		this.simbolosNoTerminales = simbolosNoTerminales;
		this.simbolosTerminales = simbolosTerminales;
		this.producciones = producciones;
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
					if (me.getValue().get(j).length() == 1
							&& this.simbolosNoTerminales.contains(me.getValue().get(j).charAt(0))) {
						if (this.checarSiTieneProduccionUnitaria(me.getValue().get(j))) {
							if (!me.getValue().contains(this.simboloUnitario)) {
								this.producciones.get(me.getKey()).add(this.simboloUnitario);
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
		for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
			for (int j = 0; j < me.getValue().size(); j++) {
				if (me.getValue().get(j).length() == 1
						&& this.simbolosNoTerminales.contains(me.getValue().get(j).charAt(0))) {
					me.getValue().remove(j);
					j--;
				}
			}
		}

		for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
			if (me.getValue().size() == 1) {
				if (me.getValue().get(0).length() == 1
						&& this.simbolosTerminales.contains(me.getValue().get(0).charAt(0))) {
					this.producciones.remove(me.getKey());
				}
			}
		}
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

	// Método clone para LinkedHashMap
	public LinkedHashMap<Character, ArrayList<String>> clone(LinkedHashMap<Character, ArrayList<String>> hm) {
		LinkedHashMap<Character, ArrayList<String>> clon = new LinkedHashMap<Character, ArrayList<String>>();
		for (int i = 0; i < this.simbolosNoTerminales.size(); i++) {
			clon.put(this.simbolosNoTerminales.get(i),
					(ArrayList<String>) hm.get(this.simbolosNoTerminales.get(i)).clone());
		}
		return clon;
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
