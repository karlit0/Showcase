package lab01.pomlib;

public class Sphere extends SceneObject {

	IVector center;
	double radius;
	
	@Override
	public void updateIntersection(Intersection inters, IVector start, IVector d) {
	}

	@Override
	public IVector getNormalInPoint(IVector point) {
		return null;
	}

}
