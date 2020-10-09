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

	public void aplicarAlgoritmo() {
		
		// Llenar primera linea
		for(int i = 0; i < this.word.length(); i++) {
			for(Entry<Character, LinkedHashSet<String>> me : this.p.entrySet()) {
				if(me.getValue().contains(Character.toString(this.word.charAt(i)))) {
					this.matriz.get(0).get(i).add(me.getKey());
				}
			}
		}
		System.out.println(this.matriz);
		
	}

}
