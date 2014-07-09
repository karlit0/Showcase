package lab01.pomlib;

public interface IMatrix {

	public int getRowsCount();
	public int getColsCount();
	public double get(int arg1, int arg2);
	public IMatrix set (int arg1, int arg2, double arg3) throws Exception;
	public IMatrix copy() throws Exception;
	public IMatrix newInstance(int arg1, int arg2) throws Exception;
	public IMatrix nTraspose(boolean arg) throws Exception;
	public IMatrix add(IMatrix arg) throws Exception;
	public IMatrix nAdd(IMatrix arg) throws Exception;
	public IMatrix sub(IMatrix arg) throws Exception;
	public IMatrix nSub(IMatrix arg) throws Exception;
	public IMatrix nMultiply(IMatrix arg) throws Exception;
	public double determinant() throws Exception;
	public IMatrix subMatrix(int arg1, int arg2, boolean arg3) throws Exception;
	public IMatrix nInvert() throws Exception;
	public double[][] toArray();
	public IVector toVector(boolean arg) throws Exception;
	public String toString(int i);	
}
