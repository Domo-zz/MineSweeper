package minesweeper;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import static minesweeper.MineSweeperConstants.COLS;
import static minesweeper.MineSweeperConstants.NUM_MINES;
import static minesweeper.MineSweeperConstants.ROWS;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public static final int CELL_SIZE = 50;
    public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
    Cell[][] cells = new Cell[ROWS][COLS];
    int numMines = NUM_MINES;
    private boolean firstClick = true;
    private boolean gameOver = false;
    public GameBoardPanel() {
        super.setLayout(new GridLayout(ROWS, COLS, 2, 2));
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cells[row][col] = new Cell(row, col);
                cells[row][col].addMouseListener(new CellMouseListener());
                super.add(cells[row][col]);
            }
        }
        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    }
    public void newGame() {
        firstClick = true;
        gameOver = false;
        MineMap mineMap = new MineMap();
        mineMap.newMineMap(numMines);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cells[row][col].newGame(mineMap.isMined[row][col]);
            }
        }
    }
    // Move a mine off the first-clicked cell to a random free cell
    private void relocateMine(int safeRow, int safeCol) {
        java.util.Random random = new java.util.Random();
        while (true) {
            int row = random.nextInt(ROWS);
            int col = random.nextInt(COLS);
            if (!cells[row][col].isMined && !(row == safeRow && col == safeCol)) {
                cells[row][col].isMined = true;
                cells[safeRow][safeCol].isMined = false;
                return;
            }
        }
    }
    private int getSurroundingMines(int srcRow, int srcCol) {
        int count = 0;
        for (int row = srcRow - 1; row <= srcRow + 1; row++) {
            for (int col = srcCol - 1; col <= srcCol + 1; col++) {
                if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                    if (cells[row][col].isMined) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    private void revealCell(int srcRow, int srcCol) {
        if (srcRow < 0 || srcRow >= ROWS || srcCol < 0 || srcCol >= COLS) {
            return;
        }
        Cell cell = cells[srcRow][srcCol];
        if (cell.isRevealed || cell.isFlagged) {
            return;
        }
        if (cell.isMined) {
            cell.isRevealed = true;
            cell.paint();
            revealAllMines();
            gameOver = true;
            JOptionPane.showMessageDialog(this, "Game Over!", "Minesweeper", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int mineCount = getSurroundingMines(srcRow, srcCol);
        cell.isRevealed = true;
        if (mineCount == 0) {
            cell.setText("");
        } else {
            cell.setText(String.valueOf(mineCount));
        }
        cell.paint();
        if (mineCount == 0) {
            for (int row = srcRow - 1; row <= srcRow + 1; row++) {
                for (int col = srcCol - 1; col <= srcCol + 1; col++) {
                    if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                        if (!cells[row][col].isMined && !cells[row][col].isRevealed) {
                            revealCell(row, col);
                        }
                    }
                }
            }
        }
        if (!gameOver && hasWon()) {
            gameOver = true;
            JOptionPane.showMessageDialog(this, "You Win!", "Minesweeper", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void chord(int srcRow, int srcCol) {
        Cell cell = cells[srcRow][srcCol];
        if (!cell.isRevealed || cell.isMined) {
            return;
        }
        int number;
        try {
            number = Integer.parseInt(cell.getText());
        } catch (NumberFormatException e) {
            return;
        }
        int flags = 0;
        for (int row = srcRow - 1; row <= srcRow + 1; row++) {
            for (int col = srcCol - 1; col <= srcCol + 1; col++) {
                if (row >= 0 && row < ROWS && col >= 0 && col < COLS && cells[row][col].isFlagged) {
                    flags++;
                }
            }
        }
        if (flags != number) {
            return;
        }
        for (int row = srcRow - 1; row <= srcRow + 1; row++) {
            for (int col = srcCol - 1; col <= srcCol + 1; col++) {
                if (gameOver) {
                    return;
                }
                revealCell(row, col);
            }
        }
    }
    private void revealAllMines() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col].isMined) {
                    cells[row][col].isRevealed = true;
                    cells[row][col].paint();
                }
            }
        }
    }
    public boolean hasWon() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Cell cell = cells[row][col];
                if (!cell.isMined && !cell.isRevealed) {
                    return false;
                }
            }
        }
        return true;
    }
    private class CellMouseListener extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (gameOver) {
                return;
            }
            Cell sourceCell = (Cell) e.getSource();
            if (!sourceCell.contains(e.getPoint())) {
                return;
            }
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (sourceCell.isRevealed) {
                    chord(sourceCell.row, sourceCell.col);
                    return;
                }
                if (sourceCell.isFlagged) {
                    return;
                }
                if (firstClick) {
                    firstClick = false;
                    if (sourceCell.isMined) {
                        relocateMine(sourceCell.row, sourceCell.col);
                    }
                }
                revealCell(sourceCell.row, sourceCell.col);
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                if (sourceCell.isRevealed) {
                    return;
                }
                sourceCell.isFlagged = !sourceCell.isFlagged;
                sourceCell.paint();
            }
        }
    }
}
