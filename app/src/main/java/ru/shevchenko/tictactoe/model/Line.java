package ru.shevchenko.tictactoe.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Vladimir Shevchenko shevchen55@gmail.com 13/07/15.
 */
public class Line {
    private Set<Cell> cellRow;

    public Line() {
        this.cellRow = new HashSet<>();
    }

    public Line(Cell... cells) {
        this.cellRow = new HashSet<>();
        putCells(cells);
    }

    public void putCell(Cell cell) {
        cellRow.add(cell);
    }

    public void putCells(Cell... cells) {
        if (cells != null && cells.length > 0) {
            Collections.addAll(cellRow, cells);
        }
    }

    public Set<Cell> getCellRow() {
        return cellRow;
    }
}
