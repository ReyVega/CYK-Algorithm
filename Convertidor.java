import java.util.Map.Entry;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class Convertidor {
	// Atributos
	private String word;
	private ArrayList<Character> simbolosNoTerminales = new ArrayList<Character>();
	private ArrayList<Character> simbolosTerminales = new ArrayList<Character>();
	private LinkedHashMap<Character, ArrayList<String>> producciones = new LinkedHashMap<Character, ArrayList<String>>();

	public void eliminarProduccionesQueNoGeneranTerminales() {
		SimbolosQueNoGeneranTerminales s = new SimbolosQueNoGeneranTerminales(this.simbolosNoTerminales,
				this.simbolosTerminales, this.producciones);
		s.eliminarProduccionesQueNoGeneranTerminales();
		this.simbolosNoTerminales = s.getSimbolosNoTerminales();
		this.simbolosTerminales = s.getSimbolosTerminales();
		this.producciones = s.getProducciones();
		this.actualizarSimbolosNoTerminales();
	}

	public void eliminarProduccionesNoAlcanzables() {
		SimbolosNoAlcanzables s = new SimbolosNoAlcanzables(this.simbolosNoTerminales, this.simbolosTerminales,
				this.producciones);
		s.eliminarProduccionesNoAlcanzables();
		this.simbolosNoTerminales = s.getSimbolosNoTerminales();
		this.simbolosTerminales = s.getSimbolosTerminales();
		this.producciones = s.getProducciones();
		this.actualizarSimbolosNoTerminales();
	}

	public void generarCerraduraDeProducciones() {
		CerraduraDeProducciones s = new CerraduraDeProducciones(this.simbolosNoTerminales, this.simbolosTerminales,
				this.producciones);
		s.generarCerraduraDeProducciones();
		this.simbolosNoTerminales = s.getSimbolosNoTerminales();
		this.simbolosTerminales = s.getSimbolosTerminales();
		this.producciones = s.getProducciones();
		this.actualizarSimbolosNoTerminales();
	}

	public void convertirChomsky() {
		FNCh s = new FNCh(this.simbolosNoTerminales, this.simbolosTerminales, this.producciones);
		s.convertirChomsky();
		this.simbolosNoTerminales = s.getSimbolosNoTerminales();
		this.simbolosTerminales = s.getSimbolosTerminales();
		this.producciones = s.getProducciones();
		this.actualizarSimbolosNoTerminales();
	}

	public void algoritmoCYK() {
		AlgoritmoCYK s = new AlgoritmoCYK(this.simbolosNoTerminales, this.simbolosTerminales, this.producciones,
				this.word);
		if (s.aplicarAlgoritmo()) {
			System.out.println("La cadena es aceptada por la gramática");
			s.construirArbol();
		} else {
			System.out.println("La cadena no es aceptada por la gramática");
		}
	}

	public void leerArchivo() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("input.txt"));
			String line;
			ArrayList<String> tmp;

			this.word = br.readLine();

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
}