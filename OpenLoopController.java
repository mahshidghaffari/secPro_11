package titan.ode;

import titan.mathematical.structures.Vector3d;

public class OpenLoopController {

    private Vector3d CurrentPos;
    private Vector3d CurrentVel;
    private Vector3d CurrentAcce;

    private double uForce = 5;
    private double vForce = 1;

    private double timeStep = 0.0002;

    private double u;
    private double v;

    private static final double g = 1.352;

    public OpenController(Vector3d position, Vector3d velocity, Vector3d acceleration) {
        this.CurrentVel = velocity;
        this.CurrentAcce = acceleration;
        this.CurrentPos = position;
        double angle = Math.acos(g / uForce);//old
        boolean speedUp = true;
        u = uForce;
        rotationState(angle, speedUp);
        v = 0;
        setAcc();
        //old
        double timeNeeded = (-CurrentVel.getX()
                - Math.sqrt(Math.pow(CurrentVel.getX(), 2) - 2 * CurrentAcce.getX() * CurrentPos.getX() / 2)) / CurrentAcce.getX();

        for (double i = 0; i <= timeNeeded; i += timeStep) {
            update();
        }
        speedUp = false;
        u = uForce;
        rotationState(-angle, speedUp);
        v = 0;
        setAcc();
        u = uForce;
        rotationState(-angle, speedUp);
        v = 0;
        setAcc();
        //old
        double tReq = Math.abs(CurrentPos.getX() / (0.5 * CurrentVel.getX()));
        double accReq = -(CurrentVel.getX() / tReq);
        double uReq = Math.abs(accReq / Math.sin(CurrentPos.getZ()));

        u = uReq;
        setAcc();

        for (double i = 0; i <= tReq; i += timeStep) {
            update();
        }
        u = 0;
        rotationState(angle, speedUp);
        v = 0;
        setAcc();
        fallDown();

    }

    public void rotationState(double reqTheta, boolean speedUp) {

        if (speedUp) {
            if (CurrentPos.getX() > 0)
                v = -vForce;
            else
                v = vForce;
        }

        else {
            if (CurrentPos.getX() > 0)
                v = vForce;
            else
                v = -vForce;
        }
        //old
        double time_halfway = Math.sqrt(Math.abs(reqTheta) / Math.abs(v));

        for (double i = 0; i <= time_halfway; i += timeStep) {
            setAcc();
            update();
        }
        v = -v;

        for (double i = 0; i <= time_halfway; i += timeStep) {
            setAcc();
            update();
        }

    }

    public void fallDown() {
        u = 0;
        setAcc();
        //old
        double tFreeFall = -(-CurrentVel.getY()
                - Math.sqrt(Math.pow(CurrentVel.getY(), 2) - 4 * g / 2.0 * (-1 / 4.0) * CurrentPos.getY())) / g;

        for (double i = 0; i <= tFreeFall; i += timeStep) {
            update();
        }

        CurrentAcce.setY(Math.pow(CurrentVel.getY(), 2) / (2 * CurrentPos.getY()));
        double timeNeeded = Math.abs(CurrentVel.getY() / CurrentAcce.getY());

        for (double i = 0; i <= timeNeeded; i += timeStep) {
            update();
        }

        System.out.println("X:" + CurrentPos.getX());
        System.out.println("Y:" + CurrentPos.getY());
        System.out.println("Z:" + CurrentPos.getZ());

    }

    public void update() {
        CurrentPos = (Vector3d) CurrentPos.addMul(timeStep, CurrentVel);
        CurrentVel = (Vector3d) CurrentVel.addMul(timeStep, CurrentAcce);
    }

    public void setAcc() {
        CurrentAcce.setX(Math.sin(CurrentPos.getZ()) * u);
        CurrentAcce.setY(Math.cos(CurrentPos.getZ()) * u - g);
        CurrentAcce.setZ(v);
    }

    public static void main(String[] args) {

        OpenController con = new OpenController(new Vector3d(5000, 100000, 0), new Vector3d(), new Vector3d());

    }
}
