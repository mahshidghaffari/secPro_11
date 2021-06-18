package to.be.reviewed;

/*
[AURÉLIEN]: can be deleted imo, I added that to Vector3d
*/

import java.util.*;

import titan.mathematical.structures.Vector3dInterface;

/**
 * A class that represents a vector that consists of three double values
 */
public class Vector3d implements Vector3dInterface {

	private ArrayList<Double> vec; // the Vector is depicted by an ArrayList

	/**
	 * Constructor creating a vector
	 * @param x initial x-value of the vector
	 * @param y initial y-value of the vector
	 * @param z initial z-value of the vector
	 */
	public Vector3d(double x, double y, double z) {
		vec = new ArrayList<Double>();
		vec.add(x); //the initial values of the vector
		vec.add(y);
		vec.add(z);
	}
	/**
	 * Constructor creating a zero-vector
	 */
	public Vector3d(){
		vec = new ArrayList<Double>();
		vec.add(0.0);
		vec.add(0.0);
		vec.add(0.0);
	}
	/**
	 * A method to get the x-value of vector
	 * @return x-value
	 */
	public double getX() { return vec.get(0); }
	/**
	 * A method to change the x-value of vector
	 * @param x x-value
	 */
    public void setX(double x) { vec.set(0,x); }
	/**
	 * A method to get the y-value of vector
	 * @return y-value
	 */
    public double getY() { return vec.get(1); }
	/**
	 * A method to change the y-value of vector
	 * @param y y-value
	 */
    public void setY(double y) { vec.set(1,y); }
	/**
	 * A method to get the z-value of vector
	 * @return z-value
	 */
    public double getZ() { return vec.get(2); }
	/**
	 * A method to change the z-value of vector
	 * @param z z-value
	 */
    public void setZ(double z) { vec.set(2,z); }
	/**
	 * A method for vector addition
	 * @param other other vector to add to the current vector
	 * @return sum of the two vectors
	 */
    public Vector3dInterface add(Vector3dInterface other) {
    	Vector3dInterface newv = new Vector3d(0,0,0);
    	newv.setX(vec.get(0)+other.getX());
    	newv.setY(vec.get(1)+other.getY());
    	newv.setZ(vec.get(2)+other.getZ());
    	return newv;
    }
	/**
	 * A method for vector subtraction
	 * @param other vector to substract from the current vector
	 * @return difference of the two vectors
	 */
    public Vector3dInterface sub(Vector3dInterface other) {
    	Vector3dInterface newv = new Vector3d(0,0,0);
    	newv.setX(vec.get(0)-other.getX());
    	newv.setY(vec.get(1)-other.getY());
    	newv.setZ(vec.get(2)-other.getZ());
    	return newv;
    }
	/**
	 * A method for vector*scalar multiplication 
	 * @param scalar scalar to multiply the vector with
	 * @return vector formed by the multiplication
	 */
    public Vector3dInterface mul(double scalar) {
    	Vector3dInterface newv = new Vector3d(0,0,0);
    	newv.setX(vec.get(0)*scalar);
    	newv.setY(vec.get(1)*scalar);
    	newv.setZ(vec.get(2)*scalar);
    	return newv;
    }
	/**
	 * A method for vector addition of a vector and another a scaled vector
	 * @param scalar scalar
	 * @param other other vector
	 * @return vector formed by applying the sum and product
	 */
    public Vector3dInterface addMul(double scalar, Vector3dInterface other) {
    	Vector3dInterface newv = new Vector3d(0,0,0);
    	newv.setX(other.getX()*scalar);
    	newv.setY(other.getY()*scalar);
    	newv.setZ(other.getZ()*scalar);

    	newv.setX(vec.get(0)+newv.getX());
    	newv.setY(vec.get(1)+newv.getY());
    	newv.setZ(vec.get(2)+newv.getZ());

    	return newv;
    }
	/**
	 * A method to find the norm of the vector
	 * @return norm of the vector
	 */
    public double norm() {
    	double sq = Math.pow(vec.get(0),2)+Math.pow(vec.get(1),2)+Math.pow(vec.get(2),2);
    	return Math.sqrt(sq);
    }
	/**
	 * A method to find the infinite norm of the vector
	 * @return infinite norm of the vector
	 */
	public double infiniteNorm(){
		double infNorm = Math.abs(vec.get(0));
		for(int i = 1; i<vec.size(); i++ ){
			if(Math.abs(vec.get(i))> infNorm){
				infNorm = Math.abs(vec.get(i));
			}
		}
		return infNorm;
	}
	/**
	 * A method to find the distance between two vectors
	 * @param other other vector
	 * @return distance between the two vectors
	 */
    public double dist(Vector3dInterface other) {
    	double sq = Math.pow(vec.get(0)-other.getX(),2)+Math.pow(vec.get(1)-other.getY(),2)+Math.pow(vec.get(2)-other.getZ(),2);
    	return Math.sqrt(sq);
    }
	/**
	 * A method to find the distance between two vectors
	 * @param other other vector
	 * @param distance distance
	 * @return new vector
	 */
    public Vector3dInterface moveToOtherVector(Vector3dInterface other, double distance) {
    	Vector3dInterface connection = other.sub(this);
		double dist = this.dist(other);
		double scalar = distance/dist;
		Vector3dInterface newv = this.addMul(scalar, connection);
    	return newv;
    }
	/**
	 * A method that returns a string representing the vector
	 * @return string of the vector
	 */
   	public String toString() {
   		String vect = "("+vec.get(0)+","+vec.get(1)+","+vec.get(2)+")";
   		return vect;
   	}
	/**
	 * A method to compare two vectors
	 * @param v the vector to be compared with the current vector
	 * @return true if the vectors are the same, else false
	 */
	public boolean equals(Vector3dInterface v) { return (v.getX() == this.getX() && v.getY() == this.getY() && v.getZ() == this.getZ()); }
	/**
	 * A method that clones a vector
	 * @return vector cloned
	 */
	@Override
	public Vector3dInterface clone() { return new Vector3d(this.getX(), this.getY(), this.getZ()); }
}

