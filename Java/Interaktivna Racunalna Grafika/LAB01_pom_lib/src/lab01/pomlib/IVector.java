package lab01.pomlib;

public interface IVector {

	public double get(int arg);
	public IVector set(int arg1, double arg2) throws Exception;
	public int getDimension();
	public IVector copy() throws Exception;
	public IVector copyPart(int arg);
	public IVector newInstance(int arg);
	public IVector add(IVector arg) throws Exception;
	public IVector nAdd(IVector arg) throws Exception;
	public IVector sub(IVector arg) throws Exception;
	public IVector nSub(IVector arg) throws Exception;
	public IVector scalarMultiply(double arg) throws Exception;
	public IVector nScalarMultiply(double arg) throws Exception;
	public double norm();
	public IVector normalize() throws Exception;
	public IVector nNormalize() throws Exception;
	public double cosine(IVector arg) throws Exception;
	public double scalarProduct(IVector arg) throws Exception;
	public IVector nVectorProduct(IVector arg) throws Exception;
	public IVector nFromHomogeneus();
	public IMatrix toRowMatrix(boolean arg) throws Exception;
	public IMatrix toColumnMatrix(boolean arg) throws Exception;	
	public double[] toArray();
	String toString(int precision);
}
