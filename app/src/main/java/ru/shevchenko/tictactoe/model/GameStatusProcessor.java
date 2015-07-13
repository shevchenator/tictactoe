package ru.shevchenko.tictactoe.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Vladimir Shevchenko shevchen55@gmail.com 12/07/15.
 */
public class GameStatusProcessor {

    private Board board;
    private Line winningLine;

    public GameStatusProcessor(Board board) {
        this.board = board;
    }

    public GameStatus checkGameStatus() {
        if (board.isBlank()) {
            return GameStatus.NOT_STARTED;
        }

        Line winningRow = findWinningRow();
        Line winningColumn = findWinningColumn();
        Line winningDiagonal = findWinningDiagonal();

        if (winningRow != null) {
            winningLine = winningRow;
            return getWinner();
        } else if (winningColumn != null) {
            winningLine = winningColumn;
            return getWinner();
        } else if (winningDiagonal != null) {
            winningLine = winningDiagonal;
            return getWinner();
        } else if (board.isFull()) {
            return GameStatus.DRAW;
        }

        return GameStatus.PLAYING;
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

    public CellState getWinnerState() {
        Iterator<Cell> iterator = winningLine.getCellRow().iterator();
        if (iterator.hasNext()) {
            return board.getCellState(iterator.next());
        } else {
            return null;
        }
    }

    public GameStatus getWinner() {
        CellState cellState = getWinnerState();

        if (cellState != null && cellState.equals(CellState.X)) {
            return GameStatus.X_WIN;
        } else if (cellState != null && cellState.equals(CellState.O)) {
            return GameStatus.O_WIN;
        } else {
            return null;
        }
    }

    public Line getWinningLine() {
        return winningLine;
    }
}
