package lab01.pomlib;

public class MatrixSubMatrixView extends AbstractMatrix {

	private IMatrix originalMatrix;
	private int[] rowIndexes;
	private int[] colIndexes;
	
	public MatrixSubMatrixView(IMatrix _originalMatrix, int row, int col) throws Exception {
		if (_originalMatrix.getRowsCount() == 1 || _originalMatrix.getColsCount() == 1)
			throw new Exception();		
		originalMatrix = _originalMatrix;
		rowIndexes = new int[originalMatrix.getRowsCount()-1];
		colIndexes = new int[originalMatrix.getColsCount()-1];
		int i = 0;
		int j = 0;
		while (i < rowIndexes.length) {
			if (j != row) {
				rowIndexes[i] = j;
				i++;				
			}
			j++;			
		}
		i = 0;
		j = 0;
		while (i < colIndexes.length){
			if (j != col) {
				colIndexes[i] = j;
				i++;
			}
			j++;
		}			
	}
	
	private MatrixSubMatrixView(IMatrix _originalMatrix, int[] _rowIndexes, int[] _colIndexes) {
		originalMatrix = _originalMatrix;
		rowIndexes = _rowIndexes;
		colIndexes = _colIndexes;		
	}
	
	@Override
	public int getRowsCount() {
		return rowIndexes.length;
	}

	@Override
	public int getColsCount() {
		return colIndexes.length;
	}

	@Override
	public double get(int i, int j) {
		return originalMatrix.get(rowIndexes[i], colIndexes[j]);
	}

	@Override
	public IMatrix set(int i, int j, double val) throws Exception {
		originalMatrix.set(rowIndexes[i], colIndexes[j], val);
		return this;
	}

	@Override
	public IMatrix copy() throws Exception {
		return new MatrixSubMatrixView(originalMatrix, rowIndexes, colIndexes);
	}

	@Override
	public IMatrix newInstance(int rows, int cols) throws Exception {
		return originalMatrix.newInstance(rows, cols);
	}

	@Override
	public IMatrix subMatrix(int row, int col, boolean liveView) throws Exception {
		if (this.getRowsCount() == 1 || this.getColsCount() == 1)
			throw new Exception();
		if (liveView == true)
			return new MatrixSubMatrixView(this, row, col);
		else 
			return super.subMatrix(row, col, false);	
	}
}
