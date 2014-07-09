package lab01.pomlib;

import java.nio.file.Path;

public class RTScene {

	// parametri iz datoteke
	IVector eye;
	IVector view;
	IVector viewUp;
	double h;
	double xAngle;
	double yAngle;
	double[] gaIntesnity = new double[] {0, 0, 0};
	Light[] sources;
	SceneObject[] objects;
	
	// izracunati parametri
	IVector xAxis;
	IVector yAxis;
	double l;
	double r;
	double b;
	double t;
	
	// metoda koja racuna xAxis, yAxis, l, r, b i t
	private void computeKS() {		
	}
		
	// metoda koja ucitava scenu i na kraju poziva computeKS();
	public static RTScene ucitajScenu(Path path) {
		return null;
	}
	
}
