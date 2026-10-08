package minesweeper;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MineSweeperMain extends JFrame {
    private static final long serialVersionUID = 1L;

    private final GameBoardPanel board = new GameBoardPanel();
    private final JButton btnNewGame = new JButton("New Game");

    public MineSweeperMain() {
        super("MineSweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(board, BorderLayout.CENTER);
        cp.add(btnNewGame, BorderLayout.SOUTH);

        btnNewGame.addActionListener(e -> board.newGame());
        board.newGame();

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MineSweeperMain frame = new MineSweeperMain();
            frame.setVisible(true);
        });
    }
}
