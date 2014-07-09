package lab01.pomlib;

public abstract class AbstractMatrix implements IMatrix {
	
	
	public abstract int getRowsCount();
	public abstract int getColsCount();
	public abstract double get(int arg1, int arg2);
	public abstract IMatrix set(int arg1, int arg2, double arg3) throws Exception;
	public abstract IMatrix copy() throws Exception;
	public abstract IMatrix newInstance(int arg1, int arg2) throws Exception;
	
	@Override
	public IMatrix nTraspose(boolean liveView) throws Exception {
		IMatrix newMatrix = null;
		if (liveView == true)
			return new MatrixTransposeView(this);
		else {
			newMatrix = new Matrix(this.getColsCount(), this.getRowsCount());
			for (int i = newMatrix.getRowsCount()-1; i >= 0; i--)
				for (int j = newMatrix.getColsCount()-1; j>=0; j--)
					newMatrix.set(i, j, this.get(j, i));			
		}
		return newMatrix;
	}
	
	@Override
	public IMatrix add(IMatrix other) throws Exception {
		if (this.getRowsCount() != other.getRowsCount()
				|| this.getColsCount() != other.getColsCount())
			throw new Exception();
		for (int i = this.getRowsCount()-1; i >= 0; i--)
			for (int j = this.getColsCount()-1; j >= 0; j--)
				this.set(i, j, this.get(i, j)+other.get(i, j));
		return this;		
	}
	
	@Override
	public IMatrix nAdd(IMatrix other) throws Exception {
		return this.copy().add(other);
	}
	
	@Override
	public IMatrix sub(IMatrix other) throws Exception {
		if (this.getRowsCount() != other.getRowsCount()
				|| this.getColsCount() != other.getColsCount())
			throw new Exception();
		for (int i = this.getRowsCount()-1; i >= 0; i--)
			for (int j = this.getColsCount()-1; j >= 0; j--)
				this.set(i, j, this.get(i, j)-other.get(i, j));
		return this;		
	}
	
	@Override
	public IMatrix nSub(IMatrix other) throws Exception {
		return this.copy().sub(other);
	}
	
	@Override
	public IMatrix nMultiply(IMatrix other) throws Exception {
		if (this.getColsCount() != other.getRowsCount())
			throw new Exception();
		IMatrix newMatrix = new Matrix(this.getRowsCount(), other.getColsCount());		
		for (int i = newMatrix.getRowsCount()-1; i >= 0; i--)
			for (int j = newMatrix.getColsCount()-1; j >= 0; j--) {
				double metaResult = 0;
				for (int k = this.getColsCount()-1/*newMatrix.getRowsCount()-1*/; k >= 0; k--)
						metaResult += this.get(i, k)*other.get(k, j);
				newMatrix.set(i, j, metaResult);
			}
		return newMatrix;
	}
	
	@Override
	public double determinant() throws Exception {
		if (this.getRowsCount() != this.getColsCount())
			throw new Exception();
		if (this.getRowsCount() == 2)
			return (this.get(0, 0)*this.get(1, 1) - this.get(0, 1)*this.get(1, 0));
		if (this.getRowsCount() == 1)
			return this.get(0, 0);
		double metaResult = 0;
		for (int j = 0; j < this.getColsCount(); j++) {
			int sgn;
			if (j%2 == 0)
				sgn = 1;
			else
				sgn = -1;
			metaResult += sgn*this.get(0, j)*subMatrix(0, j, true).determinant();
		}
		return metaResult;		
	}
	
	@Override
	public IMatrix subMatrix(int row, int col, boolean liveView) throws Exception {
		if (liveView == true)
			return new MatrixSubMatrixView(this, row, col);
		else {
			IMatrix newMatrix = new Matrix(this.getRowsCount()-1, this.getColsCount()-1);
			int i = 0;
			int i2 = 0;
			int j = 0;
			int j2 = 0;
			while (i < this.getRowsCount()-1) {
				j = 0;
				j2 = 0;
				if (i2 != row) {
					while (j < this.getColsCount()-1) {
						if (j2 != col) {
							newMatrix.set(i, j, this.get(i2, j2));
							j++;
						}		
						j2++;
					}
					i++;
				}
				i2++;
			}
			return newMatrix;
		}
		
	}
	
	@Override
	public IMatrix nInvert() throws Exception {
		double det = this.determinant();
		IMatrix matrixCofactor = new Matrix(this.getRowsCount(), this.getColsCount());
		for (int i = 0; i < matrixCofactor.getRowsCount(); i++)
			for (int j = 0; j < matrixCofactor.getColsCount(); j++) {
				int sgn;
				if ((i+j)%2 == 0)
					sgn = 1;
				else
					sgn = -1;
				matrixCofactor.set(i, j, sgn*this.subMatrix(i, j, true).determinant());
			}		
		IMatrix matrixCofactorTransposed = matrixCofactor.nTraspose(false);		
		IMatrix inverseMatrix = matrixCofactorTransposed.copy();
		for (int i = inverseMatrix.getRowsCount()-1; i >= 0; i--)
			for (int j = inverseMatrix.getColsCount()-1; j >= 0; j--)
				inverseMatrix.set(i, j, inverseMatrix.get(i, j)/det);
		return inverseMatrix;
	}
	
	@Override
	public double[][] toArray() {
		double[][] array = new double[this.getRowsCount()][this.getColsCount()];
		for (int i = this.getRowsCount()-1; i >= 0; i--)
			for (int j = this.getColsCount()-1; j >= 0; j--)
				array[i][j] = this.get(i, j);
		return array;
	}
	
	@Override
	public String toString() {
		return this.toString(3);
	}
	
	@Override
	public String toString(int precision) {
		String str = "[";
		int rowCount = this.getRowsCount();
		int colsCount = this.getColsCount();
		for (int i = 0; i < rowCount; i++){			
			for (int j = 0; j < colsCount; j++) {
				if (j+1 < colsCount)
					str += String.format("%." + precision + "f", this.get(i,j)) + ", ";
				else
					str += String.format("%." + precision + "f", this.get(i,j)) + "]";
			}
			if (i+1 < rowCount)
				str += "\n[";
		}
		return str;		
	}
	
	public IVector toVector(boolean liveView) throws Exception {
		if (liveView)
			return new VectorMatrixView(this);
		else {
			if (this.getRowsCount() == 1) {
				double array[] = new double[this.getColsCount()];
				for (int j = this.getColsCount()-1; j >= 0; j--)
					array[j] = this.get(0, j);
				return new Vector(array);
			}else if (this.getColsCount() == 1) {
				double array[] = new double[this.getRowsCount()];
				for (int i = this.getRowsCount()-1; i >= 0; i--)
					array[i] = this.get(i, 0);
				return new Vector(array);
			}else
				throw new Exception();
		}		
	}	
}
