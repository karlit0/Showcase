package lab01.pomlib;

import java.util.Arrays;

public abstract class AbstractVector implements IVector {
	
	public abstract double get(int arg);
	public abstract IVector set(int arg1, double arg2) throws Exception;
	public abstract int getDimension();
	public abstract IVector copy() throws Exception;
	public abstract IVector newInstance(int arg);
	
	@Override
	public IVector add(IVector other) throws Exception/*IncompatibleOperandException*/ {
		if (this.getDimension() != other.getDimension())
			throw new Exception();/*IncompatibleOperandException();*/
		for (int i = this.getDimension()-1; i >= 0; i--) {
			this.set(i, this.get(i)+other.get(i));
		}		
		return this;
	}
	
	@Override
	public IVector nAdd(IVector other) throws Exception {
		return this.copy().add(other);
	}
	
	@Override
	public IVector sub(IVector other) throws Exception {
		if (this.getDimension() != other.getDimension())
			throw new Exception();
		for (int i = this.getDimension()-1; i >= 0; i--) {
			this.set(i, this.get(i)-other.get(i));
		}
		return this;
	}
	
	@Override
	public IVector nSub(IVector other) throws Exception{
		return this.copy().sub(other);
	}
	
	@Override
	public IVector scalarMultiply(double multiplier) throws Exception {
		for (int i = this.getDimension()-1; i >= 0; i--) {
			this.set(i, this.get(i)*multiplier);
		}
		return this;		
	}
	
	@Override
	public IVector nScalarMultiply(double multiplier) throws Exception {
		return this.copy().scalarMultiply(multiplier);
	}
	
	@Override
	public double norm() {
		double metaResult = 0;
		for (int i = this.getDimension()-1; i >= 0; i--) {
			metaResult += this.get(i)*this.get(i);
		}		
		return Math.sqrt(metaResult);
	}
	
	@Override
	public IVector normalize() throws Exception {
		double norm = this.norm();			
		if (norm == 0) /* null-vector */
			throw new Exception();		
		for (int i = this.getDimension()-1; i >= 0; i--) {
			this.set(i, this.get(i)/norm);
		}
		return this;
	}
	
	@Override
	public IVector nNormalize() throws Exception {
		return this.copy().normalize();
	}
	
	@Override
	public double cosine(IVector other) throws Exception {
		return (this.scalarProduct(other)) / (this.norm()*other.norm());
	}
	
	@Override
	public double scalarProduct(IVector other) throws Exception {
		double metaResult = 0;
		if (this.getDimension() != other.getDimension())
			throw new Exception();
		for (int i = this.getDimension()-1; i >= 0; i--) {
			metaResult += this.get(i)*other.get(i);
		}
		return metaResult;
	}
	

	@Override
	public IVector nVectorProduct(IVector other) throws Exception {
		if (this.getDimension() != other.getDimension() || this.getDimension() != 3)
			throw new Exception();
		IVector newVector = this.copy();		
		newVector.set(0, this.get(1)*other.get(2) - this.get(2)*other.get(1));
		newVector.set(1, -this.get(0)*other.get(2) + this.get(2)*other.get(0));
		newVector.set(2, this.get(0)*other.get(1) - this.get(1)*other.get(0));
		return newVector;		
	}
	
	@Override
	public IVector nFromHomogeneus() {
		double[] array = Arrays.copyOfRange(this.toArray(), 0, this.getDimension()-1);
		double d = this.get(getDimension()-1);
		for (int i = this.getDimension()-2; i >= 0; i--) {
			array[i] /= d;
		}
		return new Vector(array);
	}
	
	@Override
	public double[] toArray() {
		double[] array = new double[this.getDimension()];
		for (int i = this.getDimension()-1; i >= 0; i--) {
			array[i] = this.get(i);
		}
		return array;
	}
	
	@Override
	public IVector copyPart(int n) {
		double[] array = new double[n];
		int i = 0;
		while (i < n) {
			if (i < this.getDimension())
				array[i] = this.get(i);
			else
				array[i] = 0;
			i++;
		}		
		return new Vector(array);
	}
	
	@Override
	public IMatrix toRowMatrix(boolean liveView) throws Exception {
		if (liveView)
			return new MatrixVectorView(this, true);
		else {
			double[][] array = new double[1][this.getDimension()];
			for (int j = this.getDimension()-1; j >= 0; j--)
				array[0][j] = this.get(j);
			return new Matrix(1, this.getDimension(), array, false);
		}		
	}

	@Override 
	public IMatrix toColumnMatrix(boolean liveView) throws Exception {
		if (liveView)
			return new MatrixVectorView(this, false);
		else {
			double[][] array = new double[this.getDimension()][1];
			for (int i = this.getDimension()-1; i >= 0; i--)
				array[i][0] = this.get(i);
			return new Matrix(this.getDimension(), 1, array, false);
		}
	}
		
	@Override
	public String toString(int precision) {
		int dim = this.getDimension();
		if (dim == 0)	/* null-vector	*/
			return "0";
		String str = "[";		
		for (int i = 0; i < dim; i++) {
			if (i+1 < dim)
				str += String.format("%." + precision + "f", this.get(i)) + ", ";
			else
				str += String.format("%." + precision + "f", this.get(i)) + "]";
		}
		return str;
	}
	
	@Override
	public String toString() {
		return this.toString(3);
	}		
}
