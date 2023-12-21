import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class ColorTubes {

    // For reading input and storing values as we solve the problem
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    // To record all moves
    static ArrayList<String> moves;

    // To keep track of balls
    int[][] tubes;

    // To keep track of which tubes are okay to mess with
    boolean[] offLimits;

    /**
     * There is a new puzzle generating buzz on social media—Color Tubes. The rules
     * are relatively simple: you are given n+1 tubes filled with 3n colored balls.
     * Each
     * tube can hold at most 3 balls, and each color appears on exactly 3 balls (so
     * there are colors).
     * 
     * Using a series of moves, you are supposed to reach a Color Tubes state—each
     * tube should either hold 3 balls of a single color or it should be empty.
     * 
     * The only move allowed is to take the top ball from one tube and place it into
     * a different tube that has room for it (i.e. holds at most two balls before
     * the move).
     * 
     * You want to write a program to solve this puzzle for you. Initially, you are
     * not interested in an optimal solution, but you want your program to be good
     * enough to solve any puzzle configuration using at most 20n moves.
     * 
     * Link: https://open.kattis.com/problems/colortubes
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, IllegalStateException {

        int numColors = Integer.parseInt(reader.readLine().strip());
        ColorTubes sol = new ColorTubes();
        sol.tubes = new int[numColors + 1][3];
        sol.offLimits = new boolean[numColors+1];
        moves = new ArrayList<>();

        for (int i = 0; i < sol.tubes.length; i++) {
            ArrayList<String> tubeList = new ArrayList<String>(Arrays.asList(reader.readLine().split(" ")));
            int[] tube = new int[3];
            for (int j = 0; j < tubeList.size(); j++)
                tube[j] = Integer.parseInt(tubeList.get(j));

            sol.tubes[i] = tube;
        }

        sol.fixColors();

        System.out.println(moves.size());
        for (String move : moves)
            System.out.println(move);
    }

    /**
     * Helper method to solve all of the tubes from this index prior
     *
     */
    private void fixColors() throws IllegalStateException {
        for (int i = 0; i < tubes.length - 1; i++) {
            offLimits[i] = true;
            fixTube(i);
        }
    }

    /**
     * Helper method to fix the particular tube within the array of tubes
     * 
     * @param tubeIndex
     */
    private void fixTube(int tubeIndex) throws IllegalStateException {
        int bottomColor = tubes[tubeIndex][0];
        if (bottomColor == 0) { // empty tube - grab every ball from the last tube and put it in here
            if (tubeIndex < tubes.length - 1) {
                move(tubes.length - 1, tubeIndex);
                move(tubes.length - 1, tubeIndex);
                move(tubes.length - 1, tubeIndex);
            }
            fixTube(tubeIndex);
        } else {
            int[] thisTube = tubes[tubeIndex];
            if (thisTube[1] == bottomColor && thisTube[2] == bottomColor) {
                // we're done
                return;
            } else if (thisTube[1] == bottomColor && thisTube[2] == 0) {
                mm0(tubeIndex);
            } else if (thisTube[1] == bottomColor) {
                mmx(tubeIndex);
            } else if (thisTube[1] == 0) {
                m00(tubeIndex);
            } else if (thisTube[2] == 0) {
                mx0(tubeIndex);
            } else if (thisTube[2] == bottomColor) {
                mxm(tubeIndex);
            } else {
                mxy(tubeIndex);
            }
        }
    }

    /**
     * Ultimately becomes mm0
     * 
     * @param tubeIndex
     */
    private void mxy(int tubeIndex) throws IllegalStateException {
        // find another ball of the color needed
        int[] thisTube = tubes[tubeIndex];
        int color = thisTube[0];
        int getColorFromHere = -1;
        for (int i = tubeIndex + 1; i < tubes.length; i++) {
            int[] tube = tubes[i];
            if (tube[0] == color || tube[1] == color || tube[2] == color) {
                getColorFromHere = i;
                break;
            }
        }

        // we know which tube we need to get the ball from
        int[] otherTube = tubes[getColorFromHere];
        if (otherTube[2] == color) {
            // then simply dump the two top balls from tubeIndex and move this colored ball
            // in
            // getColorFromHere is full, so no dumping of balls into it will occur
            dumpTopBall(tubeIndex);
            dumpTopBall(tubeIndex);
            move(getColorFromHere, tubeIndex);

        } else if (otherTube[1] == color) { // tube with the color has the color in the MIDDLE
            if (otherTube[2] != 0)
                dumpTopBall(getColorFromHere);
            // at this point, we know that there are two open slots
            int[] openTubes = new int[] { -1, -1 };
            for (int i = 0; i < offLimits.length; i++) {
                if (i != getColorFromHere && !offLimits[i]) {
                    int[] dumpInHere = tubes[i];
                    if (dumpInHere[1] == 0) {
                        // this is the only tube we will need to dump x and y into
                        openTubes[0] = i;
                        openTubes[1] = -1; // should not be necessary but just in case
                        // (because only 3 open spots exist and two of them are here and one is in
                        // getColorFromHere)
                        break;
                    } else if (dumpInHere[2] == 0) {
                        if (openTubes[0] == -1) // we have not found a tube to dump x and y into yet
                            openTubes[0] = i;
                        else { // we have found both tubes to dump x and y into
                            openTubes[1] = i;
                            break;
                        }
                    }
                }
            }
            // we found the (up to) two tubes to dump x and y into
            if (openTubes[1] != -1) {
                move(tubeIndex, openTubes[1]);
                move(tubeIndex, openTubes[0]);
            } else if (openTubes[0] != -1) {// one tube served as both
                move(tubeIndex, openTubes[0]);
                move(tubeIndex, openTubes[0]);
            }

            // we are now ready to move m from getColorFromHere to tubeIndex
            move(getColorFromHere, tubeIndex);

        } else { // tube with the color has the color in the BOTTOM
            if (otherTube[2] != 0)
                dumpTopBall(getColorFromHere);
            if (otherTube[1] != 0)
                dumpTopBall(getColorFromHere);
            // at this point, there is one other tube with exactly one open slot up top
            int putColoredBallHere = -1;
            for (int i = tubeIndex + 1; i < tubes.length; i++) {
                if (i != getColorFromHere) {
                    if (tubes[i][2] == 0) {
                        putColoredBallHere = i;
                        break;
                    }
                }
            }

            // put the colored ball that we want at the top of putColoredBallHere
            move(getColorFromHere, putColoredBallHere);

            // remove the two non-correct colored balls above the colored one in our current
            // tube out
            dumpTopBall(tubeIndex);
            dumpTopBall(tubeIndex);

            // now bring back the colored ball that we want at the second spot of this tube
            move(putColoredBallHere, tubeIndex);
        }

        // now we can progress to a different case
        mm0(tubeIndex);

    }

    /**
     * Soon becomes mxy
     * 
     * @param tubeIndex
     */
    private void mx0(int tubeIndex) throws IllegalStateException {
        pullNonColor(tubeIndex);
        mxy(tubeIndex);
    }

    /**
     * Soon becomes mx0
     * 
     * @param tubeIndex
     */
    private void m00(int tubeIndex) throws IllegalStateException {
        pullNonColor(tubeIndex);
        if (tubes[tubeIndex][1] == 0) { 
            // There was no other color to pull, which means only one tube is remaining and it has our two other colors
            move(tubeIndex+1, tubeIndex);
            move(tubeIndex+1, tubeIndex);
        }
        else {
            mx0(tubeIndex);
        }
    }

    /**
     * Soon becomes mx0
     * 
     * @param tubeIndex
     */
    private void mxm(int tubeIndex) throws IllegalStateException {
        dumpTopBall(tubeIndex);
        mx0(tubeIndex);
    }

    /**
     * Soon becomes mm0
     * 
     * @param tubeIndex
     */
    private void mmx(int tubeIndex) throws IllegalStateException {
        dumpTopBall(tubeIndex);
        mm0(tubeIndex);
    }

    /**
     * Base case for handling fixing a tube to a certain color
     * 
     * @param tubeIndex
     */
    private void mm0(int tubeIndex) throws IllegalStateException {
        int[] tube = tubes[tubeIndex];
        int color = tube[0];

        // find a tube with the color we need
        for (int i = 0; i < tubes.length; i++) {
            if (offLimits[i] || i==tubeIndex)
                continue;
            int[] otherTube = tubes[i];
            if (otherTube[2] == color || otherTube[1] == color || otherTube[0] == color) {
                if (otherTube[2] == color) {
                    move(i, tubeIndex);
                    break;
                } else if (otherTube[1] == color) {
                    if (otherTube[2] != 0)
                        dumpTopBall(i);
                    move(i, tubeIndex);
                    break;
                } else if (otherTube[0] == color) {
                    if (otherTube[2] != 0)
                        dumpTopBall(i);
                    if (otherTube[1] != 0)
                        dumpTopBall(i);
                    move(i, tubeIndex);
                    break;
                }
            }
        }
    }

    /**
     * Helper method to record and perform a movement of balls between tubes
     * 
     * @param from - index of the tube from which a ball is being taken
     * @param to   - index of the tube to which that same ball is going
     */
    private void move(int from, int to) throws IllegalStateException {
        if (tubes[from][0] == 0 || tubes[to][2] != 0)
            throw new IllegalStateException("Tried to move from an empty tube or to a full tube (or both)");

        moves.add(String.valueOf(from + 1) + " " + String.valueOf(to + 1));
        int[] fromTube = tubes[from];
        int[] toTube = tubes[to];

        int fromNowFree = (fromTube[2] == 0) ? ((fromTube[1] == 0) ? 0 : 1) : 2;
        int colorMoving = fromTube[fromNowFree];

        int toNoLongerFree = (toTube[1] != 0) ? 2 : (toTube[0] != 0) ? 1 : 0;

        fromTube[fromNowFree] = 0;
        toTube[toNoLongerFree] = colorMoving;
    }

    /**
     * Helper method to pull one ball not equal to this tube's color (bottom ball)
     * from another tube
     * 
     * @param tubeIndex
     * @param offLimits - do not mess with tubes with an index lower than or equal
     *                  to this one
     * @param tubes     - the array of tubes
     */
    private void pullNonColor(int tubeIndex) throws IllegalStateException {
        int color = tubes[tubeIndex][0];

        // now go find a tube who has a ball NOT equal to this tube's bottom color
        int takeFromHere = -1;
        for (int i = 0; i < offLimits.length; i++) {
            if (offLimits[i] || i == tubeIndex)
                continue;
            else if ((tubes[i][2] != color && tubes[i][2] != 0) || (tubes[i][1] != color && tubes[i][1] != 0) || (tubes[i][0] != color && tubes[i][0] != 0)) {
                if (tubes[i][0] != 0) {
                    takeFromHere = i;
                    break;
                }
            }
        }

        if (takeFromHere != -1)
        {
            int[] tubeWithBallToTake = tubes[takeFromHere];
            if (tubeWithBallToTake[2] != 0 && tubeWithBallToTake[2] != color)
                move(takeFromHere, tubeIndex);
            else if (tubeWithBallToTake[1] != 0 && tubeWithBallToTake[1] != color) {
                if (tubeWithBallToTake[2] != 0)
                    dumpTopBall(takeFromHere);
                move(takeFromHere, tubeIndex);
            } else {
                if (tubeWithBallToTake[2] != 0)
                    dumpTopBall(takeFromHere);
                if (tubeWithBallToTake[1] != 0)
                    dumpTopBall(takeFromHere);
                move(takeFromHere, tubeIndex);
            }
        }
    }

    /**
     * Helper method to dump the top ball of a tube to another location
     * 
     * @param tubeIndex
     * @param tubes     - the array of tubes
     */
    private void dumpTopBall(int tubeIndex) throws IllegalStateException {
        // look for another tube that has a top spot free
        for (int i = 0; i < offLimits.length; i++) {
            if (i == tubeIndex)
                continue;
            else if (!offLimits[i] && tubes[i][2] == 0) { // this tube is not full
                move(tubeIndex, i);
                break;
            }
        }
    }

}