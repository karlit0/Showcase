package lab01.pomlib;

import java.util.Scanner;

public class ZD2 {

	public static void main(String[] args) {
		Scanner userInputScanner = new Scanner(System.in);		
		double[] a = new double[3];
		double[] b = new double[3];
		double[] c = new double[3];
		double[] r = new double[3];
				
		System.out.print("Enter a1: ");
		a[0] = userInputScanner.nextDouble();
		System.out.print("Enter b1: ");
		b[0] = userInputScanner.nextDouble();
		System.out.print("Enter c1: ");
		c[0] = userInputScanner.nextDouble();
		System.out.print("Enter r1: ");
		r[0] = userInputScanner.nextDouble();
		
		System.out.print("Enter a2: ");
		a[1] = userInputScanner.nextDouble();
		System.out.print("Enter b2: ");
		b[1] = userInputScanner.nextDouble();
		System.out.print("Enter c2: ");		
		c[1] = userInputScanner.nextDouble();
		System.out.print("Enter r1: ");
		r[1] = userInputScanner.nextDouble();
		
		System.out.print("Enter a3: ");
		a[2] = userInputScanner.nextDouble();
		System.out.print("Enter b3: ");
		b[2] = userInputScanner.nextDouble();
		System.out.print("Enter c3: ");
		c[2] = userInputScanner.nextDouble();
		System.out.print("Enter r3: ");
		r[2] = userInputScanner.nextDouble();
		
		userInputScanner.close();		
		double array[][] = new double[3][3];
		
		for (int i = 0; i < 3; i++)
			array[i][0] = a[i]; 
		for (int i = 0; i < 3; i++)
			array[i][1] = b[i]; 
		for (int i = 0; i < 3; i++)
			array[i][2] = c[i]; 
		
		IMatrix M = null;
		try {
			M = new Matrix(3, 3, array, false);
		} catch (Exception e) {
			e.printStackTrace();
		}		
//		System.out.println("M = \n" + M);
		
		double array2[][] = new double[3][1];
		for (int i = 0; i < 3; i++)
			array2[i][0] = r[i];
		
		IMatrix R =  null;
		try {
			R = new Matrix(3, 1, array2, false);			
		} catch (Exception e) {
			e.printStackTrace();
		}		
//		System.out.println("R = \n" + R);
		
		System.out.println(a[0] + "x + " + b[0] + "y + " + c[0] + "z = " + r[0]);
		System.out.println(a[1] + "x + " + b[1] + "y + " + c[1] + "z = " + r[1]);
		System.out.println(a[2] + "x + " + b[2] + "y + " + c[2] + "z = " + r[2]);
		
		IMatrix S = null;		
		try {			
			S = M.nInvert().nMultiply(R);
		} catch (Exception e) {
			e.printStackTrace();
		}			
		System.out.println("Rjesenje sustava je:\n" + S);
		
		
	}

}
