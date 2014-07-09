package lab01.pomlib;

import java.util.Scanner;

public class ZD3 {

	public static void main(String[] args) {
		Scanner userInputScanner = new Scanner(System.in);		
		double[] x = new double[3];
		double[] y = new double[3];
		double[] z = new double[3];
		double xT;
		double yT;
		double zT;		
				
		System.out.print("Enter x coordinate of point A: ");
		x[0] = userInputScanner.nextDouble();
		System.out.print("Enter y coordinate of point A: ");
		y[0] = userInputScanner.nextDouble();
		System.out.print("Enter z coordinate of point A: ");
		z[0] = userInputScanner.nextDouble();
		
		System.out.print("Enter x coordinate of point B: ");
		x[1] = userInputScanner.nextDouble();
		System.out.print("Enter y coordinate of point B: ");
		y[1] = userInputScanner.nextDouble();
		System.out.print("Enter z coordinate of point B: ");
		z[1] = userInputScanner.nextDouble();
		
		System.out.print("Enter x coordinate of point C: ");
		x[2] = userInputScanner.nextDouble();
		System.out.print("Enter y coordinate of point C: ");
		y[2] = userInputScanner.nextDouble();
		System.out.print("Enter z coordinate of point C: ");
		z[2] = userInputScanner.nextDouble();
		
		System.out.print("Enter x coordinate of point T: ");
		xT = userInputScanner.nextDouble();
		System.out.print("Enter y coordinate of point T: ");
		yT = userInputScanner.nextDouble();
		System.out.print("Enter z coordinate of point T: ");
		zT = userInputScanner.nextDouble();
		
		userInputScanner.close();
		
		double[] array = new double[3];
		IVector a = null;
		IVector b = null;
		IVector c = null;
		IVector t = null;
		for (int i = 0; i < 3; i++) {
			array[0] = x[i];
			array[1] = y[i];
			array[2] = z[i];
			if (i == 0)
				a = new Vector(array);
			else if (i == 1)
				b = new Vector(array);
			else
				c = new Vector (array);
		}
		
		array[0] = xT;
		array[1] = yT;
		array[2] = zT;
		t = new Vector(array);
		
		System.out.println("a:\n" + a);
		System.out.println("b:\n" + b);
		System.out.println("c:\n" + c);
		System.out.println("t:\n" + t);
		
		double pov = 0;
		double povA = 0;
		double povB = 0;
		double povC = 0;
		try {
		pov = b.nSub(a).nVectorProduct(c.nSub(a)).norm() / 2.0;
		povA = b.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
		povB = a.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
		povC = a.nSub(t).nVectorProduct(b.nSub(t)).norm() / 2.0;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		double t1 = povA / pov;
		double t2 = povB / pov;
		double t3 = povC / pov;
		
		System.out.println("Baricentricne koordinate su: ("+t1+","+t2+","+t3+").");
	}
}