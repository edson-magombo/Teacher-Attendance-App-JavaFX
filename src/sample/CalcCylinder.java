package sample;
import java.util.Date;
import java.util.Scanner;


public class CalcCylinder {
	String name;
	double pi = Math.PI;
	private double radius;
	private double height;
	double circleArea, outsideArea, totalSurfaceArea, volume;

	CalcCylinder(){
	}

	double getArea(){
		circleArea = 2 * pi * radius * radius;
		outsideArea = 2 * pi * radius * height;

		totalSurfaceArea = circleArea + outsideArea;
		return totalSurfaceArea;
	}

    double getVolume(){
        volume = pi * radius * radius * height;
        return volume;}

	double getDiameter(){

		return (radius * 2);
	}

	void setRadius(double newRadius){

		radius = newRadius;
	}
	void setHeight(double newHeight){

		height = newHeight;
	}
}

class Cylinder{
	public static void main(String[] args){

		Scanner input = new Scanner(System.in);
		
		System.out.println("enter the radius of your cylinder: ");
		double radiusI = input.nextDouble();
		
		System.out.println("enter the height of your cylinder");
		double heightI = input.nextDouble();

		CalcCylinder c1 = new CalcCylinder();
		c1.name = "drum";

		c1.setRadius(radiusI);
		c1.setHeight(heightI);

		double cArea = c1.getArea();
		double cVolume = c1.getVolume();
		double cDiameter = c1.getDiameter();

		Date date = new Date();
		System.out.print(date);

		System.out.println("the area of your cylinder is "+ cArea +  " and the volume of your cylinder " +
				"is " + cVolume + " with a diameter of " + cDiameter);
}

}