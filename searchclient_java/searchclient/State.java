package searchclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class State {
    private static final Random RNG = new Random(1);

    public int agentRow;
    public int agentCol;

    // Arrays are indexed from the top-left of the level, with first index being row and second being column.
    // Row 0: (0,0) (0,1) (0,2) (0,3) ...
    // Row 1: (1,0) (1,1) (1,2) (1,3) ...
    // Row 2: (2,0) (2,1) (2,2) (2,3) ...
    // ...
    // (Start in the top left corner, first go down, then go right)
    // E.g. this.walls[2] is an array of booleans having size MAX_COL.
    // this.walls[row][col] is true if there's a wall at (row, col)
    
    public char[][] boxes;

    public State parent;
    public Command action;

    private int g;

    private int _hash = 0;

    public State(State parent) {
        this.parent = parent;
        if (parent == null) {
            this.g = 0;
        } else {
            this.g = parent.g() + 1;
        }
    }

    public int g() {
        return this.g;
    }

    public boolean isInitialState() {
        return this.parent == null;
    }

    public boolean isGoalState(Pitch pitch) {
        for (int row = 1; row < pitch.maxRow - 1; row++) {
            for (int col = 1; col < pitch.maxCol - 1; col++) {
                char g = pitch.goals[row][col];
                char b = Character.toLowerCase(boxes[row][col]);
                if (g > 0 && b != g) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<State> getExpandedStates(Pitch pitch) {
        ArrayList<State> expandedStates = new ArrayList<>(Command.EVERY.length);
        for (Command c : Command.EVERY) {
            // Determine applicability of action
            int newAgentRow = this.agentRow + Command.dirToRowChange(c.dir1);
            int newAgentCol = this.agentCol + Command.dirToColChange(c.dir1);

            if (c.actionType == Command.Type.Move) {
                // Check if there's a wall or box on the cell to which the agent is moving
                if (this.cellIsFree(newAgentRow, newAgentCol, pitch)) {
                    State n = this.ChildState(pitch);
                    n.action = c;
                    n.agentRow = newAgentRow;
                    n.agentCol = newAgentCol;
                    expandedStates.add(n);
                }
            } else if (c.actionType == Command.Type.Push) {
                // Make sure that there's actually a box to move
                if (this.boxAt(newAgentRow, newAgentCol)) {
                    int newBoxRow = newAgentRow + Command.dirToRowChange(c.dir2);
                    int newBoxCol = newAgentCol + Command.dirToColChange(c.dir2);
                    // .. and that new cell of box is free
                    if (this.cellIsFree(newBoxRow, newBoxCol, pitch)) {
                        State n = this.ChildState(pitch);
                        n.action = c;
                        n.agentRow = newAgentRow;
                        n.agentCol = newAgentCol;
                        n.boxes[newBoxRow][newBoxCol] = this.boxes[newAgentRow][newAgentCol];
                        n.boxes[newAgentRow][newAgentCol] = 0;
                        expandedStates.add(n);
                    }
                }
            } else if (c.actionType == Command.Type.Pull) {
                // Cell is free where agent is going
                if (this.cellIsFree(newAgentRow, newAgentCol, pitch)) {
                    int boxRow = this.agentRow + Command.dirToRowChange(c.dir2);
                    int boxCol = this.agentCol + Command.dirToColChange(c.dir2);
                    // .. and there's a box in "dir2" of the agent
                    if (this.boxAt(boxRow, boxCol)) {
                        State n = this.ChildState(pitch);
                        n.action = c;
                        n.agentRow = newAgentRow;
                        n.agentCol = newAgentCol;
                        n.boxes[this.agentRow][this.agentCol] = this.boxes[boxRow][boxCol];
                        n.boxes[boxRow][boxCol] = 0;
                        expandedStates.add(n);
                    }
                }
            }
        }
        Collections.shuffle(expandedStates, RNG);
        return expandedStates;
    }

    private boolean cellIsFree(int row, int col, Pitch pitch) {
        return !pitch.walls[row][col] && this.boxes[row][col] == 0;
    }

    private boolean boxAt(int row, int col) {
        return this.boxes[row][col] > 0;
    }

    private State ChildState(Pitch pitch) {
        State copy = new State(this);
        copy.boxes = new char[pitch.maxRow][pitch.maxCol];
        for (int row = 0; row < pitch.maxRow; row++) {
            System.arraycopy(this.boxes[row], 0, copy.boxes[row], 0, pitch.maxCol);
        }
        return copy;
    }

    public ArrayList<State> extractPlan() {
		ArrayList<State> plan = new ArrayList<>();
        State n = this;
        while (!n.isInitialState()) {
            plan.add(n);
            n = n.parent;
        }
        Collections.reverse(plan);
        return plan;
    }

    @Override
    public int hashCode() {
        if (this._hash == 0) {
            final int prime = 31;
            int result = 1;
            result = prime * result + this.agentCol;
            result = prime * result + this.agentRow;
            result = prime * result + Arrays.deepHashCode(this.boxes);
            this._hash = result;
        }
        return this._hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        State other = (State) obj;
        if (this.agentRow != other.agentRow || this.agentCol != other.agentCol)
            return false;
        return Arrays.deepEquals(this.boxes, other.boxes);
    }

    public String toString(Pitch pitch) {
        StringBuilder s = new StringBuilder();
        for (int row = 0; row < pitch.maxRow; row++) {
            if (!pitch.walls[row][0]) {
                break;
            }
            for (int col = 0; col < pitch.maxCol; col++) {
                if (this.boxes[row][col] > 0) {
                    s.append(this.boxes[row][col]);
                } else if (pitch.goals[row][col] > 0) {
                    s.append(pitch.goals[row][col]);
                } else if (pitch.walls[row][col]) {
                    s.append("+");
                } else if (row == this.agentRow && col == this.agentCol) {
                    s.append("0");
                } else {
                    s.append(" ");
                }
            }
            s.append("\n");
        }
        return s.toString();
    }

}