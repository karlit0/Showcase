package demo;

import lab01.pomlib.IMatrix;
import lab01.pomlib.IVector;
import lab01.pomlib.Matrix;

public class IRG {

	public static IMatrix translate3D(float dx, float dy, float dz) throws Exception {
		return Matrix.parseSimple("1 0 0 0 | 0 1 0 0 "
				+ "| 0 0 1 0 | " + dx + " " + dy + " " + dz + " 1");
	}
	
	public static IMatrix scale3D(float sx, float sy, float sz) throws Exception {
		return Matrix.parseSimple(sx + " 0 0 0 | 0 " + sy + " 0 0"
				+ "| 0 0 " + sz + " 0 | 0 0 0 1");
	}
	
	public static IMatrix lookAtMatrix(IVector eye, IVector center, IVector viewUp) throws Exception {
		IVector f = center.sub(eye).normalize();
		IVector u = viewUp.normalize();
		IVector s = f.nVectorProduct(u).normalize();
		u = s.nVectorProduct(f);
		
		IMatrix mat = Matrix.parseSimple(s.get(0) + " " + u.get(0) + " " + f.get(0)*(-1) + " 0"
				+ "| " + s.get(1) + " " + u.get(1) + " " + f.get(1)*(-1) + " 0"
				+ "| " + s.get(2) + " " + u.get(2) + " " + f.get(2)*(-1) + " 0"
				+ "| 0 0 0 1");
		IMatrix trans = IRG.translate3D((float) eye.get(0)*(-1), (float) eye.get(1)*(-1), (float) eye.get(2)*(-1));
		return trans.nMultiply(mat);
	}
	
	public static IMatrix buildFrustumMatrix(double l, double r, double b, double t, int n, int f) throws Exception {
		IMatrix mat = Matrix.parseSimple((2*n)/(r-l) + " 0 0 0"
				+ "| 0 " + (2*n)/(t-b) + " 0 0 "
				+ "| " + (r+l)/(r-l) + " " + (t+b)/(t-b) + " " + (-1)*(f+n)/(f-n) + " -1"
				+ "| 0 0 " + (-2*f*n)/(f-n) + " 0");		
		return mat;
	}
	
	public static boolean isAntiClockwise(IVector tvs[]) {
		iPolyElem polyEl[] = new iPolyElem[3];
		double V1x = tvs[0].get(0);
		double V1y = tvs[0].get(1);
		polyEl[0] = new iPolyElem(new iTocka2D(V1x, V1y));
		double V2x = tvs[1].get(0);		
		double V2y = tvs[1].get(1);
		polyEl[1] = new iPolyElem(new iTocka2D(V2x, V2y));
		double V3x = tvs[2].get(0);
		double V3y = tvs[2].get(1);
		polyEl[2] = new iPolyElem(new iTocka2D(V3x, V3y));
		
		int i0 = polyEl.length - 1; 
		
		for (int i=0; i < polyEl.length; i++) {
			iPolyElem poleli0 = polyEl[i0];
			iPolyElem poleli = polyEl[i];
			poleli0.brid.a = poleli0.vrh.y - poleli.vrh.y;
			poleli0.brid.b = -(poleli0.vrh.x - poleli.vrh.x);
			poleli0.brid.c = poleli0.vrh.x * poleli.vrh.y
							- poleli0.vrh.y * poleli.vrh.x;
			i0 = i;
		}
		
		int ispod, iznad;
		double r;
		boolean clockwiseOrientation = true;
		ispod = iznad = 0;
		iPolyElem poleli, poleli0;
		i0 = polyEl.length-2;
		for (int i=0; i < polyEl.length; i++, i0++) {
			if (i0 >= polyEl.length)
				i0 = 0;
			poleli0 = polyEl[i0];
			poleli = polyEl[i];
			r = poleli0.brid.a * poleli.vrh.x + poleli0.brid.b * poleli.vrh.y + poleli0.brid.c;
			if (r > 0) iznad++;
			else if (r < 0) ispod++;
		}
		if (ispod == 0) {
			clockwiseOrientation = false;
		}
		else if (iznad == 0) {
			clockwiseOrientation = true;
		}
			 		
		return !clockwiseOrientation;		
	}
}
