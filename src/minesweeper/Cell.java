package minesweeper;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

public class Cell extends JButton {
    private static final long serialVersionUID = 1L;

    public static final Color BG_NOT_REVEALED = new Color(150, 150, 150);
    public static final Color FG_NOT_REVEALED = Color.RED;
    public static final Color BG_REVEALED = new Color(240, 240, 240);
    public static final Color FG_REVEALED = new Color(30, 30, 30);
    public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 24);
    public static final Color BG_FLAGGED = new Color(255, 220, 90);
    public static final Color FG_FLAGGED = new Color(200, 0, 0);
    // Classic minesweeper colors for the numbers 1-8
    public static final Color[] NUMBER_COLORS = {
        FG_REVEALED,
        new Color(0, 0, 255),
        new Color(0, 128, 0),
        new Color(255, 0, 0),
        new Color(0, 0, 128),
        new Color(128, 0, 0),
        new Color(0, 128, 128),
        Color.BLACK,
        new Color(90, 90, 90)
    };

    int row;
    int col;
    boolean isRevealed;
    boolean isMined;
    boolean isFlagged;

    public Cell(int row, int col) {
        super();
        this.row = row;
        this.col = col;
        setFont(FONT_NUMBERS);
        setFocusable(false);
        setOpaque(true);
        setBorderPainted(false);
        paint();
    }

    public void newGame(boolean isMined) {
        this.isRevealed = false;
        this.isFlagged = false;
        this.isMined = isMined;
        setEnabled(true);
        setText("");
        paint();
    }

    public void paint() {
        if (!isRevealed) {
            if (isFlagged) {
                setForeground(FG_FLAGGED);
                setBackground(BG_FLAGGED);
                setText("F");
            } else {
                setForeground(FG_NOT_REVEALED);
                setBackground(BG_NOT_REVEALED);
                setText("");
            }
        } else if (isMined) {
            setForeground(Color.BLACK);
            setBackground(Color.RED);
            setText("*");
        } else {
            setBackground(BG_REVEALED);
            setForeground(numberColor());
        }
    }

    private Color numberColor() {
        try {
            return NUMBER_COLORS[Integer.parseInt(getText())];
        } catch (NumberFormatException e) {
            return FG_REVEALED;
        }
    }
}
