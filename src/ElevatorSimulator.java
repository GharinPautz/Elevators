
/**
 * Author: Gharin Pautz
 * Course: CPSC 326, Spring 2020
 * Asgnmt: HW 1
 *
 * Description: Simple program for practicing reading from a file,
 * parsing its contents, and printing the results.
 */

import java.util.*;
import java.io.*;


public class ElevatorSimulator {

    private BufferedReader buffer;           // buffered input stream reader
    private int elevatorCount;               // number of elevators
    private Vector<Integer> elevatorFloors;  // list of elevator locations

    /**
     * Create a new elevator simulator
     */
    public ElevatorSimulator(InputStream inStream) {
        this.elevatorCount = 0;
        this.elevatorFloors = new Vector<Integer>();
        this.buffer = new BufferedReader(new InputStreamReader(inStream));
    }

    /**
     * Returns next character in the stream. Gives -1 if end of file.
     */
    private int read() {
        try {
            int ch = buffer.read();
            return ch;
        } catch(IOException e) {
            error("read error");
        }
        return -1;
    }

    /**
     * Returns next character without removing it from the stream.
     */
    private int peek() {
        int ch = -1;
        try {
            buffer.mark(1);
            ch = read();
            buffer.reset();
        } catch(IOException e) {
            error("read error");
        }
        return ch;
    }

    /**
     * Read a sequence of white space characters.
     */
    private void readSpace() {
        int ch = peek();
        while (Character.isWhitespace(ch) && ch != -1) {
            read();
            ch = peek();
        }
    }

    /**
     * Read and return a sequence of characters (up to whitespace).
     */
    private String readString() {
        String str = "";
        int ch = peek();
        while(!Character.isWhitespace(ch) && ch != -1) {
            str += (char)read();
            ch = peek();
        }
        return str;
    }

    /**
     * Read a sequence of characters (digits) and return as an integer
     * value.
     */
    private int readInt() {
        String str = "";
        int ch = peek();
        while (!Character.isWhitespace(ch) && ch != -1) {
            str += (char)read();
            ch = peek();
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            error("expecting integer, found '" + str + "'");
        }
        return -1;
    }

    /**
     * Print an error message and exit the program.
     */
    private void error(String msg) {
        System.out.println("Error: " + msg);
        System.exit(1);
    }

    /**
     * Builds and runs the simulation
     */
    public void run() {
        readSpace();
        // check if anything to read
        if (peek() == -1) {
            return;
        }
        // first statement must be the number of elevators
        String str = readString();
        if (!str.equals("elevators")) {
            error("expecting elevators, found '" + str + "'");
        }

        readSpace(); //read white space after 'elevators'
        int count = readInt();
        setElevatorCount(count);

        // add to elevatorFloors vector
        for (int i = 0; i < elevatorCount; i++) {
            elevatorFloors.add(1);
        }

        readSpace();

        while (peek() != -1) {
            str = readString();
            if (!str.equals("up") && !str.equals("down")) {
                error("expecting 'up' or 'down', found '" + str + "'");
            }

            readSpace();
            int elevatorNumber = readInt();
            if (elevatorNumber > elevatorCount) {
                error("Invalid elevator number '" + elevatorNumber + "'");
            }
            readSpace();
            int floorsChange = readInt();

            shiftFloor(str, elevatorNumber, floorsChange);
            readSpace();
        }
    }

    /**
     * Performs the change of floors for each elevator
     * in the simulation.
     */
    public void shiftFloor(String str, int elevatorNumber, int floorsChange) {
        int startingFloor = elevatorFloors.get(elevatorNumber - 1);
        int endingFloor;

        if (str.equals("up")) {
            if (floorsChange < 1) {
                error("Invalid number of floors '" + floorsChange + "'");
            }
            // set the new floor elevator is on
            elevatorFloors.set(elevatorNumber - 1, (floor(elevatorNumber) + floorsChange));

            // if the floor goes from a positive value to a negative value
            // accounting for no floor zero
            endingFloor = elevatorFloors.get(elevatorNumber - 1);
            if (startingFloor < 0 && endingFloor > 0) {
                elevatorFloors.set(elevatorNumber - 1, (floor(elevatorNumber) + 1));
            }
        }

        else {
            elevatorFloors.set(elevatorNumber - 1, (floor(elevatorNumber) - floorsChange));

            // if the floor goes from a negative value to a positive value
            // accounting for no floor zero
            endingFloor = elevatorFloors.get(elevatorNumber - 1);
            if (startingFloor > 0 && endingFloor < 0) {
                elevatorFloors.set(elevatorNumber - 1, (floor(elevatorNumber) - 1));
            }
        }
    }

    /**
     * Return the number of elevators resulting after running the
     * simulation.
     */
    public int elevators() {
        return elevatorCount;
    }

    /**
     * Return the floor the given elevator is on after running the
     * simulation.
     */
    public int floor(int elevator_num) {
        return elevatorFloors.get(elevator_num - 1);
    }

    /**
     * Set the elevator count at the beginning of
     * simulation.
     */
    public void setElevatorCount(int elevatorCount) {
        if (elevatorCount < 1) {
            error("Invalid number of elevators '" + elevatorCount + "'");
        }
        this.elevatorCount = elevatorCount;
    }

}
