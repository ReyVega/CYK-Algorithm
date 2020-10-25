import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class AlgoritmoCYK {
	private ArrayList<ArrayList<LinkedHashSet<Character>>> matriz = new ArrayList<ArrayList<LinkedHashSet<Character>>>();
	private LinkedHashMap<Character, LinkedHashSet<String>> p = new LinkedHashMap<Character, LinkedHashSet<String>>();
	private ArrayList<Character> simbolosNoTerminales;
	private LinkedHashMap<Character, ArrayList<String>> producciones;
	private String word;

	// Arbol
	private LinkedList<Character> nodoActual = new LinkedList<Character>();
	private ArrayList<ArrayList<Character>> recorrido = new ArrayList<ArrayList<Character>>();
	private int indexArray = 0;

	public AlgoritmoCYK(ArrayList<Character> simbolosNoTerminales,
			LinkedHashMap<Character, ArrayList<String>> producciones, String word) {
		this.simbolosNoTerminales = simbolosNoTerminales;
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

	public void generarRecorridoArbol() {
		this.nodoActual.add(this.simbolosNoTerminales.get(0));

		LinkedList<Integer> i = new LinkedList<Integer>();
		LinkedList<Integer> j = new LinkedList<Integer>();

		i.add(word.length() - 1);
		j.add(0);

		while (!(i.isEmpty() && j.isEmpty())) {
			int indexI = i.poll();
			int indexJ = j.poll();
			int z = indexI;
			int w = indexJ;
			Character n = this.nodoActual.poll();
			this.recorrido.add(new ArrayList<Character>(Arrays.asList(n)));

			if (indexI == 0) {
				this.recorrido.get(this.indexArray++).add(this.word.charAt(indexJ));
			} else {
				for (int k = 0; k < indexI; k++) {
					if (this.checarCorrespondencia(this.matriz.get(k).get(indexJ), this.matriz.get(--z).get(++w), n)) {
						i.add(k);
						j.add(indexJ);
						i.add(z);
						j.add(w);
						break;
					}
				}
			}
		}
		this.construirArbol();
	}

	public boolean checarCorrespondencia(LinkedHashSet<Character> first, LinkedHashSet<Character> second,
			Character nodoActual) {

		LinkedHashSet<String> tmp = new LinkedHashSet<String>();

		for (Character ch : first) {
			for (Character ch2 : second) {
				tmp.add(Character.toString(ch) + Character.toString(ch2));
			}
		}

		for (String word : tmp) {
			if (this.p.get(nodoActual).contains(word)) {
				this.recorrido.get(this.indexArray).add(word.charAt(0));
				this.recorrido.get(this.indexArray).add(word.charAt(1));
				this.nodoActual.add(word.charAt(0));
				this.nodoActual.add(word.charAt(1));
				this.indexArray++;
				return true;
			}
		}
		return false;
	}

	public void construirArbol() {
		// 1era iteración
		DefaultMutableTreeNode parent = new DefaultMutableTreeNode(this.simbolosNoTerminales.get(0));
		DefaultMutableTreeNode c1 = new DefaultMutableTreeNode(this.recorrido.get(0).get(1));
		DefaultMutableTreeNode c2 = new DefaultMutableTreeNode(this.recorrido.get(0).get(2));

		DefaultTreeModel modelo = new DefaultTreeModel(parent);
		modelo.insertNodeInto(c1, parent, 0);
		modelo.insertNodeInto(c2, parent, 1);

		JTree tree = new JTree(modelo);
		final Font currentFont = tree.getFont();
		final Font bigFont = new Font(currentFont.getName(), currentFont.getStyle(), currentFont.getSize() + 5);
		tree.setFont(bigFont);
		DefaultTreeCellRenderer render = (DefaultTreeCellRenderer) tree.getCellRenderer();
		render.setLeafIcon(new ImageIcon("leaf.png"));
		render.setOpenIcon(new ImageIcon("branch.png"));
		render.setClosedIcon(new ImageIcon("branch.png"));

		LinkedList<DefaultMutableTreeNode> parents = new LinkedList<DefaultMutableTreeNode>();

		parents.add(c1);
		parents.add(c2);

		while (!parents.isEmpty()) {
			for (int j = 1; j < this.recorrido.get(0).size(); j++) {
				for (int k = 1; k < this.recorrido.size(); k++) {
					if (this.recorrido.get(0).get(j) == this.recorrido.get(k).get(0)) {
						if (this.recorrido.get(k).size() == 2) {
							DefaultMutableTreeNode child = new DefaultMutableTreeNode(this.recorrido.get(k).get(1));
							modelo.insertNodeInto(child, parents.get(0), 0);
							parents.poll();
							this.recorrido.remove(k);
							break;
						} else {
							DefaultMutableTreeNode child1 = new DefaultMutableTreeNode(this.recorrido.get(k).get(1));
							DefaultMutableTreeNode child2 = new DefaultMutableTreeNode(this.recorrido.get(k).get(2));
							modelo.insertNodeInto(child1, parents.get(0), 0);
							modelo.insertNodeInto(child2, parents.get(0), 1);
							parents.poll();
							parents.add(child1);
							parents.add(child2);
							break;
						}
					}
				}
			}
			this.recorrido.remove(0);
			if (this.recorrido.isEmpty()) {
				break;
			}
		}

		JFrame v = new JFrame("Árbol de derivación");
		JScrollPane scroll = new JScrollPane(tree);
		JPanel panel = new JPanel();
		JButton expandBtn = new JButton("Expandir todo");
		expandBtn.addActionListener(e -> {
			setTreeExpandedState(tree, true);
		});
		panel.add(expandBtn);

		JButton collapseBtn = new JButton("Cerrar todo");
		collapseBtn.addActionListener(e -> {
			setTreeExpandedState(tree, false);
		});
		panel.add(collapseBtn);

		v.add(panel, BorderLayout.NORTH);
		panel.add(collapseBtn);
		v.setSize(new Dimension(500, 500));
		v.setIconImage(new ImageIcon("branch.png").getImage());
		v.getContentPane().add(scroll);
		v.setLocationRelativeTo(null);
		v.setVisible(true);
		v.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void setTreeExpandedState(JTree tree, boolean expanded) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getModel().getRoot();
		setNodeExpandedState(tree, node, expanded);
	}

	public void setNodeExpandedState(JTree tree, TreeNode treeNode2, boolean expanded) {
		ArrayList<TreeNode> list = (ArrayList<TreeNode>) Collections.list(treeNode2.children());
		for (TreeNode treeNode : list) {
			setNodeExpandedState(tree, treeNode, expanded);
		}
		if (!expanded && ((DefaultMutableTreeNode) treeNode2).isRoot()) {
			return;
		}
		TreePath path = new TreePath(((DefaultMutableTreeNode) treeNode2).getPath());
		if (expanded) {
			tree.expandPath(path);
		} else {
			tree.collapsePath(path);
		}
	}
}
