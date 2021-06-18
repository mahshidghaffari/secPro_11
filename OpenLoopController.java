package work.in.progress.landingStuff;

import java.util.ArrayList;

import reviewed.Vector2d;
import reviewed.landingsimulatorresult.StateLanding;

public class OpenLoopController {
    //initial U and V
    private double uForce = 2;
    private double vForce = 0.25; 
    private double u;
    private double v;
    private ArrayList<Double> us;
    private ArrayList<Double> vs;

    // initial currnet Tetta and require Tetta
    private double tetta;
    private double reqTetta;

    //time
    private ArrayList<Double> times;
    private double timeStep = 0.1;

    // position and velocity and ...
    private Vector2d currentPos;
    private Vector2d currentVel;
    private Vector2d currentAcc;

    private double distance;
   
    private double LandingHeight = 1000;
    private Vector2d initialLandingPos = new Vector2d(0,LandingHeight);
    
    public void landWithUandV (StateLanding state){
        currentPos = state.getPosition();
        currentVel = state.getVelocity();
        currentAcc = new Vector2d();
    }


    public Vector2d[] lanndingMain(){
        boolean moving = false;
        reqTetta = currentPos.angle(initialLandingPos);
        moving = true;
        u = uForce;



        return null;
    }

    public void rotation(double reqTetta){

        if(currentPos.getX()>0){
            v = -vForce;
        }else{
            v = vForce; 
        }

        times.add(Math.abs(reqTetta)/Math.abs(v));
        
        for (int i=0;i<Math.sqrt(times.get(0));i+=timeStep){

            //updating the current position and current velocity
            currentPos = currentPos.add(currentVel.mul(timeStep));
            currentVel = currentVel.add(currentAcc.mul(timeStep));
        
            // updating acceleration
            currentAcc.setX();
        }

    }

    public void distanceToLandingPos(){
        distance = Math.pow(Math.pow(currentPos.getX(), 2)+ Math.pow(currentPos.getY(), 2), 0.5);
    }



}
