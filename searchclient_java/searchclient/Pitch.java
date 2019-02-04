package searchclient;

public class Pitch {
    private static int MAX_ROW = 70;
    private static int MAX_COL = 70;
    public boolean[][] walls = new boolean[MAX_ROW][MAX_COL];
    public char[][] initBoxes = new char[MAX_ROW][MAX_COL];
    public char[][] goals = new char[MAX_ROW][MAX_COL];
	
    public int numberOfRows;			
    public int numberOfCols;

    public Pitch() {
    	super();
    }
    
	public void setSize(int numberOfRows, int numberOfCols) {
		this.numberOfRows = numberOfRows;
		this.numberOfCols = numberOfCols;

	    boolean[][] newWalls = new boolean[numberOfRows][numberOfCols];
	    char[][] newInitBoxes = new char[numberOfRows][numberOfCols];
	    char[][] newGoals = new char[numberOfRows][numberOfCols];
		
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfCols; j++) {
				newWalls[i][j] = walls[i][j];
				newInitBoxes[i][j] = initBoxes[i][j];
				newGoals[i][j] = goals[i][j];
			}
		}
		
		walls = newWalls;
		initBoxes = newInitBoxes;
		goals = newGoals;
	}
}
