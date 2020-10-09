import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

public class AlgoritmoCYK {
	private ArrayList<ArrayList<LinkedHashSet<Character>>> matriz = new ArrayList<ArrayList<LinkedHashSet<Character>>>();
	private LinkedHashMap<Character, LinkedHashSet<String>> p = new LinkedHashMap<Character, LinkedHashSet<String>>();
	private ArrayList<Character> simbolosNoTerminales;
	private ArrayList<Character> simbolosTerminales;
	private LinkedHashMap<Character, ArrayList<String>> producciones;
	private String word;

	public AlgoritmoCYK(ArrayList<Character> simbolosNoTerminales, ArrayList<Character> simbolosTerminales,
			LinkedHashMap<Character, ArrayList<String>> producciones, String word) {
		this.simbolosNoTerminales = simbolosNoTerminales;
		this.simbolosTerminales = simbolosTerminales;
		this.producciones = producciones;
		this.word = word;

		// Preparar producciones para el algoritmo
		for (Entry<Character, ArrayList<String>> me : this.producciones.entrySet()) {
			this.p.put(me.getKey(), new LinkedHashSet<String>());
			for (String prod : me.getValue()) {
				this.p.get(me.getKey()).add(prod);
			}
		}

		// Generar Matriz
		for (int i = word.length(), m = 0; i > 0; i--, m++) {
			this.matriz.add(new ArrayList<LinkedHashSet<Character>>());
			for (int j = 0; j < i; j++) {
				this.matriz.get(m).add(new LinkedHashSet<Character>());
			}
		}
	}

	public boolean aplicarAlgoritmo() {

		// Llenar primera linea
		for (int i = 0; i < this.word.length(); i++) {
			for (Entry<Character, LinkedHashSet<String>> me : this.p.entrySet()) {
				if (me.getValue().contains(Character.toString(this.word.charAt(i)))) {
					this.matriz.get(0).get(i).add(me.getKey());
				}
			}
		}

		// Algoritmo
		for (int i = 1; i < this.word.length(); i++) {
			for (int j = 0; j < this.matriz.get(i).size(); j++) {
				int w = j;
				int z = i;
				for (int k = 0; k < i; k++) {
					LinkedHashSet<Character> tmp = this.checarSiExisteProduccion(this.matriz.get(k).get(j),
							this.matriz.get(--z).get(++w));
					this.matriz.get(i).get(j).addAll(tmp);
				}
			}
		}
		return this.matriz.get(word.length() - 1).get(0).contains(this.simbolosNoTerminales.get(0));
	}

	public LinkedHashSet<Character> checarSiExisteProduccion(LinkedHashSet<Character> first,
			LinkedHashSet<Character> second) {
		
		LinkedHashSet<String> tmp = new LinkedHashSet<String>();

		for (Character ch : first) {
			for (Character ch2 : second) {
				tmp.add(Character.toString(ch) + Character.toString(ch2));
			}
		}
				
		LinkedHashSet<Character> encontrados = new LinkedHashSet<Character>();

		for (String word : tmp) {
			for (Entry<Character, LinkedHashSet<String>> me : this.p.entrySet()) {
				if (me.getValue().contains(word)) {
					encontrados.add(me.getKey());
				}
			}
		}
		return encontrados;
	}
}
