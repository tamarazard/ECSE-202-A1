import java.awt.Color;

import acm.graphics.GLine;
import acm.graphics.GOval;
import acm.program.GraphicsProgram;

public class Bounce extends GraphicsProgram{
	
	private static final long serialVersionUID = 1L;
	private static final int WIDTH=600; //Width of the applet
	private static final int HEIGHT=600; // Height of the applet
	private static final int OFFSET=200; // Offset of the applet
	
	private static final double g = 9.8; // gravitational constant 9.8 m/s^2
	private static final double Pi = 3.141592654; // To convert degrees to radians
	private static double Xinit = 5.0; // Initial ball position (X) before simulation
	private static final double DELTA = 0.1; // Clock tick duration (sec)
	private static final double ETHR = 0.01; // Threshold
	private static final double XMAX = 100.0; // Maximum value of X
	private static final double SCALE = HEIGHT/XMAX ; // To convert from pixels value to simulation values
	private static final double k = 0.0016; // Chosen constant for the assignment
	private static final boolean TEST = true; // to validate the program
	
	public void run() {
		this.resize(WIDTH, HEIGHT+OFFSET); //creating the applet
		
		double Vox = 0; //defining Vox before the simulation
		double Voy = 0; //defining Voy before the simulation
		double Vt = 0; //defining Vt before the simulation
	
	//code to read simulation parameters from user
		double Vo = readDouble ("Enter the initial velocity of the ball in meters/seconds [0,100]:");
		double theta = readDouble ("Enter the launch angle in degrees [0,90]:");
		double lossValue = readDouble ("Enter energy loss parameter [0,1]:");		
		double bSize = readDouble ("Enter the radius of the ball in meters [0.1,5]:");
		
		GLine line = new GLine (0,HEIGHT,WIDTH,HEIGHT); // creating the line on which the ball will bounce
		line.setColor(Color.BLACK); //defining the color of the line
		add(line);

	// converting the pixels coordinates to simulation coordinates
	double ScaledX = Xinit*SCALE; 
	double ScaledY = HEIGHT-2*bSize*SCALE; 
	
	GOval myBall = new GOval (ScaledX,ScaledY,2*bSize*SCALE,2*bSize*SCALE); // creating the ball
	myBall.setFilled(true); // to fill the ball with the desired color
	myBall.setColor(Color.GREEN); //defining the ball's color
	add(myBall);
	pause(1000); 
		
	
	double t = 0; 
	Vt = g / (4*Pi*bSize*bSize*k); // Terminal velocity
	Vox=Vo*Math.cos(theta*Pi/180); // X component of initial velocity
	Voy=Vo*Math.sin(theta*Pi/180); // Y component of initial velocity
	
	double KEx = 0.5*Vox*Vox; // Kinetic energy in X direction 
	double KEy = 0.5*Voy*Voy; // Kinetic energy in Y direction  
	
	double Xlast = 0;
	double Ylast = 0;
	double Vy = 0;
	double Vx =0;
	
	
	while (true) { //start of simulation loop 
		double X = Vox*Vt/g*(1-Math.exp(-g*t/Vt)) + Xinit; // X position during simulation
		double Y = (bSize + Vt/g*(Voy+Vt)*(1-Math.exp(-g*t/Vt))-Vt*t); // Y position during simulation
		
	
		if (Y<=2*bSize && Vy<0) { //when collision is detected 
			
			KEx = 0.5*Vx*Vx*(1-lossValue); //change of the kinetic energy in X direction
			KEy = 0.5*Vy*Vy*(1-lossValue); // change of the kinetic energy in Y direction
			
			Vox = Math.sqrt(KEx*2); //X component of initial velocity affected by the loss of energy
			Voy = Math.sqrt(KEy*2); //Y component of initial velocity affected by the loss of energy
			t=0;
			Xinit = X; // change of Xinit for the next parabola
			         
		}
		double dotHeight = HEIGHT-2*bSize*SCALE;
		GOval dots = new GOval (Xinit*SCALE, dotHeight,1,1); //adding the dots to mark the trajectory of the ball
		add (dots);
		
	//display update
	double ScrX = (X-bSize)*SCALE;
	double ScrY =  HEIGHT -(Y+bSize)*SCALE;
	myBall.setLocation(ScrX,ScrY);         
	dots.setLocation(ScrX+4*bSize, ScrY +8*bSize);
	
	
	Vy = (Y-Ylast)/DELTA; // Estimate Vx from difference
	Vx = (X-Xlast)/DELTA; // Estimate Vy from difference
	
	pause(100);
	t+=DELTA; 
	Xlast = X;
	Ylast = Y;
	
 if ((KEx<ETHR)|| (KEy<ETHR)) break; //condition for the simulation to terminate
 
 if (TEST) System.out.printf("t: %.2f X: %.2f Y: %.2f Vx: %.2f Vy:%.2f\n",
		 t,Xinit+X,Y,Vx,Vy); // print on the console the values of the time, X, Y, Vx and Vy
 

		}
	}
}



