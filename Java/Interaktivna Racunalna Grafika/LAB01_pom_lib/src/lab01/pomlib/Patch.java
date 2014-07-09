package lab01.pomlib;

public class Patch extends SceneObject {

	IVector center;
	IVector v1;
	IVector v2;
	IVector normal;
	double w;
	double h;
	
	@Override
	public void updateIntersection(Intersection inters, IVector start, IVector d) {		
	}
	
	@Override
	public IVector getNormalInPoint(IVector point) {	
		return null;
	}
}
