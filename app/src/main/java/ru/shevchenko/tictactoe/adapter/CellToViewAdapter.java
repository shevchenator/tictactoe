package ru.shevchenko.tictactoe.adapter;

import java.util.HashMap;
import java.util.Map;

import ru.shevchenko.tictactoe.R;
import ru.shevchenko.tictactoe.model.Cell;

/**
 * @author Vladimir Shevchenko shevchen55@gmail.com 13/07/15.
 */
public class CellToViewAdapter {
    private static final Map<Integer, Cell> viewCellMap;
    private static final Map<Cell,Integer> cellViewMap;
    static {
        viewCellMap = new HashMap<>();
        viewCellMap.put(R.id.game_cell_1_1, new Cell(0, 0));
        viewCellMap.put(R.id.game_cell_1_2, new Cell(0, 1));
        viewCellMap.put(R.id.game_cell_1_3, new Cell(0, 2));
        viewCellMap.put(R.id.game_cell_2_1, new Cell(1, 0));
        viewCellMap.put(R.id.game_cell_2_2, new Cell(1, 1));
        viewCellMap.put(R.id.game_cell_2_3, new Cell(1, 2));
        viewCellMap.put(R.id.game_cell_3_1, new Cell(2, 0));
        viewCellMap.put(R.id.game_cell_3_2, new Cell(2, 1));
        viewCellMap.put(R.id.game_cell_3_3, new Cell(2, 2));
        cellViewMap = new HashMap<>();
        cellViewMap.put(new Cell(0, 0), R.id.game_cell_1_1);
        cellViewMap.put(new Cell(0, 1), R.id.game_cell_1_2);
        cellViewMap.put(new Cell(0, 2), R.id.game_cell_1_3);
        cellViewMap.put(new Cell(1, 0), R.id.game_cell_2_1);
        cellViewMap.put(new Cell(1, 1), R.id.game_cell_2_2);
        cellViewMap.put(new Cell(1, 2), R.id.game_cell_2_3);
        cellViewMap.put(new Cell(2, 0), R.id.game_cell_3_1);
        cellViewMap.put(new Cell(2, 1), R.id.game_cell_3_2);
        cellViewMap.put(new Cell(2, 2), R.id.game_cell_3_3);
    }

    public static Cell viewToCell(int resId) {
        return viewCellMap.get(resId);
    }

    public static int cellToView(Cell cell) {
        return cellViewMap.get(cell);
    }
}
