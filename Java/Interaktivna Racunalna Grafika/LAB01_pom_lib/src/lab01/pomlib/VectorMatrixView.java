package lab01.pomlib;

public class VectorMatrixView extends AbstractVector{

	private int dimension;
	private boolean rowMatrix;
	private IMatrix originalMatrix;
	
	public VectorMatrixView(IMatrix _originalMatrix) throws Exception {
		if (_originalMatrix.getRowsCount() == 1) {
			rowMatrix = true;
			dimension = _originalMatrix.getColsCount();
		} else if (_originalMatrix.getColsCount() == 1) {
			rowMatrix = false;
			dimension = _originalMatrix.getRowsCount();
		} else
			throw new Exception();
		originalMatrix = _originalMatrix;
	}

	@Override
	public double get(int i) {
		if (rowMatrix == true)
			return originalMatrix.get(0, i);
		else
			return originalMatrix.get(i, 0);		
	}

	@Override
	public IVector set(int i, double val) throws Exception {
		if (rowMatrix == true)
			originalMatrix.set(0, i, val);
		else
			originalMatrix.set(i, 0, val);
		return this;
	}

	@Override
	public int getDimension() {
		return dimension;
	}

	@Override
	public IVector copy() throws Exception {
		return new VectorMatrixView(originalMatrix);
	}

	@Override
	public IVector newInstance(int dim) {
		double[] array = new double[dim];
		return new Vector(array);
	}			
}
