package gameoflife;

import java.util.HashSet;
import java.util.Random;

public class GameOfLife {

    int[][] grid;
    int gridLength;
    int gridWidth;
    int numCells;
    public static final int DEAD = 0;
    public static final int ALIVE = 1;

    public GameOfLife(int length, int width, int numAlive) {
        initializeField(length, width);
        initialPopulate(numAlive);
    }

    private void initializeField(int length, int width) {
        if (length <= 0 || width <= 0) throw new
                IllegalArgumentException("Field's length or/and width must be greater than 0");

        gridLength = length;
        gridWidth = width;
        grid = new int[gridLength][gridWidth];
        numCells = gridLength * gridWidth;
    }

    private void initialPopulate(int numAlive) {
        if (numAlive < 0 || numAlive >= numCells) throw new
                IllegalArgumentException("Invalid initial number of alive cells");

        HashSet<Integer> firstGenerationPositions = new HashSet<>();
        Random random = new Random();

        while (firstGenerationPositions.size() < numAlive) {
            int randomPosition = random.nextInt(numCells);
            firstGenerationPositions.add(randomPosition);
        }

        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridWidth; j++) {
                grid[i][j] = DEAD;
            }
        }

        for (int position : firstGenerationPositions) {
            int row = position / gridWidth;
            int col = position % gridWidth;
            grid[row][col] = ALIVE;
        }
    }

    public void nextGeneration() {
        int numRows = grid.length;
        int numCols = grid[0].length;
        int[][] newGrid = new int[numRows][numCols];

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int aliveNeighbors = countAliveNeighbors(grid, row, col);

                if (grid[row][col] == ALIVE) {
                    if (aliveNeighbors < 2 || aliveNeighbors > 3) {
                        newGrid[row][col] = DEAD;
                    } else {
                        newGrid[row][col] = ALIVE;
                    }
                } else {
                    if (aliveNeighbors == 3) {
                        newGrid[row][col] = ALIVE;
                    } else {
                        newGrid[row][col] = DEAD;
                    }
                }
            }
        }

        for (int row = 0; row < numRows; row++) {
            System.arraycopy(newGrid[row], 0, grid[row], 0, numCols);
        }
    }

    private int countAliveNeighbors(int[][] grid, int row, int col) {
        int numRows = grid.length;
        int numCols = grid[0].length;
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols) {
                    count += grid[newRow][newCol];
                }
            }
        }

        return count;
    }
}
