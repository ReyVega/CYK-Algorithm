
public class Main {
	public static void main(String[] args) {
		Convertidor c = new Convertidor();
		c.leerArchivo();
		c.eliminarProduccionesQueNoGeneranTerminales();
//		c.eliminarProduccionesNoAlcanzables();
		c.imprimirGramatica();
	}
}
