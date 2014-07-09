package lab01.pomlib;

public class Light {
	IVector position;
	double[] rgb;
	
	public Light(IVector pos, double[] _rgb) {
		try {
			position = pos.copy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		rgb = new double[3];
		rgb[0] = _rgb[0];
		rgb[1] = _rgb[1];
		rgb[2] = _rgb[2];
	}
}
