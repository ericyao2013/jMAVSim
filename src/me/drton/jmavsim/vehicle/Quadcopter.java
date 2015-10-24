package me.drton.jmavsim.vehicle;

import me.drton.jmavsim.Rotor;
import me.drton.jmavsim.World;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;
import java.io.FileNotFoundException;

/**
 * Generic quadcopter model.
 */
public class Quadcopter extends AbstractMulticopter {
    private static final int rotorsNum = 4;
    private Vector3d[] rotorPositions = new Vector3d[rotorsNum];

    /**
     * Generic quadcopter constructor.
     *
     * @param world          world where to place the vehicle
     * @param modelName      filename of model to load, in .obj format
     * @param orientation    "x" or "+"
     * @param armLength      length of arm from center
     * @param rotorThrust    full thrust of one rotor
     * @param rotorTorque    torque at full thrust of one rotor
     * @param rotorTimeConst spin-up time of rotor
     * @param rotorsOffset   rotors positions offset from gravity center
     * @throws FileNotFoundException if .obj model file not found
     */
    public Quadcopter(World world, String modelName, String orientation, double armLength, double rotorThrust,
                      double rotorTorque, double rotorTimeConst, Vector3d rotorsOffset) throws FileNotFoundException {
        super(world, modelName);
        rotorPositions[0] = new Vector3d(0.0, armLength, 0.0);
        rotorPositions[1] = new Vector3d(armLength, 0.0, 0.0);
        rotorPositions[2] = new Vector3d(0.0, -armLength, 0.0);
        rotorPositions[3] = new Vector3d(-armLength, 0.0, 0.0);
        if (orientation.equals("x") || orientation.equals("X")) {
            Matrix3d r = new Matrix3d();
            r.rotZ(-Math.PI / 4);
            for (int i = 0; i < rotorsNum; i++) {
                r.transform(rotorPositions[i]);
            }
        } else if (orientation.equals("+")) {
        } else {
            throw new RuntimeException("Unknown orientation: " + orientation);
        }
        for (int i = 0; i < rotors.length; i++) {
            rotorPositions[i].add(rotorsOffset);
            Rotor rotor = rotors[i];
            rotor.setFullThrust(rotorThrust);
            rotor.setFullTorque((i % 2 == 0) ? -rotorTorque : rotorTorque);
            rotor.setTimeConstant(rotorTimeConst);
        }
    }

    @Override
    protected int getRotorsNum() {
        return rotorsNum;
    }

    @Override
    protected Vector3d getRotorPosition(int i) {
        return rotorPositions[i];
    }
}
