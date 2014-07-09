package lab01.pomlib;

public class ZD1 {

	public static void main(String[] args) {
		try	{
			IVector v1 = Vector.parseSimple("2 3 -4").nAdd(Vector.parseSimple("-1 4 -3"));		
			System.out.println("v1 = " + Vector.parseSimple("2 3 -4") +" + " + Vector.parseSimple("-1 4 -3") + " = " + v1);
			double s = v1.scalarProduct(Vector.parseSimple("-1 4 -3"));
			System.out.println("s = v1 * " + Vector.parseSimple("-1 4 -3") + " = " + s);
			IVector v2 = v1.nVectorProduct(Vector.parseSimple("2 2 4"));
			System.out.println("v2 = v1 x " + Vector.parseSimple("2 2 4") + " = " + v2);
			IVector v3 = v2.nNormalize();
			System.out.println("v3 = normalize(v2) = " + v3);
			IVector v4 = v2.nScalarMultiply(-1);
			System.out.println("v4 = -v2 = " + v4);
			IMatrix Ma = Matrix.parseSimple("1 2 3 | 2 1 3 | 4 5 1");
			IMatrix Mb = Matrix.parseSimple("-1 2 -3 | 5 -2 7 | -4 -1 3");
			IMatrix M1 = Ma.nAdd(Mb);
			System.out.println("M1 = \n" + Ma + "\n + \n" + Mb + "\n = \n" + M1);
			IMatrix M2 = Ma.nMultiply(Mb.nTraspose(true));
			System.out.println("M2 = \n" + Ma + "\n * \n" + Mb.nTraspose(true) + "\n = \n" + M2);
			IMatrix Ma2 = Matrix.parseSimple("-24 18 5 | 20 -15 -4 | -5 4 1");
			IMatrix Mb2 = Matrix.parseSimple("1 2 3 | 0 1 4 | 5 6 0");
			IMatrix M3 = Ma2.nInvert().nMultiply(Mb2.nInvert());
			System.out.println("M3 = \ninverse of\n" + Ma2 + "\n * \ninverse of\n" + Mb2 + "\n = \n" + M3);
			IMatrix a = Matrix.parseSimple("1 2 3 | 4 5 6");
			IMatrix b = Matrix.parseSimple("1 0 | 1 1 | 0 0");
			System.out.println(a.nMultiply(b));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
