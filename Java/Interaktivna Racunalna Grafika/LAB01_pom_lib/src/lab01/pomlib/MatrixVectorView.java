package lab01.pomlib;

public class MatrixVectorView extends AbstractMatrix{

	private boolean asRowMatrix;
	private IVector originalVector;
	
	public MatrixVectorView(IVector _originalVector, boolean _asRowMatrix) {
		asRowMatrix = _asRowMatrix;				
		originalVector = _originalVector;
	}
	
	@Override
	public int getRowsCount() {
		if (asRowMatrix == true)
			return 1;
		else
			return originalVector.getDimension();
	}

	@Override
	public int getColsCount() {
		if (asRowMatrix == true)
			return originalVector.getDimension();
		else
			return 1;
	}

	@Override
	public double get(int i, int j) {
		if (asRowMatrix == true) {
			return originalVector.get(j);
		} else
			return originalVector.get(i);
	}

	@Override
	public IMatrix set(int i, int j, double val) throws Exception {
		if (asRowMatrix == true)
			originalVector.set(j, val);
		else
			originalVector.set(i, val);
		return this;
	}

	@Override
	public IMatrix copy() throws Exception {
		return new MatrixVectorView(originalVector, asRowMatrix);
	}

	@Override
	public IMatrix newInstance(int rows, int cols) throws Exception {		
		return new Matrix(rows, cols);
	}
}
