package lab01.pomlib;

public class Matrix extends AbstractMatrix {
	
	double[][] elements;
	int rows;
	int cols;
	
	public Matrix(int _rows, int _cols) throws Exception{
		if (_rows == 0 || _cols == 0)
			throw new Exception();
		rows = _rows;
		cols = _cols;
		elements = new double[rows][cols];
	}
	
	public Matrix (int _rows, int _cols, double[][] array, boolean canUseArray) throws Exception{
		if (_rows == 0 || _cols == 0)
			throw new Exception();
		rows = _rows;
		cols = _cols;
		if (canUseArray == true)
			elements = array;
		else {
			elements = new double[rows][cols];
			for (int i = rows-1; i >= 0; i--)
				for (int j = cols-1; j >= 0; j--)
					elements[i][j] = array[i][j];
		}		
	}
	
	@Override
	public int getRowsCount() {
		return rows;
	}
	
	@Override
	public int getColsCount() {
		return cols;
	}
	
	public double get(int i, int j) {
		return elements[i][j];		
	}
	
	public IMatrix set(int i, int j, double val) {
		elements[i][j] = val;
		return this;
	}
	
	public IMatrix copy() throws Exception {
		return new Matrix(rows, cols, elements, false);	
	}
	
	public IMatrix newInstance(int _rows, int _cols) throws Exception {
		return new Matrix(_rows, _cols);
	}
	
	public static Matrix parseSimple(String input) throws Exception {
		String[] strRows = input.split("[|]+");
		if (strRows.length <= 0)
			throw new Exception();
		int rows = strRows.length;
		int cols = strRows[0].trim().split("[ ]+").length;
		double array[][] = new double[rows][cols];
		for (int i = 0; i < rows; i++) {
			String strElements[] = strRows[i].trim().split("[ ]+");
			if (strElements.length != cols)
				throw new Exception();
			for (int j = 0; j < cols; j++)
				array[i][j] = Double.parseDouble(strElements[j]);				
		}				
		return new Matrix(rows, cols, array, false);
	}
	
	

}
