package searchclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class Heuristic implements Comparator<State> {
    private List<Goal> goalList = new ArrayList<Goal>();
	private Integer pitchMaxRow;
	private Integer pitchMaxCol;
    
	public Heuristic(State initialState, Pitch pitch) {
		pitchMaxRow = pitch.maxRow;
		pitchMaxCol = pitch.maxCol;
		for (int i = 0; i < pitch.maxRow; i++) {
			for (int j = 0; j < pitch.maxCol; j++) {
				if ('a' <= pitch.goals[i][j] && pitch.goals[i][j] <= 'z') {
					Goal goal = new Goal(pitch.goals[i][j], i, j, pitch);
					goalList.add(goal);
				}
			}
		}
	}

    public int h(State n) {
		int sum = 0;
		
		// Create list of boxes 
		List<Box> boxes = new ArrayList<Box>();
		for (int i = 0; i < pitchMaxRow; i++) {
			for (int j = 0; j < pitchMaxCol; j++) {
				if ('A' <= n.boxes[i][j] && n.boxes[i][j] <= 'Z') {
					boxes.add(new Box(n.boxes[i][j], i, j));
				}
			}
		}
		
		//Find shortest path from goal to matching box
		for (Goal goal : goalList) {
			int minPathLength = pitchMaxRow + pitchMaxCol;
			for (Box box : boxes) {
				if (Character.toLowerCase(box.letter) == goal.letter) {
					minPathLength = Math.min(minPathLength, goal.shortestPathLenght[box.row][box.col]);
				}
			}
			sum += minPathLength;
		}
		
		return sum;
    }

    public abstract int f(State n);

    @Override
    public int compare(State n1, State n2) {
        return this.f(n1) - this.f(n2);
    }

    public static class AStar extends Heuristic {
        public AStar(State initialState, Pitch pitch) {
            super(initialState, pitch);
        }

        @Override
        public int f(State n) {
            return n.g() + this.h(n);
        }

        @Override
        public String toString() {
            return "A* evaluation";
        }
    }

    public static class WeightedAStar extends Heuristic {
        private int W;

        public WeightedAStar(State initialState, Pitch pitch, int W) {
            super(initialState, pitch);
            this.W = W;
        }

        @Override
        public int f(State n) {
            return n.g() + this.W * this.h(n);
        }

        @Override
        public String toString() {
            return String.format("WA*(%d) evaluation", this.W);
        }
    }

    public static class Greedy extends Heuristic {
        public Greedy(State initialState, Pitch pitch) {
            super(initialState, pitch);
        }

        @Override
        public int f(State n) {
            return this.h(n);
        }

        @Override
        public String toString() {
            return "Greedy evaluation";
        }
    }
}
