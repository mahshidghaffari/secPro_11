package titan.ode;

import titan.*;
import titan.mathematical.structures.Vector3d;
import titan.mathematical.structures.Vector3dInterface;
import titan.universe.State;
import titan.universe.Universe;

import java.io.FileWriter;
import java.io.IOException;

public class ODEComparison {

    // The final time and step size
    static double FINAL_TIME = 60 * 60 * 24 * (182); // 1 years
    static double STEP_SIZE;// = 60 * 10; // 10 minutes

    public static void main(String[] args) throws IOException {

        long start;
        long elapsed;

        State lastSate;

        ODEEuler euler = new ODEEuler();
        ODERungeKutta runge = new ODERungeKutta();
        ODEVerlet verlet = new ODEVerlet();
        ODEVerletModified verlet_modified = new ODEVerletModified();
        ODEAdaptiveSolver adaptive = new ODEAdaptiveSolver();

        double absErrorEuler;
        double relErrorEuler;
        double absErrorRunge;
        double relErrorRunge;
        double absErrorVerlet;
        double relErrorVerlet;
        double absErrorVerlet_modified;
        double relErrorVerlet_modified;
        double absErrorAdaptive;
        double relErrorAdaptive;

        //Vector3dInterface marsCoordinates = new Vector(-8.471846525675179E+10,  2.274355520083190E+11,  6.819319353400901E+09); // 1 year
        //marsCoordinates = new Vector(2.043622654175490E+11,  4.567011623721744E+10, -4.085820593522130E+09); // 30. sep. 2020

        Vector3dInterface mercuryCoordinates = new Vector3d(1.976434630222940E+10, -6.276688066078666E+10, -7.090925138935536E+09); // 30. sep. 2020

        FileWriter csvWriter = new FileWriter("ode_comparison.csv");
        FileWriter csvWriterRuntime = new FileWriter("ode_comparison_runtime.csv");


        csvWriter.append("timestep, Euler: abs. error, Euler: rel. error, Verlet: abs. error, Verlet: rel. error, Runge-Kutta: abs. error, Runge-Kutta: rel. error, Verlet Modified: abs. error, Verlet Modified: rel. error").append("\n");
        csvWriterRuntime.append("timestep, euler runtime, verlet runtime, runge-kutta runtime, verlet modified runtime").append("\n");


        StateInterface[] re = euler.solve(new ODEUniverseFunction(), Universe.initialState,FINAL_TIME,14);
        lastSate = (State) re[re.length-1];
        absErrorEuler = absError(lastSate.getPositions()[1], mercuryCoordinates);
        relErrorEuler = relError(lastSate.getPositions()[1], mercuryCoordinates);
        System.out.println("Absolute Error Euler's Method: " + absErrorEuler);
        System.out.println("Relative Error Euler's Method: " + relErrorEuler);
        // mod verl abs. 20 : 4.218722132580199E7
        // mod verl. abs. 15: 4.218724940217987E7

        System.exit(1);

        for(STEP_SIZE = 600; STEP_SIZE > 60; STEP_SIZE-=60){
            //if(STEP_SIZE == 6) STEP_SIZE = 1;
            //if(STEP_SIZE == 60 + 60*30) STEP_SIZE = 60*30;

            System.out.println("\ntimestep: " + STEP_SIZE);

            csvWriter.append(Double.toString(STEP_SIZE)).append(",");
            csvWriterRuntime.append(Double.toString(STEP_SIZE)).append(",");

            // Euler
            start = System.currentTimeMillis();
            StateInterface[] resultEuler = euler.solve(new ODEUniverseFunction(), Universe.initialState,FINAL_TIME,STEP_SIZE);
            elapsed = System.currentTimeMillis()-start;
            lastSate = (State) resultEuler[resultEuler.length-1];
            System.out.println("Runetime for Euler's Method with timestep = " + STEP_SIZE + ": " + elapsed);
            absErrorEuler = absError(lastSate.getPositions()[1], mercuryCoordinates);
            relErrorEuler = relError(lastSate.getPositions()[1], mercuryCoordinates);
            System.out.println("Absolute Error Euler's Method: " + absErrorEuler);
            System.out.println("Relative Error Euler's Method: " + relErrorEuler);


            csvWriter.append(Double.toString(absErrorEuler)).append(",").append(Double.toString(relErrorEuler)).append(",");
            csvWriterRuntime.append(Long.toString(elapsed)).append(",");


            System.out.println();

            // Verlet
            start = System.currentTimeMillis();
            StateInterface[] resultVerlet = verlet.solve(new ODEUniverseFunction(),Universe.initialState,FINAL_TIME,STEP_SIZE);
            elapsed = System.currentTimeMillis()-start;
            lastSate = (State) resultVerlet[resultVerlet.length-1];
            System.out.println("Runetime for Verlet's Method with timestep = " + STEP_SIZE + ": " + elapsed);
            absErrorVerlet = absError(lastSate.getPositions()[1], mercuryCoordinates);
            relErrorVerlet = relError(lastSate.getPositions()[1], mercuryCoordinates);
            System.out.println("Absolute Error Verlet's Method: " + absErrorVerlet);
            System.out.println("Relative Error Verlet's Method: " + relErrorVerlet);


            csvWriter.append(Double.toString(absErrorVerlet)).append(",").append(Double.toString(relErrorVerlet)).append(",");
            csvWriterRuntime.append(Long.toString(elapsed)).append(",");



            System.out.println();


            // Runge
            start = System.currentTimeMillis();
            StateInterface[] resultRunge = runge.solve(new ODEUniverseFunction(),Universe.initialState,FINAL_TIME,STEP_SIZE);
            elapsed = System.currentTimeMillis()-start;
            lastSate = (State) resultRunge[resultRunge.length-1];
            System.out.println("Runetime for Runge-Kutta's Method with timestep = " + STEP_SIZE + ": " + elapsed);
            absErrorRunge = absError(lastSate.getPositions()[1], mercuryCoordinates);
            relErrorRunge = relError(lastSate.getPositions()[1], mercuryCoordinates);
            //absErrorRunge = 0;
            //relErrorRunge = 0;
            System.out.println("Absolute Error Runge-Kutta's Method: " + absErrorRunge);
            System.out.println("Relative Error Runge-Kutta's Method: " + relErrorRunge);


            csvWriter.append(Double.toString(absErrorRunge)).append(",").append(Double.toString(relErrorRunge)).append("\n");
            csvWriterRuntime.append(Long.toString(elapsed)).append("\n");


            System.out.println();

            // Verlet modified
            start = System.currentTimeMillis();
            StateInterface[] resultVerletModified = verlet_modified.solve(new ODEUniverseFunction(),Universe.initialState,FINAL_TIME,STEP_SIZE);
            elapsed = System.currentTimeMillis()-start;
            lastSate = (State) resultVerletModified[resultVerletModified.length-1];
            System.out.println("Runetime for Verlet modified's Method with timestep = " + STEP_SIZE + ": " + elapsed);
            absErrorVerlet_modified = absError(lastSate.getPositions()[1], mercuryCoordinates);
            relErrorVerlet_modified = relError(lastSate.getPositions()[1], mercuryCoordinates);
            System.out.println("Absolute Error Verlet modified's Method: " + absErrorVerlet_modified);
            System.out.println("Relative Error Verlet modified's Method: " + relErrorVerlet_modified);


            csvWriter.append(Double.toString(absErrorVerlet_modified)).append(",").append(Double.toString(relErrorVerlet_modified)).append(",");
            csvWriterRuntime.append(Long.toString(elapsed)).append(",");


            System.out.println();


            csvWriter.flush();
            csvWriterRuntime.flush();

            // //Adaptive
            // start = System.currentTimeMillis();
            // StateInterface[] resultAdaptive = adaptive.solve(new ODEUniverseFunction(),Universe.initialState,FINAL_TIME,STEP_SIZE);
            // elapsed = System.currentTimeMillis()-start;
            // lastSate = (State) resultAdaptive[resultAdaptive.length-1];
            // System.out.println("Runetime for Adaptive Method with timestep = " + STEP_SIZE + ": " + elapsed);
            // absErrorAdaptive = absError(lastSate.getPositions()[1], mercuryCoordinates);
            // relErrorAdaptive = relError(lastSate.getPositions()[1], mercuryCoordinates);
            // System.out.println("Absolute Error Adaptive Method: " + absErrorAdaptive);
            // System.out.println("Relative Error Adaptive Method: " + relErrorAdaptive);


            // csvWriter.append(Double.toString(absErrorAdaptive)).append(",").append(Double.toString(relErrorAdaptive)).append(",");
            // csvWriterRuntime.append(Long.toString(elapsed)).append(",");


            // System.out.println();


            // csvWriter.flush();
            // csvWriterRuntime.flush();

        }

        csvWriter.close();
        csvWriterRuntime.close();

    }

    public static double absError(Vector3dInterface estimate, Vector3dInterface actual){
        //return (estimate.sub(actual)).norm();
        return estimate.dist(actual);
    }

    public static double relError(Vector3dInterface estimate, Vector3dInterface actual){
        return ((estimate.sub(actual)).norm())/actual.norm();
    }

}
