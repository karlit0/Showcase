package lab01.pomlib;

public class Vector extends AbstractVector {

	private double[] elements;
	private int dimension;
	private boolean readOnly = false;
	
	public Vector(double ... arg) {
		dimension = arg.length;
		elements = new double[dimension];
		for (int i = 0; i < dimension; i++) {
			elements[i] = arg[i];
		}		
	}
	
	public Vector(boolean _readOnly, boolean canUseArray, double[] array) {
		readOnly = _readOnly;
		if (canUseArray == true)
			elements = array;
		else {
			elements = new double[array.length];
			for (int i = array.length-1; i >= 0; i--)
				elements[i] = array[i];
		}			
	}

	@Override
	public double get(int i) {
		return elements[i];
	}

	@Override
	public IVector set(int i, double val) throws Exception {
		if (readOnly == true)
			throw new Exception();
		else {
			elements[i] = val;
			return this;
		}
	}

	@Override
	public int getDimension() {
		return dimension;
	}

	@Override
	public IVector copy() {
		return new Vector(elements);
	}

	@Override
	public IVector newInstance(int dim) {
		double[] array = new double[dim];
		return new Vector(array);
	}
	
	public static Vector parseSimple(String input){
		String[] strElements = input.trim().split("[ ]+");
		double[] array = new double[strElements.length];
		for (int i = strElements.length-1; i >= 0; i--)
			array[i] = Double.parseDouble(strElements[i]);
		return new Vector(array);
	}
}
