package lab01.pomlib;

public abstract class SceneObject {
	// parametri prednje strane objekta
	double[] fambRGB;
	double[] fdifRGB;
	double[] frefRGB;
	double fn;
	double fkref;
	
	// parametri straznje strane objekta
	double[] bambRGB;
	double[] bdifRGB;
	double[] brefRGB;
	double bn;
	double bkref;
	
	public abstract void updateIntersection(Intersection inters, IVector start, IVector d);
	public abstract IVector getNormalInPoint(IVector point);
}
