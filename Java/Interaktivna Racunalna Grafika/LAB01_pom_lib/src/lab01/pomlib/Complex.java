package lab01.pomlib;

public class Complex {
	double re;
	double im;
	
	public Complex(double _re, double _im) {
		re = _re;
		im = _im;	
	}
	
	public double getRe() {
		return re;
	}
	public double getIm() {
		return im;
	}
	
	public Complex add(Complex other) {
		re += other.re;
		im += other.im;
		return this;
	}
	public Complex sub(Complex other) {
		re -= other.re;
		im -= other.im;
		return this;
	}
	public Complex multiply(Complex other) {
		double a = re;
		double b = im;
		double c = other.re;
		double d = other.im;
		// (a + bi) (c + di) = (ac - bd) (bc + ad)i
		
		re = a*c - b*d;
		im = b*c + a*d;
		return this;
	}
	public Complex squared() {
		Complex z = new Complex(re, im);		
		return this.multiply(z);
	}
	public Complex cubed() {
		Complex z = new Complex(re, im);
		return this.multiply(z.multiply(z));
	}
	public double module() {
		return Math.sqrt(re*re + im*im);
	}
	
	@Override
	public String toString() {
		return re + ", " + im + "i";
	}
}

