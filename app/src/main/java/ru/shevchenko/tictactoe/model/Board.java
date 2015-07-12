package ru.shevchenko.tictactoe.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Vladimir Shevchenko shevchen55@gmail.com 12/07/15.
 */
public class Board {
    public static final int MAX_X = 3;
    public static final int MAX_Y = 3;
    private Map<Cell, CellState> cellMap;

    public Board() {
        cellMap = new HashMap<>();

        for (int x = 0; x < Board.MAX_X; x++) {
            for (int y = 0; y < Board.MAX_Y; y++) {
                cellMap.put(new Cell(x, y), CellState.BLANK);
            }
        }
    }

    public boolean place(CellState state, Cell cell) {
        if (state != CellState.BLANK) {
            cellMap.put(cell, state);
            return true;
        }
        return false;
    }

    public CellState getCellState(Cell cell) {
        if (cellMap.containsKey(cell)) {
            return cellMap.get(cell);
        }
        return CellState.BLANK;
    }

    public Set<Cell> getAvailablePlaces() {
        Set<Cell> cellSet = new HashSet<>();
        for (Cell cell : cellMap.keySet()) {
            if (getCellState(cell) == CellState.BLANK) {
                cellSet.add(cell);
            }
        }
        return cellSet;
    }

    public boolean isBlank() {
        for (CellState cellState : cellMap.values()) {
            if (cellState != CellState.BLANK) {
                return false;
            }
        }
        return true;
    }

    public boolean isFull() {
        Set<CellState> cellStates = new HashSet<>(cellMap.values());
        return !cellStates.contains(CellState.BLANK) &&
                cellStates.contains(CellState.X) &&
                cellStates.contains(CellState.O);
    }
}
