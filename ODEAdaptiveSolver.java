package titan.ode;

import java.util.Arrays;

import titan.RateInterface;
import titan.StateInterface;
import titan.mathematical.structures.Vector3d;
import titan.universe.State;


public class ODEAdaptiveSolver implements ODESolverInterface {

	private double et = 0.001; // this is error tolerence

	@Override
	public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double[] ts) {
		StateInterface[] ys = new StateInterface[ts.length];
		ys[0] = y0;

		for (int i = 0; i < ts.length - 1; i++) {
			double t = ts[i];
			double h = ts[i + 1] - ts[i];
			ys[i + 1] = step(f, t, ys[i], h);
		}

		return ys;
	}

	@Override
	public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double tf, double h) {
		int steps = (int) Math.floor(tf / h); // get the approximate

		double last_step = tf - (steps * h);

		// if step-size does not exactly divide the solution time range
		if (last_step > 0) {
			// increase amount of steps by 1
			steps += 1;
		}

		StateInterface[] result = new StateInterface[steps + 1]; // empty array of all states (to be recorded)
		result[0] = y0; // initial state is given

		// compute state for all other steps
		for (int step = 1; step <= steps; step++) {
			// if last step is different from other steps
			if (step == steps && last_step > 0) {
				result[step] = step(f, h * (step - 1), result[step - 1], last_step);
			} else {
				result[step] = step(f, h * (step - 1), result[step - 1], h);
			}
		}
		return result;
	}

	/**
	 * Update rule for one step.
	 *
	 * @param f the function defining the differential equation dy/dt=f(t,y)
	 * @param t the time
	 * @param y the state
	 * @param h the step size
	 * @return the new state after taking one step with optimized step size
	 */
	@Override
	public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {

		State currentState = (State) y;
		State fourth = (State) fourthOrderRungeKutta(f, t, y, h);
		State third = (State) thirdOrderRungeKutta(f, t, y, h);

		// two arrays to save the maximum difference of our two states
		double[] positionMax = new double[currentState.size()];
		double[] velocityMax = new double[currentState.size()];
		// System.out.println(vec.sub(vecc) );

		// finding the maximum of the difference of each planet in state
		for (int i = 0; i < currentState.size(); i++) {
			positionMax[i] = ((Vector3d) fourth.getPositions()[i].sub(third.getPositions()[i])).infiniteNorm();
			velocityMax[i] = ((Vector3d) fourth.getVelocities()[i].sub(third.getVelocities()[i])).infiniteNorm();
			// System.out.println("------------------");
			// System.out.println(fourth.getPositions()[i].sub(third.getPositions()[i]));
		}

		Arrays.sort(positionMax);
		Arrays.sort(velocityMax);

		// find the maximum in positionMax array and velocityMax array
		double maxi = Math.max(positionMax[positionMax.length - 1], velocityMax[positionMax.length - 1]);

		// q = (ei * h / s * |forth - third|) * (1/n) --> n = 2 , s = 2, |forth - third
		// | = maxi
		double q = Math.sqrt(et * h * 0.5 / maxi);

		if (maxi >= et * h) {

			return step(f, t, y, h * q); // recursive call with new h

		} else {

			return third;
		}
	}

	/**
	 * Update rule for one step in third order Runge-kutta.
	 *
	 * @param f the function defining the differential equation dy/dt=f(t,y)
	 * @param t the time
	 * @param y the state
	 * @param h the step size
	 * @return the new state after taking one step
	 */
	private StateInterface thirdOrderRungeKutta(ODEFunctionInterface f, double t, StateInterface y, double h) {
		RateInterface k1 = f.call(t, y);
		RateInterface k2 = f.call(t + h * 0.5, y.addMul(0.5 * h, k1));
		RateInterface k3 = f.call(t + h, y.addMul(-h, k1).addMul(2 * h, k2));

		return y.addMul(h / 6, k1).addMul(2 * h / 3, k2).addMul(h / 6, k3);
	}

	/**
	 * Update rule for one step in forth order Runge-Kutta.
	 * 
	 * @param f the function defining the differential equation dy/dt=f(t,y)
	 * @param t the time
	 * @param y the state
	 * @param h the step size
	 * @return the new state after taking one step
	 */
	private StateInterface fourthOrderRungeKutta(ODEFunctionInterface f, double t, StateInterface y, double h) {
		RateInterface k1 = f.call(t, y);
		RateInterface k2 = f.call(t + h * 0.5, y.addMul(0.5 * h, k1));
		RateInterface k3 = f.call(t + h * 0.5, y.addMul(0.5 * h, k2));
		RateInterface k4 = f.call(t + h, y.addMul(h, k3));

		return y.addMul(h / 6, k1).addMul(h / 3, k2).addMul(h / 3, k3).addMul(h / 6, k4);
	}
}
