package lab01.pomlib;

public class test {
		
	//TODO /* postoji pogreska u zadatku na labosu, nScalarProduct - no such thing */

	public static void main(String[] args) {
//		double[] array = { 1, 2, 3 };
//		Vector vec1 = new Vector(array);
//		Vector vec2 = new Vector(4, 5, 6);
//		System.out.println("Vector 1 : " + vec1);
//		System.out.println("Vector 2 : " + vec2);	
		
//		IVector a = new Vector(-2, 4, 1);
//		IVector b = a.copyPart(2);
//		IVector c = a.copyPart(5);
//		System.out.println("Vector b : " + b.toString(6));
//		System.out.println("Vector c : " + c);	
		
//		IVector a = Vector.parseSimple("3 1 3");
//		IVector b = new Vector(-2,4,1);
//		double n = 0;
//		try {
//			n = a.nAdd(new Vector(1,1,1))
//				 .nAdd(b)
//				 .scalarProduct(new Vector(2,3,2));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(a);
//		System.out.println(b);
//		System.out.println(n);
//		System.out.println(new Vector(1).nFromHomogeneus());
//		
//		IVector a = Vector.parseSimple("1 0 0");
//		IVector b = Vector.parseSimple("5 0 0");
//		IVector c = Vector.parseSimple("3 8 0");
//		
//		IVector t = Vector.parseSimple("3 4 0");
//
//		try {
//		double pov = b.nSub(a).nVectorProduct(c.nSub(a)).norm() / 2.0;
//		double povA = b.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
//		double povB = a.nSub(t).nVectorProduct(c.nSub(t)).norm() / 2.0;
//		double povC = a.nSub(t).nVectorProduct(b.nSub(t)).norm() / 2.0;
//		
//		double t1 = povA / pov;
//		double t2 = povB / pov;
//		double t3 = povC / pov;
//		
//		System.out.println("Baricentricne koordinate su: ("+t1+","+t2+","+t3+").");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		try {
//		IMatrix a = Matrix.parseSimple("3 5 | 2 10");
//		IMatrix r = Matrix.parseSimple("2 | 8");
//		
//		System.out.println(a);
//		System.out.println(r);
		
			/* http://lavica.fesb.hr/mat1/predavanja/node26.html */
//		IMatrix a = Matrix.parseSimple("1 2 3 | 4 5 6 | 7 8 9");
//		IMatrix b = Matrix.parseSimple("1 2 0 -1 | 4 3 2 1 | 1 -1 1 -1");
//		System.out.println("a:");
//		System.out.println(a.toString(0));
//		System.out.println("b:");
//		System.out.println(b.toString(0));
//		System.out.println("a*b");
//		System.out.println(a.nMultiply(b).toString(0));
			
//		IMatrix m1 = Matrix.parseSimple("1 2 3 | 4 5 6");
//		IMatrix m2 = m1.nTraspose(true);
//		
//		System.out.println("m1:");
//		System.out.println(m1.toString());
//		System.out.println("m2:");
//		System.out.println(m2.toString());
//		System.out.println();
//		
//		m2.set(2, 1, 9);
//		
//		System.out.println("m1:");
//		System.out.println(m1);
//		System.out.println("m2");
//		System.out.println(m2);
		
//		IMatrix m1 = Matrix.parseSimple("1 2 3 | 4 5 6");
//		IMatrix m2 = m1.subMatrix(0, 2, true); //TODO malo buggy sa vrijednostima vecim od max.
//		System.out.println("m1:");
//		System.out.println(m1);
//		System.out.println("m2");
//		System.out.println(m2);
//		m2.set(0, 1, 15);
//		System.out.println("m1:");
//		System.out.println(m1);
//		System.out.println("m2");
//		System.out.println(m2);
		

//		IMatrix m1 = Matrix.parseSimple("1 2 3 4 5 6 | 7 8 9 10 11 12");
//		IMatrix m2 = m1.subMatrix(0, 2, false); //TODO malo buggy sa vrijednostima vecim od max.
//		System.out.println("m1:");
//		System.out.println(m1);
//		System.out.println("m2");
//		System.out.println(m2);
//		m2.set(0, 1, 15);
//		System.out.println("m1:");
//		System.out.println(m1);
//		System.out.println("m2");
//		System.out.println(m2);
			
//		IMatrix m1 = Matrix.parseSimple("1 2 3 | 4 5 6 | 7 8 9");
//		IMatrix m2 = m1.subMatrix(2, 2, true);
//		IMatrix m3 = m2.subMatrix(1, 1, false);
//		System.out.println("m1:");
//		System.out.println(m1);
//		System.out.println("m2");
//		System.out.println(m2);
//		System.out.println("m3");
//		System.out.println(m3);
//		m3.set(0, 0, 500);
//		System.out.println("m1:");
//		System.out.println(m1);
//		System.out.println("m2");
//		System.out.println(m2);
//		System.out.println("m3");
//		System.out.println(m3);
			
//		IMatrix m  = Matrix.parseSimple("5 10 | 10 0");
//		System.out.println("m");
//		System.out.println(m);
//		IMatrix mTransposed = m.nTraspose(false);
//		System.out.println("mTransposed\n" + mTransposed);		
//		IMatrix inverseM = m.nInvert();
//		System.out.println("inverse M");
//		System.out.println(inverseM);
			
//		IVector v = Vector.parseSimple("1 2 3");
//		System.out.println(v);
//		IMatrix m = v.toRowMatrix(true);
//		System.out.println("m:\n" + m);
//		m.set(5, 1, 50);
//		System.out.println("m:\n" + m);
//		System.out.println(v);
//		
//		IMatrix m2 = Matrix.parseSimple("10 |20| 30");
//		System.out.println("m2:\n" + m2);
//		IVector v2 = m2.toVector(false);
//		System.out.println("v2:\n" + v2);
//		
//		System.out.println(v2.add(v));
//		System.out.println("m2:\n" + m2);
		
		}catch (Exception e) {
			e.printStackTrace();
		}		
		
	}

}
