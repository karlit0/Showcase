package lab01.pomlib;

public class MatrixTransposeView extends AbstractMatrix{

	IMatrix originalMatrix;
	
	public MatrixTransposeView(IMatrix _originalMatrix) {
		originalMatrix = _originalMatrix;
	}
	
	@Override
	public int getRowsCount() {
		return originalMatrix.getColsCount();
	}

	@Override
	public int getColsCount() {
		return originalMatrix.getRowsCount();
	}

	@Override
	public double get(int i, int j) {
		return originalMatrix.get(j, i);
	}

	@Override
	public IMatrix set(int i, int j, double val) throws Exception {
		return originalMatrix.set(j, i, val);
	}

	@Override
	public IMatrix copy() throws Exception {
		return new MatrixTransposeView(originalMatrix);
	}

	@Override
	public IMatrix newInstance(int rows, int cols) throws Exception {
		return new Matrix(rows, cols);
	}
	
	@Override
	public double[][] toArray() {
		double[][] array = new double[this.getRowsCount()][this.getColsCount()];
		for (int i = this.getRowsCount()-1; i >= 0; i--)
			for (int j = this.getColsCount()-1; j >= 0; j--)
				array[i][j] = this.get(i, j);
		return array;
	}
}