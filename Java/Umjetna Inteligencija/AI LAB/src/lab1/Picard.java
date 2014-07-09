package lab1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Picard {
	
	private static String[][] map;
	private Node start;
	private HeuristicNode hstart;
	private int noOfOpenNodes = 0;
	private int noOfVisitedNodes = 0;
	private static int border;
	private static ArrayList<Teleporter> enterpriseTeleporters; /* liste za heuristiku 2*/
	private static ArrayList<Teleporter> eladrelTeleporters;
	private static int goalI;
	private static int goalJ;
	private static Teleporter teleporterClosestToGoal = null;
	private static int heuristicMode = 1; 	/*						
	 											1 - prva heuristika (čisti manhattan do cilja)
	 											2 - druga heuristika (manhattan do najblizeg teleportera + manhattan tog teleportera do teleportera
	 													najblizeg cilju + manhattan tog teleportera do cilja)
	 											po defaultu namješteno na prvu heuristiku
	 										*/
	
	public Picard(String[][] _map) {
		map = _map;
		for (int i=0; i < map.length; i++)
			for (int j=0; j<map.length; j++)
				if (map[i][j].equals("P"))
					try {
						start = new Node(i, j, 0, null);
						hstart = new HeuristicNode(i, j, 0, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
	}
	
	public static void setHeuristicMode(int mode) {
		heuristicMode = mode;
	}
	public static int getHeuristicMode() {
		return heuristicMode;
	}		
	
	public void printMap() {		
		for (int i=0; i < map.length; i++) {
			for (int j=0; j < map.length; j++) {
				System.out.format("%2s ", map[i][j]);
				if (j+1 == map.length/2)
					System.out.print("| ");
			}
			System.out.println();
		}
	}
	
	public int getNoOfOpenNodes() {
		return noOfOpenNodes;
	}
	public int getNoOfVisitedNodes() {
		return noOfVisitedNodes;
	}
	public static int maxdim() {
		return map.length;
	}	
	public static String getVal(int i, int j) {
		return map[i][j];
	}
		
	public static int calcWeight(int x1, int y1, int x2, int y2) throws Exception{
		int border = Picard.maxdim()/2; 
		if (y1 < border && y2 >= border) { /* provjerava je li se pokusava prec preko vertikalne granice */
			throw new Exception();
		}		
		else if (y1 >= border && y2 < border)
			throw new Exception();
		return Picard.calcWeight(Picard.getVal(x1, y1), Picard.getVal(x2, y2));				
	}
	
	public static int calcWeight(String a, String b) {
		int A, B;
		try {
			A = Integer.parseInt(a);
		} catch (NumberFormatException nfe){
			A = 0;
		}
		try {
			B = Integer.parseInt(b);
		} catch (NumberFormatException nfe){
			B = 0;
		}
		
		return Math.abs(A-B);
	}
	
	public static int manhattanDist(int x1, int y1, int x2, int y2) {
		return Math.abs(x2-x1)+Math.abs(y2-y1);
	}
	
	public Node blindAlgorithm() {
		ArrayList<Node> open = new ArrayList<Node>();
		ArrayList<Node> visited = new ArrayList<Node>();
		open.add(start);		
		
		while (!open.isEmpty()) {
			Node n = open.remove(0);
			
			if (Picard.getVal(n.getI(), n.getJ()).equals("C")) {
				noOfOpenNodes = open.size();
				noOfVisitedNodes = visited.size();
				return n;
			}
			boolean found = false;
			for (int i=0; i < visited.size(); i++) {	/* Union */		
				if (n.equals(visited.get(i))) {					
					found = true;
					break;
				}
			}
			if (!found)
				visited.add(n);
			
			ArrayList<Node> succ = n.getSucc();
			for (int i=0; i < succ.size(); i++) {
				found = false;
				for (int j=0; j < visited.size(); j++) {
					if (succ.get(i).equals(visited.get(j))) {
						found = true;
						break;
					}				
				}
				if (!found)
					open.add(succ.get(i));
			}
			Collections.sort(open);
		}
		
		noOfOpenNodes = open.size();
		noOfVisitedNodes = visited.size();
		return null;
	}
	
	public static int calcHeuristic1(int i, int j) {		
		return manhattanDist(i, j, goalI, goalJ);
	}
	
	public Node heuristicAlgorithm() {
		
		ArrayList<HeuristicNode> open = new ArrayList<HeuristicNode>();
		ArrayList<HeuristicNode> closed = new ArrayList<HeuristicNode>();
		open.add(hstart);
		
		while (!open.isEmpty()) {
			HeuristicNode n = open.remove(0);
			
			if (Picard.getVal(n.getI(), n.getJ()).equals("C")) {
				noOfOpenNodes = open.size();
				noOfVisitedNodes = closed.size();
				return n;
			}
			boolean found = false;
			for (int i=0; i < closed.size(); i++) { /* Union */
				if (n.equals(closed.get(i))) {
					found = true;
					break;
				}
			}
			if (!found)
				closed.add(n);
			ArrayList<HeuristicNode> succ = n.gethSucc();
			for (int i=0; i < succ.size(); i++) {
				found = false;
				for (int j=0; j < closed.size(); j++) { /* if neki m' element closed */
					if ( closed.get(j).getI() == succ.get(i).getI() && closed.get(j).getJ() == succ.get(i).getJ() ) // state m = state m'/*(closed.get(j).getVal().equals(succ.get(i).getVal()))*/
							if (closed.get(j).getCost() <= succ.get(i).getCost()) {
								found = true;
								break;
							} else {
								closed.remove(j);
								j--; // potrebna modifikacija jer remove shifta sve naknadne elemente ulijevo
									// idući element će se shiftat na upravo ovo J mjesto, tak da ovaj j-- poništava j++ na kraju fora								
							}
				}
				if (found)
					continue;
				for (int j=0; j < open.size(); j++) { /* if neki m' element open */
					if ( open.get(j).getI() == succ.get(i).getI() && open.get(j).getJ() == succ.get(i).getJ() )/*(open.get(j).getVal().equals(succ.get(i).getVal()))*/
						if (open.get(j).getCost() <= succ.get(i).getCost()) {
							found = true;
							break;
						} else {
							open.remove(j);
							j--;
						}
				}
				if (found)
					continue;
				
				open.add(succ.get(i));		
			}
			Collections.sort(open);
		}
		noOfOpenNodes = open.size();
		noOfVisitedNodes = closed.size();
		return null;
	}
	
	public static Teleporter findClosestTeleporter(int i, int j) {
		int min = 0;
		Teleporter t = null;
		Teleporter cur_teleporter;
		for (int k=0; k < enterpriseTeleporters.size(); k++) {
			cur_teleporter = enterpriseTeleporters.get(k);
			if (k == 0) {
				min = manhattanDist(i, j, cur_teleporter.i, cur_teleporter.j);
				t = cur_teleporter;
				continue;
			}
			if (manhattanDist(i, j, cur_teleporter.i, cur_teleporter.j) < min) {
				min = manhattanDist(i, j, cur_teleporter.i, cur_teleporter.j);
				t = cur_teleporter;
			}
		}				
		return t;
	}
	
	public static int calcHeuristic2(int i, int j){		
		Teleporter closestTeleporter = findClosestTeleporter(i, j);
		if (closestTeleporter != null && teleporterClosestToGoal != null) {
			int distPtoT1 = manhattanDist(i, j, closestTeleporter.i, closestTeleporter.j);
			int distT1toT2 = manhattanDist(closestTeleporter.i, closestTeleporter.j, teleporterClosestToGoal.i, teleporterClosestToGoal.j);
			int distT2toG = manhattanDist(teleporterClosestToGoal.i, teleporterClosestToGoal.j, goalI, goalJ);
			if (j < border)
				return distPtoT1 + distT1toT2 + distT2toG;
			else
				return manhattanDist(i, j, goalI, goalJ);
		}
		else
			return manhattanDist(i, j, goalI, goalJ);
	}

	public static void main(String[] args) {		
		File file = new File("example6.txt"); //TODO
		BufferedReader br = null;
		String line;
		Picard picard = null;
		enterpriseTeleporters = new ArrayList<Teleporter>();
		eladrelTeleporters = new ArrayList<Teleporter>();
		
		try {
			br = new BufferedReader(new FileReader(file));			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			boolean init = false;
			String[][] _map = null;
			int i = 0;
			while ((line = br.readLine()) != null) {
                String delims = "[ ]+";			/* parsiranje stringa */
                String[] tokens = line.split(delims);                
                if (init == false) {
                	_map = new String[tokens.length][tokens.length];
                	border = tokens.length/2;                	
                	init = true;                	
                }               
                for (int j=0; j < tokens.length; j++) {
                	_map[i][j] = tokens[j];
                	if (_map[i][j].startsWith("T")) {
                		if (j < border)
                			enterpriseTeleporters.add(new Teleporter(i, j));
                		else
                			eladrelTeleporters.add(new Teleporter(i, j));                			
                	}
                	if (_map[i][j].equals("C")) {
                		goalI = i;
                		goalJ = j;
                	}                	
                }
                i++;
			}
			int min = 0;
			for (i=0; i < eladrelTeleporters.size(); i++) {
				Teleporter cur_teleporter = eladrelTeleporters.get(i);
				if (i == 0) {
					min = manhattanDist(cur_teleporter.i, cur_teleporter.j,
									goalI, goalJ);
					teleporterClosestToGoal = cur_teleporter;
					continue;
				}
				if (manhattanDist(cur_teleporter.i, cur_teleporter.j, goalI, goalJ) < min) {
					min = manhattanDist(cur_teleporter.i, cur_teleporter.j, goalI, goalJ);
					teleporterClosestToGoal = cur_teleporter;					
				}				
			}
			picard = new Picard(_map);
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		picard.printMap();

		Node n = picard.blindAlgorithm();
		System.out.println("\nBlind Algorithm:");
		if (n != null)
			System.out.println("Minimal cost: " + n.getCost());
//		System.out.println("Open nodes: " + picard.getNoOfOpenNodes());
		System.out.println("Opened nodes: " + picard.getNoOfVisitedNodes());
		System.out.println("Path:");
		if (n != null)
			n.path();
		
		Picard.setHeuristicMode(1); // odaberi 1. heuristiku
		n = picard.heuristicAlgorithm();
		System.out.println("\nHeuristic 1 Algorithm:");
		if (n != null)
			System.out.println("Minimal cost: " + n.getCost());
//		System.out.println("Open nodes: " + picard.getNoOfOpenNodes());
		System.out.println("Opened nodes: " + picard.getNoOfVisitedNodes());
		System.out.println("Path:");
		if (n != null)
			n.path();
		System.out.println();
		
		Picard.setHeuristicMode(2); // odaberi 2. heuristiku
		n = picard.heuristicAlgorithm();
		System.out.println("\nHeuristic 2 Algorithm:");
		if (n != null)
			System.out.println("Minimal cost: " + n.getCost());
//		System.out.println("Open nodes: " + picard.getNoOfOpenNodes());
		System.out.println("Opened nodes: " + picard.getNoOfVisitedNodes());
		System.out.println("Path:");
		if (n != null)
			n.path();

	}
	
}

class Node implements Comparable<Node> {
	
	private int i;
	private int j;	
	private int cost;
	private Node parent;
	
	public Node(int _i, int _j, int _cost, Node _parent) throws Exception {
		if ( _i < 0 || _j < 0 || _i >= Picard.maxdim() || _j >= Picard.maxdim() ) // out of bounds
			throw new Exception();		
		i = _i;
		j = _j;	
		cost = _cost;
		parent = _parent;
	}
	
	public ArrayList<Node> getSucc() {
		ArrayList<Node> succ = new ArrayList<Node>();

		try{ /* move down,right,up,left */
			succ.add(new Node(i+1, j, cost + Picard.calcWeight(i, j, i+1, j), this));
		} catch (Exception e) {}
		try{
			succ.add(new Node(i, j+1, cost + Picard.calcWeight(i, j, i, j+1), this));
		} catch (Exception e) {}
		try {
			succ.add(new Node(i, j-1, cost + Picard.calcWeight(i, j, i, j-1), this));
		} catch (Exception e) {}
		try {			
			succ.add(new Node(i-1, j, cost + Picard.calcWeight(i, j, i-1, j), this));
		} catch (Exception e) {}	
		
		if (Picard.getVal(i, j).equals("SS")) {
			for (int x=Picard.maxdim()/2; x < Picard.maxdim(); x++) {
				for (int y=0; y < Picard.maxdim(); y++) {
					if (Picard.getVal(x, y).equals("SL"))
						try {
							succ.add(new Node(x, y, cost + 3*Picard.manhattanDist(i, j, x, y), this));
						} catch (Exception e) {							
						}
				}
			}			
		} else if (Picard.getVal(i, j).startsWith("T")) {
			for (int x=0; x < Picard.maxdim(); x++)
				for (int y=0; y < Picard.maxdim(); y++)
					if (Picard.getVal(i, j).equals(Picard.getVal(x, y)))
						try {
							succ.add(new Node(x, y, cost + Picard.manhattanDist(i, j, x, y), this));
						} catch (Exception e) {							
						}
		}
		return succ;
	}	
	
	@Override
	public String toString() {
		return "(" + (i+1) + ", " + (j+1) + ") = " + cost;
	}

	@Override
	public int compareTo(Node other) {		
		if (other.cost <= cost)
			return 1;
		else
			return -1;
	}

	public boolean equals(Node other) {
		if (i == other.i && j == other.j)
			return true;
		else
			return false;
	}
	
	public int getI(){
		return i;
	}
	public int getJ(){
		return j;
	}
	public int getCost() {
		return cost;
	}
	public Node getParent() {
		return parent;
	}
	
	public void path() {
		if (parent == null) {
			System.out.println(this);
			return;
		}		
		parent.path();
		System.out.println(this);
	}
}

class HeuristicNode extends Node{

	int heuristicCost;
	
	public HeuristicNode(int _i, int _j, int _cost, Node _parent) throws Exception {
		super(_i, _j, _cost, _parent);
		if (Picard.getHeuristicMode() == 1)
			heuristicCost = Picard.calcHeuristic1(_i, _j);
		else
			heuristicCost = Picard.calcHeuristic2(_i, _j);
	}
	
	public ArrayList<HeuristicNode> gethSucc() {
		ArrayList<Node> succ = getSucc();
		ArrayList<HeuristicNode> hsucc = new ArrayList<HeuristicNode>();
		for (int i=0; i < succ.size(); i++) {
			Node cur_node = succ.get(i);
			try {
				hsucc.add(new HeuristicNode(cur_node.getI(), cur_node.getJ(),
							cur_node.getCost(), cur_node.getParent()));
			} catch (Exception e) {}
		}
		return hsucc;
	}
	
	public String getVal() {
		return Picard.getVal(getI(), getJ());
	}
	
	@Override
	public String toString() {
		return super.toString() + " (" + heuristicCost + ")";
	}
	
	@Override
	public int compareTo(Node _other) {
		HeuristicNode other = (HeuristicNode) _other;
		if (other.getCost() + other.heuristicCost <= getCost() + heuristicCost)
			return 1;
		else
			return -1;
	}
	
}

class Teleporter {
	public int i;
	public int j;
	
	public Teleporter(int _i, int _j) {
		i = _i;
		j = _j;
	}
	
	@Override
	public String toString(){
		return "(" + (i+1) + ", " + (j+1) + ")";
	}
}
