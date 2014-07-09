package demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import lab01.pomlib.IVector;
import lab01.pomlib.Vector;

public class ZD5 {

	public static void main(String[] args) {
		File file = new File("kocka.obj");
//		File file = new File("tetrahedron.obj");
		BufferedReader br = null;
		String line = null;
		
		try {
			br = new BufferedReader(new FileReader(file));
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}

		ObjectModel obj = new ObjectModel();
		
		String delims = "[ ]+";
		String[] tokens; 
		
		try {
			while ((line = br.readLine()) != null) {
//				System.out.println(line);
				tokens = line.split(delims);
				if (tokens.length < 1)
					continue;
				if (tokens[0].equals("v")) {
					if (tokens.length != 4) {
						System.out.println("Invalid line: " + line);
						continue;
					}
					float x = Float.parseFloat(tokens[1]);
					float y = Float.parseFloat(tokens[2]);
					float z = Float.parseFloat(tokens[3]);
					obj.addVertex(x, y, z);
				} else if (tokens[0].equals("f")) {
					if (tokens.length != 4) {
						System.out.println("Invalid line: " + line);
						continue;
					}
					int index0 = Integer.parseInt(tokens[1]);
					int index1 = Integer.parseInt(tokens[2]);
					int index2 = Integer.parseInt(tokens[3]);
					obj.addFace(index0, index1, index2);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		System.out.println(obj);
		
		obj.calc();
//		ArrayList<PlaneEquation> equations = obj.getEquations();
//		for (int i=0; i < equations.size(); i++)
//			System.out.println(equations.get(i));
//		ObjectModel obj2 = obj.copy();
//		Vertex3D vertex = obj2.get3rdVertex();
//		vertex.setX(5);
//		Face3D face = obj2.get1stFace();
//		face.set1stIndex(20);
//		System.out.println("OBJ2");
//		System.out.println(obj2);
//		System.out.println("OBJ");
//		System.out.println(obj);
		
//		System.out.println();
//		Vertex3D v = new Vertex3D(0, 0, 0);
//		boolean isInside = false;
//		try {
////			isInside = obj.isInside(v);
////			if (isInside)
////				System.out.println("Tocka " + v + " nalazi se unutar tijela");
////			else
////				System.out.println("Tocka " + v + " nalazi se izvan tijela");
//			v = new Vertex3D(0.5f, 0, 0.5f);
//			isInside = obj.isInside(v);
//			if (isInside)
//				System.out.println("Tocka " + v + " nalazi se unutar tijela");
//			else
//				System.out.println("Tocka " + v + " nalazi se izvan tijela");
//			v = new Vertex3D(2, 2, 2);
//			isInside = obj.isInside(v);
//			if (isInside)
//				System.out.println("Tocka " + v + " nalazi se unutar tijela");
//			else
//				System.out.println("Tocka " + v + " nalazi se izvan tijela");
//		} catch(Exception e) {
//			System.out.println("Tocka " + v + " nalazi se na obodu tijela");
//		}

//		obj.normalize();
//		System.out.println(obj);
		
		String input = "";
		br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			while (!(input).equals("quit")) {
				input = br.readLine();
				if (input.equals("normiraj")) {
					obj.normalize();
					System.out.println(obj);
				} else if (input.equals("objekt"))
					System.out.println(obj);
				else if (input.equals("jednadzbe")) {
					ArrayList<PlaneEquation> equations = obj.getEquations();
					for (int i=0; i < equations.size(); i++)
						System.out.println(equations.get(i));
				}
				else {
				
					tokens = input.split(delims);
					if (tokens.length != 3)
						continue;
					float x, y ,z;
					try {
						x = Float.parseFloat(tokens[0]);
						y = Float.parseFloat(tokens[1]);
						z = Float.parseFloat(tokens[2]);
					} catch (NumberFormatException nfe) {
						continue;
					}
					Vertex3D v = new Vertex3D(x, y, z);
					try {
						boolean isInside = obj.isInside(v);
						if (isInside)
							System.out.println("Tocka " + v + " nalazi se unutar tijela");
						else
							System.out.println("Tocka " + v + " nalazi se izvan tijela");
					} catch(Exception e) {
						System.out.println("Tocka " + v + " nalazi se na obodu tijela");
					}					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

class PlaneEquation {
	private double a;
	private double b;
	private double c;
	private double d;
	
	public PlaneEquation(double _a, double _b, double _c, double _d) {
		a = _a;
		b = _b;
		c = _c;
		d = _d;
	}
	
	public double getA() {
		return a;
	}
	public double getB() {
		return b;
	}
	public double getC() {
		return c;
	}
	public double getD() {
		return d;
	}
	
	public boolean equals(PlaneEquation other) {
		if (a == other.getA() && b == other.getB()
				&& c == other.getC() && d == other.getD())
			return true;
		else
			return false;				 
	}
	
	@Override
	public String toString() {
		return "" + a + "x + " + b + "y + " + c + "z + " + d + " = 0";
	}
}

class Vertex3D {
	private float x;
	private float y;
	private float z;
	private float r;
	private float g;
	private float b;
	private IVector normal;
	
	public Vertex3D(float _x, float _y, float _z) {
		x = _x;
		y = _y;
		z = _z;
		r = (float) Math.random();
		g = (float) Math.random();
		b = (float) Math.random();
	}
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getZ() {
		return z;
	}
	public float getR() {
		return r;
	}
	public float getG() {
		return g;
	}
	public float getB() {
		return b;
	}
	public void setX(float _x) {
		x = _x;
	}
	public void setY(float _y) {
		y = _y;		
	}
	public void setZ(float _z) {
		z = _z;
	}
	public void setNormal(IVector n) {
		normal = n;
	}
	public IVector getNormal() {
		return normal;
	}
		
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	
	public boolean equals(Vertex3D other) {
		if (other.getX() == x && other.getY() == y
				&& other.getZ() == z)
			return true;
		else
			return false;			
	}
}

class Face3D {
	private int[] indexes = new int[3];
	private boolean visible = true;
	private double a;
	private double b;
	private double c;
	private double d;
	private IVector normal;
	
	public Face3D(int index0, int index1, int index2) {
		indexes[0] = index0;
		indexes[1] = index1;
		indexes[2] = index2;
	}
	
	public Face3D(int[] _indexes) {
		indexes[0] = _indexes[0];
		indexes[1] = _indexes[1];
		indexes[2] = _indexes[2];
	}	
	
	public int[] getIndexes() {
		return indexes;
	}
	
	public void setEquation(double _a, double _b, double _c, double _d) {
		a = _a;
		b = _b;
		c = _c;
		d = _d;
		normal = new Vector(a, b, c);
	}
	
	public double getA() {
		return a;
	}
	
	public double getB() {
		return b;
	}
	
	public double getC() {
		return c;
	}
	
	public double getD() {
		return d;
	}
	public IVector getNormal() {
		return normal;
	}
	
	public void setVisibility(boolean b) {
		visible = b;
	}
	
	public boolean isVisible() {
		return visible;
	}
}

class ObjectModel {
	private ArrayList<Vertex3D> vertices;
	private ArrayList<Face3D> faces;
	private ArrayList<PlaneEquation> equations;
	
	public ObjectModel() {
		vertices = new ArrayList<Vertex3D>();
		faces = new ArrayList<Face3D>();
		equations = new ArrayList<PlaneEquation>();
	}
	
	public ObjectModel(ArrayList<Vertex3D> _vertices, ArrayList<Face3D> _indexes, ArrayList<PlaneEquation> _equations) {
		vertices = _vertices;
		faces = _indexes;
		equations = _equations;		
	}
	
	public void addVertex(float x, float y, float z) {
		vertices.add(new Vertex3D(x, y, z));
	}	
	public void addFace(int index0, int index1, int index2) {
		faces.add(new Face3D(index0, index1, index2));
	}
	
	public ArrayList<Face3D> getFaces() {
		return faces;
	}
	
	public ArrayList<Vertex3D> getVertices() {
		return vertices;
	}
		
	public void calc() {
		equations.clear();
		for (int i=0; i < faces.size(); i++) {
			Vertex3D Vi = vertices.get(faces.get(i).getIndexes()[0] - 1); // faces pamti indexe od 1 pa nadalje
			Vertex3D Vi1 = vertices.get(faces.get(i).getIndexes()[1] - 1);
			Vertex3D Vi2 = vertices.get(faces.get(i).getIndexes()[2] - 1);
			
			IVector vec1 = new Vector(Vi1.getX() - Vi.getX(), Vi1.getY() - Vi.getY(), Vi1.getZ() - Vi.getZ());
			IVector vec2 = new Vector(Vi2.getX() - Vi.getX(), Vi2.getY() - Vi.getY(), Vi2.getZ() - Vi.getZ());
			IVector norm = null;
			try {
				norm = vec1.nVectorProduct(vec2);
				norm.normalize();
//				norm = vec2.nVectorProduct(vec1);
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			double a = norm.get(0);
			double b = norm.get(1);
			double c = norm.get(2);
			double d = -a*Vi.getX() - b*Vi.getY() - c*Vi.getZ();
			
			faces.get(i).setEquation(a, b, c, d);
			
			PlaneEquation eq = new PlaneEquation(a, b, c, d);
			boolean found = false;
			for (int j=0; j < equations.size(); j++)
				if (eq.equals(equations.get(j))) {
					found = true;
					break;
				}
			if (!found)
				equations.add(eq);

		}
	}
	
	public boolean isInside(Vertex3D vertex) throws Exception {
		double a, b, c, d, r;
		float x = vertex.getX();
		float y = vertex.getY();
		float z = vertex.getZ();
		boolean mightBeOn = false;
		for (int i=0; i < equations.size(); i++) {			
			a = equations.get(i).getA();
			b = equations.get(i).getB();
			c = equations.get(i).getC();
			d = equations.get(i).getD();
			r = a*x + b*y + c*z + d;
			if (r == 0) /* nalazi se na bridu */
				mightBeOn = true;
			if (r > 0) // TODO
				return false;
//			if (r < 0)
//				return false;
		}
		if (mightBeOn)
			throw new Exception();
		return true;
	}
	
	public ObjectModel copy() {
		ArrayList<Vertex3D> _vertices = new ArrayList<Vertex3D>();
		ArrayList<Face3D> _indexes = new ArrayList<Face3D>(); 
		ArrayList<PlaneEquation> _equations = new ArrayList<PlaneEquation>();
		
		for (int i=0; i < vertices.size(); i++)
			_vertices.add(new Vertex3D(vertices.get(i).getX(), vertices.get(i).getY()
							, vertices.get(i).getZ()));
		for (int i=0; i < faces.size(); i++)
			_indexes.add(new Face3D(faces.get(i).getIndexes()));
		for (int i=0; i < equations.size(); i++)
			_equations.add(new PlaneEquation(equations.get(i).getA(), equations.get(i).getB()
							, equations.get(i).getC(), equations.get(i).getD()));
					
		return new ObjectModel(_vertices, _indexes, _equations);
	}
	
	public String dumpToOBJ() {
		String string = "";
		for (int i=0; i < vertices.size(); i++)
			string += "v " + vertices.get(i).getX() + " "
					+ vertices.get(i).getY() + " " 
					+ vertices.get(i).getZ() + "\n";
		for (int i=0; i < faces.size(); i++) {
			string += "f " + faces.get(i).getIndexes()[0] + " "
					+ faces.get(i).getIndexes()[1] + " "
					+ faces.get(i).getIndexes()[2];
			if (i != faces.size()-1)
				string += "\n";
		}
		
		return string;
	}
		
	public void normalize() {
		if (vertices.size() == 0)
			return;
		
		float xmax, xmin, ymax, ymin, zmax, zmin;
		xmax = xmin = vertices.get(0).getX();
		ymax = ymin = vertices.get(0).getY();
		zmax = zmin = vertices.get(0).getZ();
		
		for (int i=1; i < vertices.size(); i++) {
			if (vertices.get(i).getX() > xmax)
				xmax = vertices.get(i).getX();
			if (vertices.get(i).getX() < xmin)
				xmin = vertices.get(i).getX();
			if (vertices.get(i).getY() > ymax)
				ymax = vertices.get(i).getY();
			if (vertices.get(i).getY() < ymin)
				ymin = vertices.get(i).getY();
			if (vertices.get(i).getZ() > zmax)
				zmax = vertices.get(i).getZ();
			if (vertices.get(i).getZ() < zmin)
				zmin = vertices.get(i).getZ();			
		}
		
		float xmid = (xmin + xmax) / 2;
		float ymid = (ymin + ymax) / 2;
		float zmid = (zmin + zmax) / 2;
		
		for(int i=0; i < vertices.size(); i++) {
			float x = vertices.get(i).getX();
			float y = vertices.get(i).getY();
			float z = vertices.get(i).getZ();
			vertices.get(i).setX(x - xmid);
			vertices.get(i).setY(y - ymid);
			vertices.get(i).setZ(z - zmid);
		}
		
		float m1 = xmax - xmin;
		float m2 = ymax - ymin;
		float m3 = zmax - zmin;
		float M = m1;
		if (m2 > M)
			M = m2;
		if (m3 > M)
			M = m3;
		
		for (int i=0; i < vertices.size(); i++) {
			float x = vertices.get(i).getX();
			float y = vertices.get(i).getY();
			float z = vertices.get(i).getZ();
			vertices.get(i).setX(x * (2/M));
			vertices.get(i).setY(y * (2/M));
			vertices.get(i).setZ(z * (2/M));
		}
		calc();
	}
	
	public ArrayList<PlaneEquation> getEquations() {
		return equations;
	}
	
	void determineFaceVisibilities1(IVector eye) throws Exception {
		IVector e = new Vector(eye.get(0), eye.get(1), eye.get(2), 1);
		for (int i=0; i < faces.size(); i++) {
			IVector eq = new Vector(faces.get(i).getA(), faces.get(i).getB(),
						faces.get(i).getC(), faces.get(i).getD());
			double result = e.scalarProduct(eq);
			if (result > 0)
				faces.get(i).setVisibility(true);
			else
				faces.get(i).setVisibility(false);
		}
	}
	
	void determineFaceVisibilities2(IVector eye) throws Exception {
		for (int i=0; i < faces.size(); i++) {
			Vertex3D Vi1 = vertices.get(faces.get(i).getIndexes()[0] - 1);
			Vertex3D Vi2 = vertices.get(faces.get(i).getIndexes()[1] - 1);
			Vertex3D Vi3 = vertices.get(faces.get(i).getIndexes()[2] - 1);
			IVector V1 = new Vector(Vi1.getX(), Vi1.getY(), Vi1.getZ());
			IVector V2 = new Vector(Vi2.getX(), Vi2.getY(), Vi2.getZ());
			IVector V3 = new Vector(Vi3.getX(), Vi3.getY(), Vi3.getZ());
			IVector c = V1.nAdd(V2).nAdd(V3).nScalarMultiply((float) 1/3);
			IVector e = eye.nSub(c);
			IVector n = new Vector(faces.get(i).getA(), faces.get(i).getB(), faces.get(i).getC());
			
			double result = n.scalarProduct(e);
			
			if (result > 0)
				faces.get(i).setVisibility(true);
			else
				faces.get(i).setVisibility(false);						
		}
	}
	
	public void normalAverages() {
		for (int i=0; i < vertices.size(); i++) {
			IVector n = new Vector(0, 0, 0);
			int count = 0;
			Vertex3D curVertex = vertices.get(i);
			for (int j=0; j < faces.size(); j++) {
				Face3D curFace = faces.get(j);
				if (curVertex.equals(vertices.get(curFace.getIndexes()[0] - 1))
						|| curVertex.equals(vertices.get(curFace.getIndexes()[1] - 1))
						|| curVertex.equals(vertices.get(curFace.getIndexes()[2] - 1))) {
					try {
						n.add(curFace.getNormal());
					} catch (Exception e) {
						e.printStackTrace();
					}
					count++;
				}
			}
			try {
				n.scalarMultiply(1f / count);
			} catch (Exception e) {
				e.printStackTrace();
			}
			curVertex.setNormal(n);
		}
	}
		
	@Override
	public String toString() {
		return dumpToOBJ();
	}
}
