package searchclient;

public class Pitch {
    private static int MAX_ROW = 70;
    private static int MAX_COL = 70;
    public boolean[][] walls = new boolean[MAX_ROW][MAX_COL];
    public char[][] initBoxes = new char[MAX_ROW][MAX_COL];
    public char[][] goals = new char[MAX_ROW][MAX_COL];
	
    public int maxRow;			
    public int maxCol;

    public Pitch() {
    	super();
    }
    
	public void setSize(int maxRow, int maxCol) {
		this.maxRow = maxRow;
		this.maxCol = maxCol;

	    boolean[][] newWalls = new boolean[maxRow][maxCol];
	    char[][] newInitBoxes = new char[maxRow][maxCol];
	    char[][] newGoals = new char[maxRow][maxCol];
		
		for (int i = 0; i < maxRow; i++) {
			for (int j = 0; j < maxCol; j++) {
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
