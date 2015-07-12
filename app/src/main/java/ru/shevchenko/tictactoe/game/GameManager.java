package ru.shevchenko.tictactoe.game;

import java.util.HashSet;
import java.util.Set;

import ru.shevchenko.tictactoe.model.Board;
import ru.shevchenko.tictactoe.model.Cell;
import ru.shevchenko.tictactoe.model.CellState;
import ru.shevchenko.tictactoe.model.Line;

/**
 * @author Vladimir Shevchenko shevchen55@gmail.com 12/07/15.
 */
public class GameManager {
    private Board board;
    private CellState previousTurnState = CellState.O;
    private boolean isGameOver = false;

    private OnGameStateChangeListener gameStateChangeListener;

    public GameManager(OnGameStateChangeListener gameStateChangeListener) {
        this.gameStateChangeListener = gameStateChangeListener;
        startNewGame();
    }

    public void startNewGame() {
        this.board = new Board();
        this.isGameOver = false;
        this.previousTurnState = CellState.O;
    }

    public void makeMove(Cell cell) {
        if (!isGameOver &&
                board.getCellState(cell).equals(CellState.BLANK)) {
            if (previousTurnState.equals(CellState.O) && board.place(CellState.X, cell)) {
                gameStateChangeListener.onTurnMade(CellState.X, cell);
                previousTurnState = CellState.X;
            } else if (previousTurnState.equals(CellState.X) && board.place(CellState.O, cell)) {
                gameStateChangeListener.onTurnMade(CellState.O, cell);
                previousTurnState = CellState.O;
            }

            isGameOver = isGameOver();
        }
    }

    private boolean isGameOver() {
        Line winningRow = findWinningRow();
        Line winningColumn = findWinningColumn();
        Line winningDiagonal = findWinningDiagonal();

        if (winningRow != null) {
            gameStateChangeListener.win(previousTurnState, winningRow);
            return true;
        } else if (winningColumn != null) {
            gameStateChangeListener.win(previousTurnState, winningColumn);
            return true;
        } else if (winningDiagonal != null) {
            gameStateChangeListener.win(previousTurnState, winningDiagonal);
            return true;
        } else if (board.isFull()) {
            gameStateChangeListener.draw();
            return true;
        }

        return false;
    }

    private Line findWinningRow() {
        for (int i = 0; i < Board.MAX_X; i++) {
            Line row = new Line(new Cell(i, 0), new Cell(i, 1),new Cell(i, 2));
            if (isWinningLine(row)) {
                return row;
            }
        }
        return null;
    }

    private Line findWinningColumn() {
        for (int i = 0; i < Board.MAX_Y; i++) {
            Line column = new Line(new Cell(0, i), new Cell(1, i), new Cell(2, i));
            if (isWinningLine(column)) {
                return column;
            }
        }
        return null;
    }

    private Line findWinningDiagonal() {
        Line diagonal = new Line();
        for (int i = 0, j = 0; i < Board.MAX_X && j < Board.MAX_Y; i++, j++) {
            diagonal.putCell(new Cell(i, j));
        }
        if (isWinningLine(diagonal)) {
            return diagonal;
        }

        diagonal = new Line();
        for (int i = 0, j = Board.MAX_Y -1; i < Board.MAX_X && j >= 0 ; i++, j--) {
            diagonal.putCell(new Cell(i, j));
        }
        if (isWinningLine(diagonal)) {
            return diagonal;
        }
        return null;
    }

    private boolean isWinningLine(Line line) {
        Set<CellState> stateSet = new HashSet<>();
        for (Cell cell : line.getCellRow()) {
            stateSet.add(board.getCellState(cell));
        }
        return !stateSet.contains(CellState.BLANK) && stateSet.size() == 1;
    }
}
