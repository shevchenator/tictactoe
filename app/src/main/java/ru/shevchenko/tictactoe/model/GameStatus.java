package ru.shevchenko.tictactoe.model;

/**
 * @author Vladimir Shevchenko shevchen55@gmail.com 12/07/15.
 */
public enum GameStatus {
    NOT_STARTED, PLAYING, DRAW, X_WIN, O_WIN;

    public boolean isGameOver() {
        return this.equals(GameStatus.X_WIN) ||
                this.equals(GameStatus.O_WIN) ||
                this.equals(GameStatus.DRAW);
    }

    public CellState toWinnerState() {
        if (this.equals(X_WIN)) {
            return CellState.X;
        } else if (this.equals(O_WIN)) {
            return CellState.O;
        }
        return CellState.BLANK;
    }
}
