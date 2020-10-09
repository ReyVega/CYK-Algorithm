
public class Main {
	public static void main(String[] args) {
		Convertidor c = new Convertidor();
		c.leerArchivo();
		// 1er paso (Elimninamos producciones que no generan terminales)
		c.eliminarProduccionesQueNoGeneranTerminales();
		// 2do paso (Eliminamos producciones que no sean alcanzables partiendo desde el símbolo inicial)
		c.eliminarProduccionesNoAlcanzables();
		// 3er paso (Generamos la cerradura de producciones y eliminamos producciones épsilon y unitarias)
		c.generarCerraduraDeProducciones();
		// 4to paso (Limpiamos nuestra grámatica debido a que por la cerradura se generan simbolos no alcanzables)
		c.eliminarProduccionesNoAlcanzables();
		// 5to paso (Convertir GLC a FNCh)
		c.convertirChomsky();
		c.imprimirGramatica();
		// 6to paso (Ya podemos utilizar el algoritmo para verificar que nuestra cadena corresponde a la gramática)
		c.algoritmoCYK();
	}
}
