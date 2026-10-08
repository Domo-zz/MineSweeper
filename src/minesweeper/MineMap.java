package minesweeper;

import java.util.Random;
import static minesweeper.MineSweeperConstants.COLS;
import static minesweeper.MineSweeperConstants.ROWS;

public class MineMap {
    int numMines;
    boolean[][] isMined = new boolean[ROWS][COLS];
    public MineMap() {
        super();
    }
    public void newMineMap(int numMines) {
        this.numMines = numMines;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                isMined[i][j] = false;
            }
        }
        Random random = new Random();
        int placed = 0;
        while (placed < numMines) {
            int row = random.nextInt(ROWS);
            int col = random.nextInt(COLS);
            if (!isMined[row][col]) {
                isMined[row][col] = true;
                placed++;
            }
        }
    }
}
