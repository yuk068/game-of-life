package gameoflife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class GameOfLifeGUI extends JPanel {

    private final GameOfLife gameOfLife;
    private final int windowSize;
    private final int cellSize;
    private final Color gridLineColor;
    private final Color backgroundColor;
    private final Color cellColor;
    private boolean paintingMode;
    private boolean gameStarted;
    private boolean isDragging;

    public GameOfLifeGUI(int gridLength, int gridWidth, int numAlive, Color gridLineColor,
                         Color backgroundColor, Color cellColor, int updateDelay) {
        this.gameOfLife = new GameOfLife(gridLength, gridWidth, numAlive);
        this.windowSize = 750;
        this.cellSize = windowSize / gridLength;
        this.gridLineColor = gridLineColor;
        this.backgroundColor = backgroundColor;
        this.cellColor = cellColor;
        this.paintingMode = numAlive == 0;
        this.gameStarted = false;
        this.isDragging = false;

        setPreferredSize(new Dimension(windowSize, windowSize));
        setBackground(backgroundColor);
        setFocusable(true);
        requestFocusInWindow();

        Timer timer = new Timer(updateDelay, e -> {
            if (gameStarted) {
                gameOfLife.nextGeneration();
                repaint();
            }
        });

        timer.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isDragging = true;
                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;
                if (paintingMode && !gameStarted) {
                    toggleCellState(row, col);
                } else if (gameStarted) {
                    addPattern(row, col);
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    int row = e.getY() / cellSize;
                    int col = e.getX() / cellSize;
                    if (paintingMode && !gameStarted) {
                        toggleCellState(row, col);
                    } else if (gameStarted) {
                        addPattern(row, col);
                    }
                    repaint();
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    paintingMode = false;
                    gameStarted = true;
                }
            }
        });
    }

    private void toggleCellState(int row, int col) {
        if (row < gameOfLife.gridLength && col < gameOfLife.gridWidth) {
            gameOfLife.grid[row][col] = gameOfLife.grid[row][col] == GameOfLife.ALIVE ? GameOfLife.DEAD : GameOfLife.ALIVE;
        }
    }

    private void addPattern(int row, int col) {
        int[][] glider = {
                {0, 1, 0},
                {0, 0, 1},
                {1, 1, 1}
        };

        for (int i = 0; i < glider.length; i++) {
            for (int j = 0; j < glider[i].length; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (newRow < gameOfLife.gridLength && newCol < gameOfLife.gridWidth) {
                    gameOfLife.grid[newRow][newCol] = glider[i][j];
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
    }

    private void drawGrid(Graphics g) {
        int[][] grid = gameOfLife.grid;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == GameOfLife.ALIVE) {
                    g.setColor(cellColor);
                } else {
                    g.setColor(backgroundColor);
                }
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }

        g.setColor(gridLineColor);
        for (int i = 0; i <= grid.length; i++) {
            int position = i * cellSize;
            g.drawLine(position, 0, position, windowSize);
            g.drawLine(0, position, windowSize, position);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Game of Life");
            GameOfLifeGUI panel = new GameOfLifeGUI(150, 150, 0, Color.BLACK, Color.BLACK, Color.GREEN, 20);

            frame.add(panel);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setResizable(false);
        });
    }
}
