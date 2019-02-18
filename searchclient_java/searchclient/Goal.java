package searchclient;

import java.awt.geom.Point2D;
import java.util.LinkedList;

public class Goal extends Box {
	int pitchMaxRow;
	int pitchMaxCol;
	public int[][] shortestPathLenght;

	public Goal(char letter, int row, int col, Pitch pitch) {
		super(letter, row, col);
		pitchMaxRow = pitch.maxRow;
		pitchMaxCol = pitch.maxCol;
		setShortestPathLength(pitch.walls);
	}
	
	// BFS implementation for finding shortest paths to all fields in pitch
	public void setShortestPathLength(boolean[][] walls) {			
		shortestPathLenght = new int[pitchMaxRow][pitchMaxCol];
		boolean[][] visited = new boolean[pitchMaxRow][pitchMaxCol];			
        LinkedList<Point2D.Float> queue = new LinkedList<Point2D.Float>(); 
        Point2D.Float field = new Point2D.Float(col, row);
        
        visited[(int) field.y][(int) field.x] = true;
        shortestPathLenght[(int) field.y][(int) field.x] = 0;
		queue.add(field); 
  
        while (queue.size() != 0) { 
        	field = queue.poll();
        	
        	//Upper neighbor
        	if (field.y-1 >= 0 && !walls[(int) field.y-1][(int) field.x] && !visited[(int) field.y-1][(int) field.x]) {
        		visited[(int) field.y-1][(int) field.x] = true;
        		queue.add(new Point2D.Float(field.x, field.y-1));
        		shortestPathLenght[(int) field.y-1][(int) field.x] = shortestPathLenght[(int) field.y][(int) field.x] + 1;
        	}
        	
        	//Lower neighbor
        	if (field.y+1 != pitchMaxRow && !walls[(int) field.y+1][(int) field.x] && !visited[(int) field.y+1][(int) field.x]) {
        		visited[(int) field.y+1][(int) field.x] = true;
        		queue.add(new Point2D.Float(field.x, field.y+1));
        		shortestPathLenght[(int) field.y+1][(int) field.x] = shortestPathLenght[(int) field.y][(int) field.x] + 1;
        	}
        	
        	//Left neighbor
        	if (field.x-1 >= 0 && !walls[(int) field.y][(int) field.x-1] && !visited[(int) field.y][(int) field.x-1]) {
        		visited[(int) field.y][(int) field.x-1] = true;
        		queue.add(new Point2D.Float(field.x-1, field.y));
        		shortestPathLenght[(int) field.y][(int) field.x-1] = shortestPathLenght[(int) field.y][(int) field.x] + 1;	        	
        	}
        	
        	//Right neighbor
        	if (field.x+1 != pitchMaxCol && !walls[(int) field.y][(int) field.x+1] && !visited[(int) field.y][(int) field.x+1]) {
        		visited[(int) field.y][(int) field.x+1] = true;
        		queue.add(new Point2D.Float(field.x+1, field.y));
        		shortestPathLenght[(int) field.y][(int) field.x+1] = shortestPathLenght[(int) field.y][(int) field.x] + 1;	        	
        	}	
        }
        
//        for (int i = 0; i < pitchMaxRow; i++) {
//        	for (int j = 0; j < pitchMaxCol; j++) {
//        		if (walls[i][j]) {
//        			System.err.print("+");
//        		} else {
//        			System.err.print(shortestPathLenght[i][j]);
//        		}
//        	}
//        	System.err.println();
//        }
	}
}

